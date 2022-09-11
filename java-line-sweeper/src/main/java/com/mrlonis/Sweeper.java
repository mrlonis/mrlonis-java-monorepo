package com.mrlonis;

/**
 * [read-only]
 * 
 * Given a set of n line segments, find a pair that intersects (if such a pair exists).
 * 
 * Simplifications:
 * - there are no vertical line segments
 * - there are no three-way (or more) intersections
 * 
 * The brute force solution compares all pairs of line segments for intersection and 
 * is in O(n^2).
 * 
 * O(n log n) Line Sweep Algorithm:
 * - Sort all end-points of the line segments from left to right (along the x-axis).
 * - Sweep a vertical line from left to right, stopping at each end-point.
 * - Maintain a BST of all the segments that intersect the sweep line, sorted by where
 *   they cross the sweep line at the current x-coordinate.
 * - When a segment is added to the tree (because the sweep line touches its left
 *   endpoint), check whether it intersects with the one immediately above it or the 
 *   one immediately below it on the sweep line.
 * - When a segment is removed from the tree (because the sweep line touches its
 *   right endpoint), check whether the lines immediately above and immediately below 
 *   intersect with each other.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class Sweeper {
	private int sweepX; // x-coordinate of the sweep line
	private List<LineSegment> world; // all the line segments
	private BinarySearchTree<LineSegment> tr; // those segments that intersect
												// the sweep line

	public Sweeper(List<LineSegment> world, int treeType) {
		this.world = world;
		/**
		 * Note that the relationship on which the tree is organized changes
		 * with the changing sweep line. This means we have to rebuild if we
		 * want another intersection (and to mitigate the effect of the dirty
		 * segments in the tree).
		 */
		BiPredicate<LineSegment, LineSegment> sweepPred = (LineSegment s1, LineSegment s2) -> s1.lessThan(s2, sweepX);
		if (treeType == Constants.BST)
			tr = new BinarySearchTree<>(sweepPred);
		else
			tr = new AVLTree<>(sweepPred);
	}

	/**
	 * Returns the current x-coordinate of the sweep line.
	 */
	public int getSweepX() {
		return sweepX;
	}

	/**
	 * Runs the sweep from left to right until either an intersection is
	 * detected, in which case true is returns, or the rightmost endpoint is
	 * processed, in which case false is returned.
	 */
	public boolean run() {
		// Create a list of all the endpoints in the world.
		List<Endpoint> endpoints = new ArrayList<>();

		// Create a dictionary that allows us to lookup segments
		// in the world using one of their endpoints as the key.
		Map<Endpoint, LineSegment> dict = new HashMap<>();
		for (LineSegment seg : world) {
			Endpoint end;
			dict.put(end = seg.getLeftEndpoint(), seg);
			endpoints.add(end);
			dict.put(end = seg.getRightEndpoint(), seg);
			endpoints.add(end);
		}

		// Sort the endpoints by their x-coordinates.
		Collections.sort(endpoints);

		for (Endpoint currEndpoint : endpoints) {
			// Move the sweep line to the current endpoint.
			sweepX = (int) currEndpoint.getX();
			LineSegment currSeg = dict.get(currEndpoint);
			if (currEndpoint.onLeft()) {
				// Process a left endpoint.
				// Add the segment to the tree because it intersects the sweep
				// line.
				Location<LineSegment> loc = tr.insert(currSeg);
				Location<LineSegment> above = loc.getBefore();
				if (above != null) {
					if (currSeg.intersects(above.get())) {
						currSeg.highlight();
						above.get().highlight();
						return true;
					}
				}
				Location<LineSegment> below = loc.getAfter();
				if (below != null) {
					if (currSeg.intersects(below.get())) {
						currSeg.highlight();
						below.get().highlight();
						return true;
					}
				}
			} else {
				// Process a right endpoint.
				Location<LineSegment> loc = tr.search(currSeg);
				if (loc == null) {
					tr.rebuild();
					if ((loc = tr.search(currSeg)) == null)
						throw new SweeperException(currSeg, tr); // shouldn't
																	// happen,
																	// but just
																	// in
																	// case...
				}
				Location<LineSegment> above = loc.getBefore();
				Location<LineSegment> below = loc.getAfter();
				tr.remove(currSeg);
				if (above != null && below != null && above.get().intersects(below.get())) {
					above.get().highlight();
					below.get().highlight();
					return true;
				}
			}
		}
		return false;
	}
}

/**
 * This exception is useful for debugging purposes. It is caught by the GUI,
 * which highlights the segment and the sweep line and also prints the state of
 * the tree.
 */

class SweeperException extends RuntimeException {
	/**
	 * Added default serial ID to remove Eclipse warning.
	 */
	private static final long serialVersionUID = 1L;
	
	private LineSegment seg;
	private Tree<LineSegment> tr;

	public SweeperException(LineSegment seg, Tree<LineSegment> tr) {
		super("Sweeper: the search failed to locate " + seg + " in the tree while processing its right endpoint.");
		this.seg = seg;
		this.tr = tr;
	}

	public LineSegment getSeg() {
		return seg;
	}

	public Tree<LineSegment> getTree() {
		return tr;
	}
}

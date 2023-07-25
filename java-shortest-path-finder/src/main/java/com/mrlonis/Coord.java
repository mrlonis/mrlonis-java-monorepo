package com.mrlonis;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coord represents an (x,y)-coordinate on a 2D board. The origin, (0,0) is assumed to be in the upper left corner.
 * X-coordinates increase from left to right. Y-coordinates increase from top to bottom. Operations are provided to
 * easily locate neighboring coordinates (in the NSEW compass directions) on a board.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Coord implements Comparable<Coord> {

    protected int x, y;

    /**
     * Constructs a new Coord that is a copy of the given Coord.
     */
    public Coord(Coord coord) {
        this(coord.x, coord.y);
    }

    /**
     * Constructs a new Coord representing (x,y).
     */
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Coord representing (x,y).
     */
    public Coord(String x, String y) {
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
    }

    /**
     * Returns a list of the immediate board coordinates of this Coord's north, south, east, and west neighbors.
     */
    public List<Coord> neighbors(Dimension dim) {
        List<Coord> ans = new ArrayList<>();
        for (Coord coord : new Coord[]{up(), down(), right(), left()}) { // NSEW
            if (coord.onBoard(dim)) {
                ans.add(coord);
            }
        }
        return ans;
    }

    /**
     * Returns the Coord that is directly above (i.e., north of) this one.
     */
    public Coord up() {
        return new Coord(x, y - 1);
    }

    /**
     * Returns the Coord that is directly below (i.e., south of) this one.
     */
    public Coord down() {
        return new Coord(x, y + 1);
    }

    /**
     * Returns the Coord that is immediately to the right (i.e., east) of this one.
     */
    public Coord right() {
        return new Coord(x + 1, y);
    }

    /**
     * Returns the Coord that is immediately to the left (i.e., west) of this one.
     */
    public Coord left() {
        return new Coord(x - 1, y);
    }

    /**
     * Returns true iff this Coord is visible on a board of the given dimensions.
     */
    public boolean onBoard(Dimension dim) {
        return x >= 0 && x < dim.width && y >= 0 && y < dim.height;
    }

    /**
     * Returns a list of the immediate board coordinates of this Coord's north, south, east, and west neighbors.
     *
     * @param dim
     *         The dimension of the board for the given coordinate.
     *
     * @return A HashMap consisting of the neighbors for the given coordinate with strings as keys representing the
     * direction the neighbor lies from the given coordinate.
     */
    public Map<String, Coord> neighborsMap(Dimension dim) {
        Map<String, Coord> ans = new HashMap<>();
        Coord up = up();
        Coord down = down();
        Coord left = left();
        Coord right = right();

        if (up.onBoard(dim)) {
            ans.put("Up", up);
        }

        if (down.onBoard(dim)) {
            ans.put("Down", down);
        }

        if (left.onBoard(dim)) {
            ans.put("Left", left);
        }

        if (right.onBoard(dim)) {
            ans.put("Right", right);
        }

        return ans;
    }

    /**
     * Returns the minimal distance separating this coord and the given coord. The result corresponds to the length of
     * the shortest connecting wire. This is also known of as the Manhattan Distance between two points on a grid.
     */
    public int separation(Coord to) {
        return Math.abs(Math.abs(this.x - to.x) + Math.abs(this.y - to.y));
    }

    /**
     * Coords are ordered first on the x-coordinate and then on the y-coordinate.
     */
    public int compareTo(Coord that) {
        if (this.x == that.x) {
            return this.y - that.y;
        }
        return this.x - that.x;
    }

    /**
     * Returns the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Pre-hashing: pack this coordinate into an int, so that the key space is as uniformly distributed among the range
     * of integers as possible.
     * <p>
     * Uses a weighted sum to pack the x- and y-coordinates into an int.
     */
    public int hashCode() {
        return x * (Integer.MAX_VALUE / Constants.MAX_BOARD_SIZE) + y;
    }

    /**
     * Returns true iff the (x,y)-coordinates of the given object match this Coord's (x,y)-coordinates.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Coord that) {
            return that.x == this.x && that.y == this.y;
        }
        return false;
    }

    /**
     * Returns this Coord as a string of the form (x, y).
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

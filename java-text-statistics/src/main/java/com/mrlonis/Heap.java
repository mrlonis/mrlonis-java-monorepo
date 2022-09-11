package com.mrlonis;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * The keys in the heap must be stored in an array.
 * 
 * There may be duplicate keys in the heap.
 * 
 * The constructor takes an argument that specifies how objects in the heap are
 * to be compared. This argument is a java.util.Comparator, which has a
 * compare() method that has the same signature and behavior as the compareTo()
 * method found in the Comparable interface.
 * 
 * Here are some examples of a Comparator<String>:
 * 
 * (s, t) -> s.compareTo(t); (s, t) -> t.length() - s.length();
 * 
 * (s, t) -> t.toLowerCase().compareTo(s.toLowerCase());
 * 
 * (s, t) -> s.length() <= 3 ? -1 : 1;
 * 
 * @author Matthew Lonis (mrlonis)
 */

public class Heap<E> implements PriorityQueue<E> {
	protected List<E> keys;
	private Comparator<E> comparator;

	/**
	 * Creates a heap whose elements are prioritized by the comparator.
	 * 
	 * @param comparator
	 *            The comparator for this Heap.
	 */
	public Heap(Comparator<E> comparator) {
		this.keys = new ArrayList<>();
		this.comparator = comparator;
	}

	/**
	 * Returns the comparator on which the keys in this heap are prioritized.
	 * 
	 * @return The comparator on which the keys in this heap are prioritized.
	 */
	public Comparator<E> comparator() {
		return comparator;
	}

	/**
	 * Returns the top of this heap. This will be the highest priority key.
	 * 
	 * @return The top element in the heap.
	 * @throws NoSuchElementException
	 *             if the heap is empty.
	 */
	public E peek() {
		return this.keys.get(0);
	}

	/**
	 * Inserts the given key into this heap. Uses siftUp().
	 * 
	 * @param key
	 *            The element to be inserted into this heap.
	 */
	public void insert(E key) {
		this.keys.add(key);
		siftUp(this.keys.size() - 1);
	}

	/**
	 * Removes and returns the highest priority key in this heap.
	 * 
	 * @return The element that was removed from this heap.
	 * @throws NoSuchElementException
	 *             if the heap is empty.
	 */
	public E delete() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		this.swap(0, this.size() - 1);
		E answer = this.keys.remove(this.size() - 1);
		siftDown(0);
		return answer;
	}

	/**
	 * Restores the heap property by sifting the key at position p down into the
	 * heap.
	 * 
	 * @param p
	 *            The starting position to start sifting down.
	 */
	public void siftDown(int p) {
		int leftChild = getLeft(p);
		int n = this.keys.size();

		if (leftChild < n) {
			int maxChild = leftChild;
			int rightChild = getRight(p);
			
			if (rightChild < n && this.comparator.compare(this.keys.get(rightChild), this.keys.get(maxChild)) < 0) {
				maxChild = rightChild;
			}
			
			if (this.comparator.compare(this.keys.get(maxChild), this.keys.get(p)) < 0) {
				swap(p, maxChild);
				siftDown(maxChild);
			}
		}
	}

	/**
	 * Restores the heap property by sifting the key at position q up into the
	 * heap. (Used by insert()).
	 * 
	 * @param q
	 *            The starting position to start sifting up.
	 */
	public void siftUp(int q) {
		int parent = getParent(q);

		while (parent >= 0 && q != parent) {
			if (this.comparator.compare(this.keys.get(q), this.keys.get(parent)) <= 0) {
				swap(q, parent);
				q = parent;
				parent = getParent(parent);
			} else {
				return;
			}
		}
	}

	/**
	 * Exchanges the elements in the heap at the given indices in keys.
	 * 
	 * @param i
	 *            The first element index to be swapped.
	 * @param j
	 *            The second element index to be swapped.
	 */
	public void swap(int i, int j) {
		E temp = this.keys.get(i);
		this.keys.set(i, this.keys.get(j));
		this.keys.set(j, temp);
	}

	/**
	 * Returns the number of keys in this heap.
	 * 
	 * @return The number of eys in this heap.
	 */
	public int size() {
		return keys.size();
	}

	/**
	 * Returns a textual representation of this heap.
	 * 
	 * @return A textual representation of this heap.
	 */
	public String toString() {
		return keys.toString();
	}

	/**
	 * Returns the index of the left child of p.
	 * 
	 * @param p
	 *            The index to find the left child of.
	 * @return The left child of p.
	 */
	public static int getLeft(int p) {
		return 2 * p + 1;
	}

	/**
	 * Returns the index of the right child of p.
	 * 
	 * @param p
	 *            The index to find the right child of.
	 * @return The right child of p.
	 */
	public static int getRight(int p) {
		return getLeft(p) + 1;
	}

	/**
	 * Returns the index of the parent of p.
	 * 
	 * @param p
	 *            The index to find the parent of.
	 * @return The parent of p.
	 */
	public static int getParent(int p) {
		return (p - 1) / 2;
	}
}
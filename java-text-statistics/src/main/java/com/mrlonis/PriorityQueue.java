package com.mrlonis;

import java.util.Comparator;

/**
 * This is the same ADT given in lecture except that it has been made generic
 * and the accessor method, comparator(), has been added.
 * 
 * Don't make any changes to this file.
 */

public interface PriorityQueue<E> {
	/**
	 * Inserts the given key into this priority queue.
	 */
	void insert(E key);

	/**
	 * Retrieves and removes the highest priority key in this queue, or returns
	 * null if this queue is empty.
	 */
	E delete();

	/**
	 * Retrieves, but does not remove, the head of this queue.
	 */
	E peek();

	/**
	 * Returns the comparator used to organize this queue.
	 */
	Comparator<E> comparator();

	/**
	 * Returns the number of keys in this queue.
	 */
	int size();

	/**
	 * Returns true iff this queue is empty.
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
}

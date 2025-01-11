package com.mrlonis;

import java.util.List;

/**
 * [read-only]
 *
 * <p>A Tree is searchable. The result of performing a search on a tree is a Location. (In the case of BinarySearchTree,
 * the Location will be a Node.) For convenience, we've modified the signature of insert() to return the location of the
 * key that was inserted. Additional operations include a clear() method and a keys() method.
 */
public interface Tree<K> {
    /** Inserts key into the tree and returns the location of the insert. */
    Location<K> insert(K key);

    /** Returns true iff key is in the tree. */
    boolean contains(K key);

    /** Removes the key from the tree. */
    void remove(K key);

    /** Returns the height of this tree, where height is the number of nodes on the longest root to leaf path. */
    int height();

    /** Clears all the keys from this tree. */
    void clear();

    /** Returns a list of the keys in this tree. */
    List<K> keys();

    /** Returns the number of keys in this tree. */
    int size();

    /** Returns true iff this tree is empty. */
    default boolean isEmpty() {
        return size() == 0;
    }

    /** Searches the tree for the key and, if found, returns a "pointer" to where it is. Otherwise, null is returned. */
    Location<K> search(K key);
}

/** A Location represents an "index" or a "pointer" into a data structure. */
interface Location<K> {
    /** Returns the location in the structure of the key preceding the one at this location. */
    Location<K> getBefore();

    /** Returns the data at this location. */
    K get();

    /** Returns the location in the structure of the key following the one at this location. */
    Location<K> getAfter();
}

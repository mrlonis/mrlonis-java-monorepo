package com.mrlonis.lab12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Starter code for lab12.
 *
 * <p>TODO: Complete the following three tasks in the order shown:
 *
 * <p>1. Implement the Node.splitChild() method.
 *
 * <p>2. Handle the root full case in BTree.insert().
 *
 * <p>3. Ignore attempt to insert a duplicate key.
 *
 * @author Matthew Lonis (mrlonis)
 * @author Alex Queen
 */
class BTree {

    /** A structure to pinpoint the location of a key in the tree. Used as the return type of the search() method. */
    class Location {
        Node p;
        int i;

        Location(Node p, int i) {
            this.p = p;
            this.i = i;
        }

        int get() {
            return p.keys.get(i);
        }
    }

    /** A node has up to b keys and b + 1 subtrees. */
    class Node {
        List<Integer> keys = new ArrayList<>(b);
        List<Node> children = new ArrayList<>(b + 1); // null pointers are not
        // stored

        /** Returns true iff this node is a leaf (i.e., it is childless). */
        boolean isLeaf() {
            return children.isEmpty();
        }

        /** Returns true iff the bucket for the keys is full. */
        boolean isFull() {
            return keys.size() == b;
        }

        /** Returns a list of the keys in the tree rooted at this Node as encountered during an inorder traversal. */
        List<Integer> inorder() {
            List<Integer> ans = new LinkedList<>();
            if (isLeaf()) ans.addAll(keys);
            else {
                int n = keys.size();
                for (int i = 0; i < n; i++) {
                    ans.addAll(children.get(i).inorder());
                    ans.add(keys.get(i));
                }
                ans.addAll(children.get(n).inorder());
            }
            return ans;
        }

        /**
         * Returns the index of key in keys, if it exists. Otherwise,the index of where key can be inserted into keys to
         * preserve the sorted order is returned. Note: the return value could be keys.size(), so need to check for that
         * before using the index on a get(). This is a helper for Node.insert() and Node.search().
         */
        int binarySearch(int key) {
            int low = 0, high = keys.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (key == keys.get(mid)) return mid;
                if (key < keys.get(mid)) high = mid - 1;
                else low = mid + 1;
            }
            return low;
        }

        /**
         * Returns the location of the given key in the tree rooted at this node. If the key is not found, then null is
         * returned. This is a helper for BTree.search().
         */
        public Location search(int key) {
            int i = binarySearch(key);
            if (i != keys.size() && key == keys.get(i)) return new Location(this, i);
            if (isLeaf()) return null;
            return children.get(i).search(key);
        }

        /**
         * Pre-conditions: This node is not full and the i-th child of this node is full. Consequently, this node is not
         * a leaf.
         *
         * <p>The middle key from the i-th child is propagated up into this node and the remaining keys in the i-th
         * child are split as evenly as possible amongst left and right siblings. In the case that b is even, there are
         * two middle keys and we choose the one on the left; consequently the right sibling will have one more key than
         * the left.
         *
         * <p>Post-conditions: This node will have one more key (and one more child). The i-th child of this node will
         * be half full, as will its right sibling.
         */
        void splitChild(int i) {
            // Verify the pre-conditions:
            assert !isFull();
            assert !isLeaf();
            assert children.get(i).isFull();
            int m = keys.size();
            assert m < b;

            /**
             * TODO #1
             *
             * <p>Split the i-th child of this node into two sibling nodes. Propagate the middle key into this node,
             * along with a pointer to the right sibling.
             */
            Node temp = this.children.get(i);

            int indexData = (b - 1) / 2;

            int data = temp.keys.get(indexData);

            Node left = new Node();
            left.children = new ArrayList<>(indexData);
            for (int j = 0; j <= indexData; j++) {
                if (j < indexData) {
                    left.keys.add(j, temp.keys.get(j));
                }

                if (!temp.children.isEmpty()) {
                    left.children.add(j, temp.children.get(j));
                }
            }

            Node right = new Node();
            int size = temp.keys.size();
            for (int j = indexData + 1, x = 0; j <= size; j++, x++) {
                if (j < size) {
                    right.keys.add(x, temp.keys.get(j));
                }

                if (!temp.children.isEmpty()) {
                    right.children.add(x, temp.children.get(j));
                }
            }

            this.keys.add(i, data);
            this.children.remove(i);
            this.children.add(i, left);
            this.children.add(i + 1, right);

            // Verify the post-conditions:
            assert keys.size() == m + 1;
            assert children.size() == m + 2;
            assert !children.get(i).isFull();
            assert !children.get(i + 1).isFull();
            assert children.get(i).keys.size() + children.get(i + 1).keys.size() + 1 == b;
            assert children.get(i).keys.size() - children.get(i + 1).keys.size() + 1 == b % 2;
        }

        /**
         * Precondition: This node is not full.
         *
         * <p>Inserts the key into the tree rooted at this node. If this key already exists in the tree, then nothing is
         * done.
         *
         * <p>This is a helper for BTree.search().
         */
        void insert(int key) {
            assert !isFull();
            int i = binarySearch(key);

            /**
             * TODO #3
             *
             * <p>This code assumes that key does not already exist in the tree. Make the appropriate changes to ensure
             * that an attempt to insert a duplicate keys does nothing.
             */
            if (this.keys.contains(key)) {
                return;
            }

            if (isLeaf()) {
                /*
                 * Can just add the key to the leaf because we know there's
                 * room.
                 */
                if (!keys.contains(key)) {
                    n++;
                    keys.add(i, key);
                }
            } else {
                /*
                 * The key belongs in the i-th child of this node.
                 */
                if (children.get(i).isFull()) {
                    splitChild(i);

                    /*
                     * There is now room in both the i-th child and the (i+1)-st
                     * child. Need to determine which of the two new children to
                     * insert the key into.
                     */
                    if (key > keys.get(i)) i++;
                }
                /*
                 * There's guaranteed to be room in this node, so go ahead and insert.
                 */
                if (!children.get(i).keys.contains(key) && !this.keys.contains(key)) {
                    children.get(i).insert(key);
                }
            }
        }

        public String toString() {
            return String.format("(%s, %s)", keys, children);
        }
    }

    Node root = new Node(); // root is never null
    int b; // bucket size
    int n; // total number of keys in the tree

    BTree(int b) {
        assert b >= 1;
        this.b = b;
    }

    List<Integer> inorder() {
        return root.inorder();
    }

    Location search(int key) {
        return root.search(key);
    }

    /** Insert key into this tree. If the key already exists, then nothing is done. */
    void insert(int key) {
        if (root.isFull()) {
            /**
             * TODO #2
             *
             * <p>Split preemptively, so that we'll have room in the root if a child needs to split later. Do this by
             * creating a new root with no keys and the old root as its only child, and then calling splitChild() to
             * split the full child.
             */
            Node newRoot = new Node();
            newRoot.children.add(this.root);
            newRoot.splitChild(0);
            this.root = newRoot;

            // Post-conditions:
            assert root.keys.size() == 1;
            assert root.children.size() == 2;
        }

        assert !root.isFull();
        root.insert(key);
    }

    /** Returns the number of keys in this tree. */
    int size() {
        return n;
    }

    /** Returns true iff this tree is empty. */
    boolean isEmpty() {
        return size() == 0;
    }

    /** Clears all keys from this tree. */
    void clear() {
        n = 0;
        root = new Node();
    }

    /** Returns a textual representation of this tree. Defers to Node.toString(). */
    public String toString() {
        return root.toString();
    }

    public static void main(String[] args) {
        BTree bt;
        bt = new BTree(3);

        /** 8 / \ 1, 4, 5 10, 12 */
        bt.root.keys.add(8);
        bt.root.children.add(bt.new Node());
        bt.root.children.add(bt.new Node());
        bt.root.children.getFirst().keys.add(1);
        bt.root.children.get(0).keys.add(4);
        bt.root.children.get(0).keys.add(5);
        bt.root.children.get(1).keys.add(10);
        bt.root.children.get(1).keys.add(12);
        bt.n = 6;
        assert 6 == bt.size();
        System.out.println("bt = " + bt);

        // Test basic insert (no split occurs).
        bt.insert(18);
        assert 7 == bt.size();
        assert bt.search(18).get() == 18;
        System.out.println("bt = " + bt);

        // This will trigger a split. (TODO #1)
        bt.insert(3);
        System.out.println("bt = " + bt);

        /** 4, 8 / | \ 1, 3 5 10, 12, 18 */
        assert 8 == bt.size();
        assert 2 == bt.root.keys.size();
        assert 4 == bt.root.keys.get(0);
        assert 8 == bt.root.keys.get(1);
        assert 2 == bt.root.children.getFirst().keys.size();
        assert 1 == bt.root.children.get(0).keys.get(0);
        assert 3 == bt.root.children.get(0).keys.get(1);
        assert 1 == bt.root.children.get(1).keys.size();
        assert 5 == bt.root.children.get(1).keys.getFirst();
        assert 3 == bt.root.children.get(2).keys.size();
        assert 10 == bt.root.children.get(2).keys.get(0);
        assert 12 == bt.root.children.get(2).keys.get(1);
        assert 18 == bt.root.children.get(2).keys.get(2);

        // This will trigger a split. (TODO #1)
        bt.insert(9);
        System.out.println("bt = " + bt);

        /** 4, 8, 12 / / \ \ 1, 3 5 9, 10 18 */
        assert 9 == bt.size();
        assert 3 == bt.root.keys.size();
        assert 4 == bt.root.keys.get(0);
        assert 8 == bt.root.keys.get(1);
        assert 12 == bt.root.keys.get(2);
        assert 2 == bt.root.children.get(2).keys.size();
        assert 9 == bt.root.children.get(2).keys.get(0);
        assert 10 == bt.root.children.get(2).keys.get(1);
        assert 1 == bt.root.children.get(3).keys.size();
        assert 18 == bt.root.children.get(3).keys.getFirst();

        // This will trigger a split in the root. (TODO #2)
        bt.insert(7);
        System.out.println("bt = " + bt);

        /** 8 / \ 4 12 / \ / \ 1, 3 5, 7 9, 10 18 */
        assert 10 == bt.size();
        assert 1 == bt.root.keys.size();
        assert 8 == bt.root.keys.getFirst();
        assert 1 == bt.root.children.get(0).keys.size();
        assert 4 == bt.root.children.get(0).keys.getFirst();
        assert 1 == bt.root.children.get(1).keys.size();
        assert 12 == bt.root.children.get(1).keys.getFirst();
        assert 2 == bt.root.children.get(0).children.get(1).keys.size();
        assert 5 == bt.root.children.get(0).children.get(1).keys.get(0);
        assert 7 == bt.root.children.getFirst().children.get(1).keys.get(1);

        // Ignore duplicate keys. (TODO #3)

        bt.insert(8);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();
        bt.insert(4);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();
        bt.insert(3);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();
        bt.insert(12);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();
        bt.insert(10);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();
        bt.insert(18);
        System.out.println("bt = " + bt);
        assert 10 == bt.size();

        bt.insert(11);
        System.out.println("bt = " + bt);

        /** 8 / \ 4 12 / \ / \ 1, 3 5, 7 9, 10, 11 18 */
        assert 11 == bt.size();
        bt.insert(10);
        System.out.println("bt = " + bt);
        assert 11 == bt.size();

        /** 8 / \ 4 10, 12 / \ / | \ 1, 3 5, 7 9 11 18 */
        assert 2 == bt.root.children.get(1).keys.size();
        assert 10 == bt.root.children.get(1).keys.get(0);
        assert 12 == bt.root.children.get(1).keys.get(1);

        // Test with larger bucket sizes.

        bt = new BTree(4);
        int[] a = new int[] {1, 2, 3, 5, 6, 7, 9, 12, 16, 18, 21, 11, 4, 8, 10, 13, 14, 15, 17, 19, 20};
        for (int value : a) bt.insert(value);
        assert bt.size() == a.length;
        for (int k : a) bt.insert(k);
        assert bt.size() == a.length;

        assert 2 == bt.root.keys.size();
        assert 5 == bt.root.keys.get(0);
        assert 12 == bt.root.keys.get(1);

        bt = new BTree(7);
        for (int j : a) bt.insert(j);
        assert bt.size() == a.length;

        assert 3 == bt.root.keys.size();
        assert 5 == bt.root.keys.get(0);
        assert 12 == bt.root.keys.get(1);
        assert 16 == bt.root.keys.get(2);

        // Make a large sequence of unique ints.
        Random gen = new Random();
        Set<Integer> xs = new HashSet<>();
        for (int i = 0; i < 1000; i++) xs.add(gen.nextInt(1000));

        bt.clear();
        for (int x : xs) bt.insert(x);
        assert bt.size() == xs.size();
        for (int x : xs) bt.insert(x);
        assert bt.size() == xs.size();

        for (int x : xs) {
            Location loc = bt.search(x);
            assert loc != null;
            assert loc.get() == x;
        }

        System.out.println("Passed all tests...");
    }
}

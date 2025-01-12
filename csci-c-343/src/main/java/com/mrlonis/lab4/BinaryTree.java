package com.mrlonis.lab4;

/**
 * Tree interface and BinaryTree class from lec4b.
 *
 * @author Matthew Lonis
 */
public class BinaryTree implements Tree {

    /**
     * Node class for BinaryTree.
     *
     * @author Matthew Lonis
     */
    class Node {
        /** The data field for a Node. */
        int data;

        /**
         * This contains the Node pointers that will be the branches of a Node. Contains both a Left and Right possible
         * branch.
         */
        Node left, right;

        /**
         * Node constructor that takes only a data field. Essentially makes a leaf.
         *
         * @param key The data to be added to the new Node.
         */
        Node(int key) {
            this(key, null, null);
        }

        /**
         * Creates a Node with specified data, left and right fields.
         *
         * @param data The data for the Node being created.
         * @param left The pointer to the left Node relative to this Node.
         * @param right The pointer to the right Node relative to this Node.
         */
        Node(int data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        /**
         * Returns true iff this Node has null for its left and right fields.
         *
         * @return true iff this Node has null for its left and right fields.
         */
        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    /** The root Node for my BinaryTree. */
    Node root;

    /** This holds the number of Nodes in the tree. Represents its size. */
    int n;

    /**
     * This inserts the key into a spot in the BinaryTree. If the root Node is null, it creates a new Node at the root.
     * If the root.left Node is null it creates a new node there. If the root.right Node is null it creates a new Node
     * there. Otherwise if none of those conditions work out, there's a 50% chance it makes a new Node as the root with
     * the old root being on its left and a 50% chance of the old root being on the right.
     *
     * @param key The data for the Node that is to be inserted.
     */
    public void insert(int key) {
        n++;
        if (root == null) root = new Node(key);
        else if (root.left == null) root.left = new Node(key);
        else if (root.right == null) root.right = new Node(key);
        else if (Math.random() < 0.5) root = new Node(key, root, null);
        else root = new Node(key, null, root);
    }

    /**
     * This returns true iff the BinaryTree contains the key.
     *
     * @param key The data we want to know if it exists in the tree.
     * @return true iff the BinaryTree contains the key.
     */
    public boolean contains(int key) {
        return containsHelper(key, root);
    }

    /**
     * This returns true iff the BinaryTree contains the key in Node p.
     *
     * @param key The data we want to know if it exists in the tree.
     * @param p The Node we are searching in.
     * @return true iff the BinaryTree contains the key in the tree.
     */
    private boolean containsHelper(int key, Node p) {
        if (p == null) return false;
        if (p.data == key) return true;
        return containsHelper(key, p.left) || containsHelper(key, p.right);
    }

    /**
     * Returns the size of the BinaryTree.
     *
     * @return n (the Size of the tree).
     */
    public int size() {
        return n;
    }

    /**
     * Returns the length of the longest root->leaf pathway.
     *
     * @return The length of the longest root->leaf pathway.
     */
    public int height() {
        Node p = this.root;
        return findHeight(p);
    }

    /**
     * Find the length of the longest root->leaf pathway. Uses recursion to check all branches.
     *
     * @param node The node the function is currently at.
     * @return The length of the longest root->leaf pathway.
     */
    public int findHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(findHeight(node.left), findHeight(node.right));
    }

    /**
     * Simple Testing.
     *
     * @param args Unused.
     */
    public static void main(String... args) {
        System.out.println("Beginning Tests...");
        int[] a = new int[] {3, 9, 7, 2, 1, 5, 6, 4, 8};
        BinaryTree tr = new BinaryTree();
        assert tr.isEmpty();
        for (int key : a) {
            tr.insert(key);
        }
        assert tr.size() == a.length;
        assert tr.height() == 5;
        assert !tr.root.isLeaf();
        for (int key : a) {
            assert tr.contains(key);
        }
        try {
            tr.remove(3);
        } catch (UnsupportedOperationException e) {
        }

        a = new int[] {1};
        tr = new BinaryTree();
        assert tr.isEmpty();
        for (int key : a) {
            tr.insert(key);
        }
        assert tr.size() == a.length;
        assert tr.height() == 1;
        assert tr.root.isLeaf();
        for (int key : a) {
            assert tr.contains(key);
        }
        try {
            tr.remove(3);
        } catch (UnsupportedOperationException e) {
        }

        a = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        tr = new BinaryTree();
        assert tr.isEmpty();
        for (int key : a) {
            tr.insert(key);
        }
        assert tr.size() == a.length;
        assert tr.height() == 8;
        assert !tr.root.isLeaf();
        for (int key : a) {
            assert tr.contains(key);
        }
        try {
            tr.remove(3);
        } catch (UnsupportedOperationException e) {
        }
        System.out.println("Passed all tests...");
    }
}

/**
 * Interface for a Tree.
 *
 * @author Matthew Lonis
 */
interface Tree {
    void insert(int key);

    default void remove(int key) {
        throw new UnsupportedOperationException();
    }

    boolean contains(int key);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}

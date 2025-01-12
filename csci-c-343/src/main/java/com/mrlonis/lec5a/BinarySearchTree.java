package com.mrlonis.lec5a;

/**
 * Tree interface and BinarySearchTree class from lec4b. ^^^^^^
 *
 * <p>TODO: Pre-Lecture Exercise for lec5a. Note: This is a team effort. Every member of your team is expected to make
 * non-trivial contributions towards your solution.
 *
 * <p>Make the following modifications:
 *
 * <p>(1) Add javadoc style comments to all methods. (2) Implement BinarySearchTree.contains() so that it runs in O(h)
 * time, where h is the height of the tree. (3) Modify Tree and BinarySearchTree so that they are generic for any
 * Comparable type object. (4) [challenge] Try to implement a sensible BinarySearchTree.toString() method. Recall that
 * an in order traversal of a BST yields a sorted sequence. (5) Test thoroughly in main(). Be sure to include tests on
 * non-integer data.
 *
 * @author green1
 * @author Dylan Clements
 */
public class BinarySearchTree<K extends Comparable<K>> implements Tree<K> {

    /** Node for BST Class. */
    class Node {
        /** Holds the data for a Node. */
        K data;

        /** Points to the left or right branch in the BST Structure. */
        Node left, right;

        /**
         * Initializes a Node with the given key in the data field.
         *
         * @param key The value to be put into the data field for the Node being created.
         */
        Node(K key) {
            this(key, null, null);
        }

        /**
         * Creates a Node with given data, left and right fields.
         *
         * @param data The data to be placed in the Node being created.
         * @param left The left Node pointer for this Node.
         * @param right The right Node pointer for this Node.
         */
        Node(K data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        /**
         * Returns true iff the left and right fields for a Node are null.
         *
         * @return <code>true</code> iff the left and right fields for a Node are <code>null</code>.
         */
        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    /** The Root Node for a BST Structure. */
    Node root;

    /** Represents the size of the BST. */
    int n;

    /**
     * Inserts the key in the correct spot in the BST.
     *
     * @param key The data for the Node that is to be placed in the BST.
     */
    public void insert(K key) {
        n++;
        root = insertHelper(key, root);
    }

    /**
     * Searches for the correct spot to place a new Node with the data key. Updates every pointer in the BST, but the
     * only noticable change will be that a new Node is inserted.
     *
     * @param key The data for the Node to be inserted into the BST.
     * @param p The pointer to the current Node. This is the Node that will have either its left or right pointers
     *     updated.
     * @return A pointer to a Node in the BST.
     */
    private Node insertHelper(K key, Node p) {
        if (p == null) return new Node(key);
        if (key.compareTo(p.data) < 0) p.left = insertHelper(key, p.left);
        else p.right = insertHelper(key, p.right);
        return p;
    }

    /**
     * Returns true iff the BST contains a Node with key as its data member.
     *
     * @return <code>true</code> iff the BST contains a Node with <code>key</code> as its data member.
     */
    public boolean contains(K key) {
        Node p = root;
        return containsHelper(key, p);
    }

    /**
     * Returns true iff the Node contains key.
     *
     * @param key The data to check each Node for.
     * @param p Pointer to the Node this method is checking in.
     * @return true iff the Node contains key, false otherwise.
     */
    public boolean containsHelper(K key, Node p) {
        if (p == null) {
            return false;
        } else if (p.data.equals(key)) {
            return true;
        } else if (key.compareTo(p.data) < 0) {
            return containsHelper(key, p.left);
        } else if (key.compareTo(p.data) > 0) {
            return containsHelper(key, p.right);
        }

        return false;
    }

    /**
     * Returns the n data member of this BST.
     *
     * @return The n data member of this BST.
     */
    public int size() {
        return n;
    }

    /**
     * Returns a string representation of a BST.
     *
     * @return A string representation of a BST.
     */
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("ROOT: ");
        toStringHelper(string, root);
        return string.toString();
    }

    /**
     * Returns a Partial string representation of a BST.
     *
     * @param string The StringBuilder receieved from toString to continue building onto.
     * @param node The cirrent Node in the BST.
     * @return The StringBuilder to be finished in toString.
     */
    private StringBuilder toStringHelper(StringBuilder string, Node node) {
        string.append('{');
        if (node != null) {
            string.append(node.data);
            toStringHelper(string.append(", LEFT: "), node.left);
            toStringHelper(string.append(", RIGHT: "), node.right);
        }
        return string.append('}');
    }

    /**
     * Testing on Integers, Doubles, and Strings.
     *
     * @param args
     */
    public static void main(String... args) {
        int[] a = new int[] {3, 9, 7, 2, 1, 5, 6, 4, 8};
        Tree<Integer> bst = new BinarySearchTree<>();
        assert bst.isEmpty();
        for (int key : a) {
            bst.insert(key);
        }
        /** 3 / \ 2 9 / / 1 7 / \ 5 8 / \ 4 6 */
        assert !bst.isEmpty();
        assert bst.size() == a.length;
        for (int key : a) {
            assert bst.contains(key);
        }
        System.out.println(bst.toString());

        String[] b = new String[] {"Matthew", "Nova", "Chris", "Dylan", "Clements", "Audretsch", "Richardson", "Lonis"};
        Tree<String> bst2 = new BinarySearchTree<>();
        assert bst2.isEmpty();
        for (String key : b) {
            bst2.insert(key);
        }
        assert !bst2.isEmpty();
        assert bst2.size() == b.length;
        for (String key : b) {
            assert bst2.contains(key);
        }
        System.out.println(bst2.toString());

        double[] c = new double[] {3, 9, 7, 2, 1, 5, 6, 4, 8};
        Tree<Double> bst3 = new BinarySearchTree<>();
        assert bst3.isEmpty();
        for (double key : c) {
            bst3.insert(key);
        }
        assert !bst3.isEmpty();
        assert bst3.size() == c.length;
        for (double key : c) {
            assert bst3.contains(key);
        }
        System.out.println(bst3.toString());
    }
}

/**
 * The interface for any Tree.
 *
 * @param <K> A Comparable Data type.
 */
interface Tree<K extends Comparable<K>> {
    /**
     * The data member for the Node to be inserted into the Tree.
     *
     * @param key The data member for the Node to be inserted into the Tree.
     */
    void insert(K key);

    /**
     * Default unused method.
     *
     * @param key
     */
    default void remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true iff the Tree contains a Node with key as its data member.
     *
     * @param key The item to search for in a Tree.
     * @return true iff the Tree contains a Node with key as its data member.
     */
    boolean contains(K key);

    /**
     * Returns the size of the Tree.
     *
     * @return The size of the Tree.
     */
    int size();

    /**
     * Default code for a Tree.
     *
     * @return true iff the tree's size isn't 0.
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}

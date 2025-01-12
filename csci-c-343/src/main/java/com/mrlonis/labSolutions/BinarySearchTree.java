package com.mrlonis.labSolutions;

/** Solutions for lab5. */
public class BinarySearchTree implements Tree {

    class Node {
        int data;
        Node left, right;

        Node(int key) {
            this(key, null, null);
        }

        Node(int data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return left == null && right == null;
        }
    }

    Node root;
    int n;

    /** Removes the key from this tree. Must run in O(h) time, where h is the height of the tree. */
    public void remove(int key) {
        root = removeHelper(key, root);
    }

    private Node removeHelper(int key, Node p) {
        if (p == null) {
            return p;
        }
        if (key == p.data) {
            // Check for "easy delete" case.
            if (p.left == null) {
                n--;
                return p.right;
            } else if (p.right == null) {
                n--;
                return p.left;
            }
            // Handle the replacement case.
            p.data = minValue(p.right);
            // At this point, p.data is in a leaf of p.right.
            p.right = removeHelper(p.data, p.right);
        } else if (key < p.data) {
            p.left = removeHelper(key, p.left);
        } else {
            p.right = removeHelper(key, p.right);
        }
        return p;
    }

    /** Returns the minimal key in the (non-empty) tree rooted at p. */
    private int minValue(Node p) {
        assert p != null;
        int minval = p.data;
        while (p.left != null) {
            minval = p.left.data;
            p = p.left;
        }
        return minval;
    }

    /** Inserts the key into this tree. Runs in O(h) time, where h is the height of the tree. */
    public void insert(int key) {
        n++;
        root = insertHelper(key, root);
    }

    private Node insertHelper(int key, Node p) {
        if (p == null) {
            p = new Node(key);
        } else if (key < p.data) {
            p.left = insertHelper(key, p.left);
        } else {
            // if keys are unique, it must be the case that key > p.data
            p.right = insertHelper(key, p.right);
        }
        return p;
    }

    /** Returns true iff key is in this tree. Runs in O(h) time, where h is the height of the tree. */
    public boolean contains(int key) {
        return containsHelper(key, root);
    }

    private boolean containsHelper(int key, Node p) {
        if (p == null) {
            return false;
        } else if (key == p.data) {
            return true;
        } else if (key < p.data) {
            return containsHelper(key, p.left);
        }
        return containsHelper(key, p.right);
    }

    /** Returns the number of keys in this tree. */
    public int size() {
        return n;
    }

    /** Testing. */
    public static void main(String... args) {
        int[] a = new int[] {3, 9, 7, 2, 1, 5, 6, 4, 8};
        int[] b = new int[] {1, 6, 4, 5, 8, 9, 7, 2};
        Tree bst = new BinarySearchTree();
        assert bst.isEmpty();
        for (int key : a) {
            bst.insert(key);
        }
        assert bst.size() == a.length;
        for (int key : a) {
            assert bst.contains(key);
        }
        bst.remove(3);
        for (int key : b) {
            assert bst.contains(key);
        }
        assert !bst.contains(3);
        int n = bst.size();
        for (int key : b) {
            assert bst.contains(key);
            bst.remove(key);
            assert !bst.contains(key);
            n--;
            assert n == bst.size();
        }
        assert bst.isEmpty();
        System.out.println("Passed all the basic tests...");

        /**
         * TODO: As a challenge, arrange things so that attempts to remove key that are not in the tree are simply
         * ignored (and do no harm).
         */
        for (int key : a) {
            bst.insert(key);
        }
        n = bst.size();
        for (int key : a) {
            bst.remove(-key);
            assert n == bst.size();
        }
        System.out.println("Passed challenge tests...");
    }
}

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

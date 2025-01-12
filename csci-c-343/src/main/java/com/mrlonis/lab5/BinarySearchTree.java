package com.mrlonis.lab5;

/**
 * Starter code for lab5. This is an implementation of BinarySearchTree for int data.
 *
 * <p>Implement the remove() method using the algorithm described by your AI.
 *
 * @author Matthew Lonis
 */
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

    /**
     * TODO
     *
     * <p>Removes the key from this tree. Must run in O(h) time, where h is the height of the tree.
     */
    public void remove(int key) {
        if (root == null) {
            return;
        } else if (root.data == key && root.isLeaf()) {
            n--;
            root = null;
            return;
        }

        deleteNode(root, key);
        n--;
    }

    public Node deleteNode(Node root, int value) {
        if (root == null) return null;
        if (root.data > value) {
            root.left = deleteNode(root.left, value);
        } else if (root.data < value) {
            root.right = deleteNode(root.right, value);

        } else {
            if (root.left != null && root.right != null) {
                Node temp = root;
                Node minNodeForRight = minimumElement(temp.right);
                root.data = minNodeForRight.data;
                deleteNode(root.right, minNodeForRight.data);

            } else if (root.left != null) {
                root = root.left;
            } else if (root.right != null) {
                root = root.right;
            } else root = null;
        }
        return root;
    }

    public static Node minimumElement(Node root) {
        if (root.left == null) return root;
        else {
            return minimumElement(root.left);
        }
    }

    public void removeHelper(int key, Node p, Node parent) {
        if (key < p.data) {
            if (p.left != null) {
                removeHelper(key, p.left, p);
            } else {
                return;
            }
        } else if (key > p.data) {
            if (p.right != null) {
                removeHelper(key, p.right, p);
            } else {
                return;
            }
        } else {
            if (p.left != null && p.right != null) {
                p.data = minValue(p.right);
                removeHelper(p.data, p.right, p);
            } else if (p.left != null) {
                if (parent.left == p) {
                    parent.left = p.left;
                } else if (parent.right == p) {
                    parent.right = p.left;
                }
            } else if (p.right != null) {
                if (parent.left == p) {
                    parent.left = p.right;
                } else if (parent.right == p) {
                    parent.right = p.right;
                }
            } else if (p.isLeaf()) {
                if (parent.left == p) {
                    parent.left = null;
                } else if (parent.right == p) {
                    parent.right = null;
                }
            }
        }
    }

    public int minValue(Node p) {
        if (p.left == null) {
            return p.data;
        } else {
            return minValue(p.left);
        }
    }

    public int maxValue(Node p) {
        if (p.right == null) {
            return p.data;
        } else {
            return maxValue(p.right);
        }
    }

    /** Inserts the key into this tree. Runs in O(h) time, where h is the height of the tree. */
    public void insert(int key) {
        n++;
        root = insertHelper(key, root);
    }

    private Node insertHelper(int key, Node p) {
        if (p == null) p = new Node(key);
        else if (key < p.data) p.left = insertHelper(key, p.left);
        else
            // if keys are unique, it must be the case that key > p.data
            p.right = insertHelper(key, p.right);
        return p;
    }

    /** Returns true iff key is in this tree. Runs in O(h) time, where h is the height of the tree. */
    public boolean contains(int key) {
        return containsHelper(key, root);
    }

    private boolean containsHelper(int key, Node p) {
        if (p == null) return false;
        if (key == p.data) return true;
        if (key < p.data) return containsHelper(key, p.left);
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
        for (int key : a) bst.insert(key);
        assert bst.size() == a.length;
        for (int key : a) assert bst.contains(key);
        bst.remove(3);
        for (int key : b) assert bst.contains(key);
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
        for (int key : a) bst.insert(key);
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

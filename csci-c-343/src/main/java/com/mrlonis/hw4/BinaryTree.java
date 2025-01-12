package com.mrlonis.hw4;

import java.util.LinkedList;
import java.util.List;

/**
 * Tree interface and BinaryTree class from lec4b.
 *
 * @author mrlonis
 */
public class BinaryTree implements Tree {

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

    public void insert(int key) {
        n++;
        if (root == null) {
            root = new Node(key);
            lst.add(key);
        } else if (root.left == null) {
            root.left = new Node(key);
            lst.add(1, key);
        } else if (root.right == null) {
            root.right = new Node(key);
            lst.add(1, key);
        } else if (key % 2 == 0 /*Math.random() < 0.5*/) {
            root = new Node(key, root, null);
            lst.add(0, key);
        } else {
            root = new Node(key, null, root);
            lst.add(0, key);
        }
    }

    public boolean contains(int key) {
        return containsHelper(key, root);
    }

    private boolean containsHelper(int key, Node p) {
        if (p == null) return false;
        if (p.data == key) return true;
        return containsHelper(key, p.left) || containsHelper(key, p.right);
    }

    public int size() {
        return n;
    }

    /**
     * Page 164. Problem 4.33. Use the simple implementation of BinaryTree.java from lab4. Add a new method named
     * pruneLeaves(). To aid in testing, change the condition involving Math.random() in the insert() method to one that
     * checks if the key is even. Test thoroughly.
     *
     * <p>Write a recursive method that takes a reference to the root node of a tree T and returns a reference to the
     * root node of the tree that results from removing all leaves from T.
     */
    public Node pruneLeaves(Node p) {
        if (p.isLeaf()) {
            n--;
            lst.remove((Integer) p.data);
            return null;
        }

        Node curr = p;

        if (curr.left != null) {
            if (curr.left.isLeaf()) {
                n--;
                lst.remove((Integer) curr.left.data);
                curr.left = null;
            } else {
                pruneLeaves(curr.left);
            }
        }

        if (curr.right != null) {
            if (curr.right.isLeaf()) {
                n--;
                lst.remove((Integer) curr.right.data);
                curr.right = null;
            } else {
                pruneLeaves(curr.right);
            }
        }

        return p;
    }

    /** Data structure to help control the traversal. To be used in the inOrder() method. */
    List<Integer> lst = new LinkedList<>();

    /**
     * Page 165. Problem 4.41. Assume that what Weiss means by "list out" is that the method returns a
     * java.util.List<Integer> containing the data from the tree in level order. Working with your class from Problem 5,
     * add a new method named levelOrder(). You will need to maintain a data structure to help you control the
     * traversal. You should use something from java.util, but make a good choice that is well-suited to the task at
     * hand. In the comment for levelOrder(), write a brief statement justifying your decision. Test thoroughly.
     *
     * <p>Write a routine to list out the nodes of a binary tree in level-order. List the root, then nodes at depth 1,
     * followed by nodes at depth 2, and so on. You must do this in linear time. Prove your time bound.
     *
     * <p>I chose LinkedList because it is fast to add and remove and can be moved through quickly using an iterator.
     */
    public List<Integer> inOrder() {
        return lst;
    }

    public static void main(String... args) {
        int[] a = new int[] {3, 9, 7, 2, 1, 5, 6, 4, 8};
        BinaryTree tr = new BinaryTree();
        assert tr.isEmpty();
        for (int key : a) {
            tr.insert(key);
        }
        assert tr.size() == a.length;
        assert !tr.root.isLeaf();
        for (int key : a) {
            assert tr.contains(key);
        }
        try {
            tr.remove(3);
        } catch (UnsupportedOperationException e) {

        }
        /** Added Tests. */
        assert "[4, 8, 5, 6, 2, 1, 3, 7, 9]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 4;
        assert "[4, 5, 2, 3]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 3;
        assert "[4, 5, 2]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 2;
        assert "[4, 5]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 1;
        assert "[4]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.lst.isEmpty();
        assert tr.isEmpty();

        a = new int[] {5, 4, 6, 3, 7, 2, 8, 1, 9};
        for (int key : a) {
            tr.insert(key);
        }
        assert tr.size() == a.length;
        assert !tr.root.isLeaf();
        assert "[1, 9, 2, 8, 3, 7, 5, 6, 4]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 4;
        assert "[1, 2, 3, 5]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 3;
        assert "[1, 2, 3]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 2;
        assert "[1, 2]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.size() == 1;
        assert "[1]".equals(tr.inOrder().toString());
        tr.root = tr.pruneLeaves(tr.root);
        assert tr.lst.isEmpty();
        assert tr.isEmpty();

        System.out.println("Passed all tests...");
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

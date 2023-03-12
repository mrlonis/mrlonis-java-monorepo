package com.mrlonis;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * This class implements a generic unbalanced binary search tree (BST).
 *
 * @author mrlonis (Matthew Lonis)
 */

public class BinarySearchTree<K> implements Tree<K> {

    protected Node root;
    protected int n;
    protected BiPredicate<K, K> lessThan;
    /**
     * Constructs an empty BST, where the data is to be organized according to the lessThan relation.
     */
    public BinarySearchTree(BiPredicate<K, K> lessThan) {
        this.lessThan = lessThan;
    }

    /**
     * Looks up the key in this tree and, if found, returns the (possibly dirty) location containing the key.
     *
     * @param key
     *         The data for the node to be searched for in this tree.
     *
     * @return The Node that is found by searching this tree.
     */
    public Node search(K key) {
        Node curr = root;

        while (curr != null) {
            if (curr.get().equals(key)) {
                return curr;
            } else if (lessThan.test(key, curr.get())) {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
        }

        return null;
    }

    /**
     * Returns the height of this tree. Runs in O(1) time!
     *
     * @return The height of this tree.
     */
    public int height() {
        if (root == null) {
            return 0;
        }
        return root.height;
    }

    /**
     * Clears all the keys from this tree. Runs in O(1) time!
     */
    public void clear() {
        n = 0;
        root = null;
    }

    /**
     * Returns the number of keys in this tree.
     *
     * @return The number of keys in this tree.
     */
    public int size() {
        return n;
    }

    /**
     * Inserts the given key into this BST, as a leaf, where the path to the leaf is determined by the predicate
     * provided to the tree at construction time. The parent pointer of the new node and the heights in all node along
     * the path to the root are adjusted accordingly.
     * <p>
     * Note: we assume that all keys are unique. Thus, if the given key is already present in the tree, nothing
     * happens.
     * <p>
     * Returns the location where the insert occurred (i.e., the leaf node containing the key).
     *
     * @param key
     *         The data for the node to be inserted into this tree.
     *
     * @return The location of the newly inserted Node.
     */
    public Node insert(K key) {
        if (root == null) {
            root = new Node(key);
            n++;
            return root;
        } else {
            Node search = search(key);
            if (search != null) {
                if (!search.dirty) {
                    return search;
                }
                search.dirty = false;
                n++;
                return search;
            }
        }

        Node curr = root;

        while (!curr.isLeaf()) {
            if (lessThan.test(key, curr.get())) {
                if (curr.left == null) {
                    break;
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    break;
                }
                curr = curr.right;
            }
        }

        if (lessThan.test(key, curr.get())) {
            curr.left = new Node(key);
            n++;
            curr.left.after = curr;
            curr.left.before = curr.before;
            if (curr.before != null) {
                curr.before.after = curr.left;
            }
            curr.before = curr.left;
            curr.left.parent = curr;

            Node p = curr;

            while (p != root) {
                p.fixHeight();
                p = p.parent;
            }
            root.fixHeight();

            return curr.left;
        } else {
            curr.right = new Node(key);
            n++;
            curr.right.after = curr.after;
            if (curr.after != null) {
                curr.after.before = curr.right;
            }
            curr.right.before = curr;
            curr.after = curr.right;
            curr.right.parent = curr;

            Node p = curr;

            while (p != root) {
                p.fixHeight();
                p = p.parent;
            }
            root.fixHeight();

            return curr.right;
        }
    }

    /**
     * Returns true iff the given key is in this BST.
     *
     * @param key
     *         The key to search for in this BST.
     *
     * @return <code>true</code> if it exists in this tree and
     * <code>false</code> otherwise.
     */
    public boolean contains(K key) {
        Node p = search(key);
        return p != null && !p.dirty;
    }

    /**
     * Removes the key from this BST. If the key is not in the tree, nothing happens. Implement the removal using lazy
     * deletion.
     *
     * @param key
     *         The key to be removed from this tree if it exists in this tree.
     */
    public void remove(K key) {
        if (root != null) {
            Node search = search(key);
            if (search != null) {
                if (!search.dirty) {
                    search.dirty = true;
                    n--;
                }
            }
        }
    }

    /**
     * Clears out all dirty nodes from this BST.
     */
    public void rebuild() {
        List<K> keys = this.keys();
        this.clear();
        for (K key : keys) {
            this.insert(key);
        }
    }

    /**
     * Returns a sorted list of all the keys in this tree.
     *
     * @return The sorted list of all the keys in this tree.
     */
    public List<K> keys() {
        Node curr = root;
        List<K> keys = new LinkedList<>();

        if (this.isEmpty()) {
            return keys;
        }

        while (curr.getBefore() != null) {
            curr = curr.getBefore();
        }

        while (curr != null) {
            if (!curr.dirty) {
                keys.add(curr.get());
            }
            curr = curr.getAfter();
        }

        return keys;
    }

    /**
     * Returns a textual representation of this BST.
     *
     * @return A textual Representation of this BST.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Start of Tree...\n");
        if (root != null) {
            str.append("ROOT: {").append(root.data.toString()).append("}");
            str.append("\nROOT LEFT: ");
            toStringHelper(root.left, str, 1);
            str.append("\nROOT RIGHT: ");
            toStringHelper(root.right, str, 1);
        }
        str.append("\n...End of Tree!");
        return str.toString();
    }

    /**
     * Helper function for my toStrig() method.
     *
     * @param p
     *         The current node in this tree to print out and reference from.
     * @param str
     *         The current state of the StringBuilder
     * @param level
     *         The level in the tree the method is currently at. Used to identify how deep the Node is in the tree.
     *         Level = to 1 is the Nodes Right below the root, level 2 is the Nodes below level 1, etc.
     */
    public void toStringHelper(Node p, StringBuilder str, int level) {
        if (p != null) {
            str.append("Node (h=").append(level).append(") Data: {").append(p.data.toString());
            if (p.left != null) {
                str.append(" LEFT: ");
                toStringHelper(p.left, str, level + 1);
            }

            if (p.right != null) {
                str.append(" RIGHT: ");
                toStringHelper(p.right, str, level + 1);
            }
            str.append("}");
        }
    }

    /**
     * Testing Method
     */
    public void printAll() {
        System.out.print("[");
        printAllHelper(root);
        System.out.println("]");
    }
	
	/*
	 * These methods are an adapted version from stackOverflow.com.
	 * 
	 * I used this to visualize my tree during testing.
	 * 
	 * I did not use this to plagiarize, simply used it as a visual aid. As
	 * you can see above I have implemented my own toString() methods but
	 * I did not copy a tree diagram and instead made it from scratch using
	 * my own knowledge. Link to source will be below:
	 * 
	 * Source: http://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
	 * Answer from: Todd Davies
	 * Answered on: Nov 26 2014
	 * Last edited on: March 7th, 2015
	
	public String toString() {
		return toStringHelper(new StringBuilder(), true, new StringBuilder(), this.root).toString();
	}

	public StringBuilder toStringHelper(StringBuilder prefix, boolean isTail, StringBuilder sb, Node curr) {
		if (curr == null) {
			return sb;
		}
		if (curr.right != null && !curr.right.dirty) {
			toStringHelper(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb, curr.right);
		}
		if (!curr.dirty) {
			sb.append(prefix).append(isTail ? "└── " : "┌── ").append(curr.data.toString()).append("\n");
		}
		if (curr.left != null && !curr.left.dirty) {
			toStringHelper(new StringBuilder().append(prefix).append(isTail ? "    " : "|   "), true, sb, curr.left);
		}
		return sb;
	}
	*/

    /**
     * Testing method
     */
    public void printAllHelper(Node p) {
        if (p == null) {
            return;
        }
        printAllHelper(p.left);
        System.out.print(p.data + ", ");
        printAllHelper(p.right);
    }

    /**
     * A Node is a Location, which means that it can be the return value of a search on the tree.
     */

    class Node implements Location<K> {
        protected K data;
        protected Node left, right;

        /**
         * Before and After pointers for a Node. Created a Threaded tree.
         */
        protected Node before, after;

        protected Node parent; // the parent of this node
        protected int height; // the height of the subtree rooted at this node
        protected boolean dirty; // true iff the key in this node has been
        // removed

        /**
         * Constructs a leaf node with the given key.
         *
         * @param key
         *         The data for the node to be created.
         */
        public Node(K key) {
            this(key, null, null);
        }

        /**
         * Constructs a new node with the given values for fields.
         *
         * @param data
         *         The data for this Node
         * @param left
         *         The left node to this node.
         * @param right
         *         The Right node to this Node.
         */
        public Node(K data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
            this.dirty = false;
            this.height = 1;
            this.fixHeight();
        }

        /**
         * Return true iff this node is a leaf in the tree.
         *
         * @return true iff this node is a leaf in the tree.
         */
        protected boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * Performs a local update on the height of this node. Assumes that the heights in the child nodes are correct.
         * This function *must* run in O(1) time.
         */
        protected void fixHeight() {
            if (right != null && left != null) {
                this.height = Math.max(right.height, left.height);
                this.height++;
            } else if (left != null) {
                this.height = left.height + 1;
            } else if (right != null) {
                this.height = right.height + 1;
            } else if (this.isLeaf()) {
                this.height = 1;
            }
        }

        /**
         * Returns the data in this node.
         *
         * @return The data for this Node.
         */
        public K get() {
            return data;
        }

        /**
         * Returns the location of the node containing the inorder predecessor of this node.
         *
         * @return The location of the node containing the inorder predecessor of this node.
         */
        public Node getBefore() {
            Node p = this;
            while (p.before != null) {
                if (!p.before.dirty) {
                    return p.before;
                } else {
                    p = p.before;
                }
            }
            return null;
        }

        /**
         * Returns the location of the node containing the inorder successor of this node.
         *
         * @return The location of the node containing the inorder successor of this node.
         */
        public Node getAfter() {
            Node p = this;
            while (p.after != null) {
                if (!p.after.dirty) {
                    return p.after;
                } else {
                    p = p.after;
                }
            }
            return null;
        }

        /**
         * Returns true iff this node is dirty.
         *
         * @return true iff this Node is dirty.
         */
        public Boolean isDirty() {
            return dirty;
        }

        /**
         * Returns the balance of this Node's children heights.
         *
         * @return The balance factor for this node.
         */
        public int getBalance() {
            int leftChildHeight = 0;
            if (this.left != null) {
                leftChildHeight = this.left.height;
            }

            int rightChildHeight = 0;
            if (this.right != null) {
                rightChildHeight = this.right.height;
            }

            int balance = rightChildHeight - leftChildHeight;
            return Math.abs(balance);
        }
    }
}
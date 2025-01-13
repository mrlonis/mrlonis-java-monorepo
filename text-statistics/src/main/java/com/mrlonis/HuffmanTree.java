package com.mrlonis;

import java.util.Comparator;

/**
 * A HuffmanTree represents a variable-length code such that the shorter the bit pattern associated with a character,
 * the more frequently that character appears in the text to be encoded.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class HuffmanTree {

    protected Node root;

    /**
     * Creates a HuffmanTree from the given frequencies of letters in the alphabet using the algorithm described in
     * lecture.
     *
     * @param charFreqs Frequency Table to be used in the construction of this HuffmanTree. Contains the frequencies for
     *     each character.
     */
    public HuffmanTree(FrequencyTable charFreqs) {
        Comparator<Node> comparator = (x, y) -> {
            /**
             * x and y are Nodes x comes before y if x's priority is less than y's priority.
             *
             * <p>Creating Integer objects per each Node priority to be used in comparator.
             */
            Integer xInt = x.priority;
            Integer yInt = y.priority;
            return xInt.compareTo(yInt);
        };
        PriorityQueue<Node> forest = new Heap<>(comparator);

        /** Complete the implementation of Huffman's Algorithm. Start by populating forest with leaves. */
        charFreqs.forEach((letter, priority) -> {
            Node temp = new Node(priority, letter);
            forest.insert(temp);
        });

        while (forest.size() > 1) {
            Node one = forest.delete();
            Node two = forest.delete();
            int newPriority = one.priority + two.priority;
            /** Dummy data for the new Node. */
            Node tree = new Node(newPriority, '\0', one, two);

            /** Updating parent links. */
            tree.left.parent = tree;
            tree.right.parent = tree;
            tree.parent = null;

            forest.insert(tree);
        }

        root = forest.peek();
    }

    /**
     * Returns the character associated with the prefix of bits.
     *
     * @param bits Prefix of bits to be match to a character association.
     * @return Character association for the given bit string.
     * @throws DecodeException if bits does not match a character in the tree.
     */
    public char decodeChar(String bits) {
        Node curr = root;
        int n = bits.length();

        for (int i = 0; i < n; i++) {

            /** If curr is a leaf you are at the character due to the fact that no character has the same prefix. */
            if (curr.isLeaf()) {
                return curr.key;
            }

            /** Traverse through the tree. */
            if (bits.charAt(i) == '0') {
                curr = curr.left;
            } else if (bits.charAt(i) == '1') {
                curr = curr.right;
            }
        }

        if (curr == null || !curr.isLeaf()) {
            throw new DecodeException(bits);
        }

        return curr.key;
    }

    /**
     * Returns the bit string associated with the given character. Must search the tree for a leaf containing the
     * character. Every left turn corresponds to a 0 in the code. Every right turn corresponds to a 1. This function is
     * used by CodeBook to populate the map.
     *
     * @param ch The character to find the bit string of.
     * @return The bit string associated with the given character.
     * @throws EncodeException if the character does not appear in the tree.
     */
    public String lookup(char ch) {
        Node curr = root;

        /** Searches for Node. */
        Node temp = search(curr, ch);

        if (temp == null) {
            throw new EncodeException(ch);
        }

        StringBuilder str = new StringBuilder();

        while (temp.parent != null) {
            Node temp1 = temp.parent;
            if (temp == temp1.left) {
                str.append("0");
                temp = temp1;
            } else if (temp == temp1.right) {
                str.append("1");
                temp = temp1;
            }
        }

        /** Reverse the string since we built it up backwards while walking up the tree. */
        str.reverse();
        return str.toString();
    }

    /**
     * Searches the Tree for a Node containing the given character ch.
     *
     * @param curr The current node in the traversal of the tree.
     * @param ch The character to search for in the tree.
     * @return The Node containing the given character if it exists, null otherwise.
     */
    public Node search(Node curr, char ch) {
        if (curr == null) {
            return curr;
        } else if (curr.key == ch) {
            return curr;
        } else {
            Node temp = search(curr.left, ch);

            if (temp == null) {
                temp = search(curr.right, ch);
            }

            return temp;
        }
    }

    static class Node {
        protected char key;
        protected int priority;
        protected Node left, right;

        /**
         * Parent Node to be used in my search method.
         *
         * <p>Increases efficiency.
         */
        protected Node parent;

        public Node(int priority, char key) {
            this(priority, key, null, null);
        }

        public Node(int priority, Node left, Node right) {
            this(priority, '\0', left, right);
        }

        public Node(int priority, char key, Node left, Node right) {
            this.key = key;
            this.priority = priority;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        public String toString() {
            int inte = this.priority;
            return Integer.toString(inte);
        }
    }
}

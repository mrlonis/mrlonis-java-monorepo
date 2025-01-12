package com.mrlonis.lab11;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Class to implement a Trie as shown in Week 11 lectures.
 *
 * @author mrlonis
 */
public class Trie implements Iterable<String> {

    /**
     * Node class contains a String and a HashMap<Char, Node> that is potentially empty.
     *
     * @author mrlonis
     */
    class Node {
        /** If word is null, then no data present in this node. */
        String word;

        /** Only non-null children are stored. */
        Map<Character, Node> children;

        /** Creates an empty leaf node. */
        Node() {
            this(null);
        }

        /** Creates a leaf node with the given word present. */
        Node(String word) {
            this.word = word;
            children = new HashMap<>();
        }

        /** Inserts s into the trie assuming that the first i characters in s have led us to this node. */
        void insert(String s, int i) {
            if (i == s.length()) {
                if (this.word == null) {
                    n++;
                    this.word = s;
                }
            } else {
                char c = s.charAt(i);
                if (!this.children.containsKey(c)) {
                    this.children.put(c, new Node());
                }
                this.children.get(c).insert(s, i + 1);
            }
        }

        /** Removes s from the trie assuming that the first i character in s have led us to this node. */
        void remove(String s, int i) {
            if (i == s.length()) {
                if (this.word != null) {
                    n--;
                    this.word = null;
                }
            } else {
                char c = s.charAt(i);
                if (!this.children.containsKey(c)) {
                    return;
                }
                this.children.get(c).remove(s, i + 1);
            }
        }

        /** Returns true iff s is in the trie assuming that the first i characters in s have led us to this node. */
        boolean contains(String s, int i) {
            if (i == s.length()) {
                return this.word != null;
            } else {
                char c = s.charAt(i);
                if (!this.children.containsKey(c)) {
                    return false;
                }
                return this.children.get(c).contains(s, i + 1);
            }
        }

        /**
         * Recursively loops through the trie finding all the keys, and adds them to the global variable keys. This
         * helps the method Trie.getKeys().
         */
        public void getKeys() {
            if (this.word != null) {
                keys.add(this.word);
            }

            this.children.forEach((c, node) -> {
                node.getKeys();
            });
        }
    }

    protected Node root = new Node(); // root is never null
    protected int n = 0; // number of keys in this trie

    /** Inserts s into this trie. If s was already present in this trie, then nothing happens. */
    public void insert(String s) {
        root.insert(s, 0);
    }

    /** Removes s from this trie. If s was not in this trie to begin with, then nothing happens. */
    public void remove(String s) {
        root.remove(s, 0);
    }

    /** Return true iff s is a key in this trie. */
    public boolean contains(String s) {
        return root.contains(s, 0);
    }

    /** Returns the number of keys in this trie. Must run in O(1) time. */
    public int size() {
        return n;
    }

    /** Returns true iff this trie is empty. Must run in O(1) time. */
    public boolean isEmpty() {
        return size() == 0;
    }

    /** Resets this trie to be empty. */
    public void clear() {
        n = 0;
        root = new Node();
    }

    /**
     * Global variable for keys. Is wiped every time getKeys() is called and then rebuilt. Made this a global variable
     * for easier code during my helper method Node.getKeys().
     */
    List<String> keys = new ArrayList<>(this.n);

    /**
     * Returns a sorted list of all the keys in this trie. If you need a helper, put it in the Node class. Make use of
     * Collections.sort() to arrange the keys in sorted order. This method should run in O(n log n) time, where n is the
     * number of keys.
     */
    public List<String> getKeys() {
        keys = new ArrayList<>(this.n);
        root.getKeys();
        Collections.sort(keys);
        return keys;
    }

    /**
     * Returns an iterator over the keys in this trie. Note that removing via the returned iterator will not remove the
     * key in the trie.
     */
    public Iterator<String> iterator() {
        return getKeys().iterator();
    }

    /** Returns a textual representation of the keys in this trie. */
    public String toString() {
        return getKeys().toString();
    }

    public static void main(String... args) {
        Trie trie = new Trie();
        String[] words;
        words = new String[] {"he", "she", "his", "hers", "her"};
        for (String word : words) trie.insert(word);
        for (String word : words) assert trie.contains(word);
        assert 5 == trie.size();
        trie.remove("he");
        trie.remove("he");
        trie.remove("ours");
        assert 4 == trie.size();
        assert trie.contains("he") == false;
        assert trie.contains("her");
        trie.insert("they");
        trie.insert("their");

        Dictionary dict = new Dictionary(word -> word.length() >= 14);
        int n = dict.size();
        assert 113 == n;
        assert dict.contains("psychoanalysis");
        for (String word : dict) {
            dict.remove(word);
            n--;
            assert dict.size() == n;
        }
        assert dict.isEmpty();
        dict = new Dictionary(word -> word.length() >= 5);
        assert !dict.contains("cat");
        n = dict.size();
        assert 17_526 == n;

        System.out.println("All tests passed...");
    }
}

class Dictionary extends Trie {
    public static final String FILENAME = "big-dictionary.txt";

    /** Reads the words from the file and creates an unfiltered dictionary. */
    public Dictionary() {
        this(word -> true);
    }

    /**
     * Reads the words from the file and creates a dictionary populated with only those words that satisfy the given
     * predicate.
     */
    public Dictionary(Predicate<String> pred) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILENAME));
            String word;
            while ((word = in.readLine()) != null) if (pred.test(word)) insert(word);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("The dictionary file, " + FILENAME + ", is not found.");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}

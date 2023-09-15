package com.mrlonis;

import java.util.HashMap;

/**
 * A CodeBook maps characters to bit strings. The code is specific to a text string so as to achieve maximal
 * compression.
 * <p>
 * Once constructed, the book is used to decode single characters in the code. This is more efficient than looking up
 * the code in the Huffman tree each time it is needed.
 * <p>
 * A CodeBook creates a HuffmanTree as an artifact, and provides a getter to make the tree available.
 *
 * @author Matthew Lonis (mrlonis)
 */

public class CodeBook {
    private final HashMap<Character, String> book = new HashMap<>();
    private final HuffmanTree ht;
    private final FrequencyTable freqs;
    private int n = 1;

    /**
     * Creates a CodeBook based on the probabilities of English letters. Source: Wikipedia.
     */
    public CodeBook() {
        this.freqs = new FrequencyTable();
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        double[] probs =
                new double[]{11.602, 4.702, 3.511, 2.670, 2.007, 3.779, 1.950, 7.232, 6.286, 0.597, 0.590, 2.705, 4.383,
                             2.365, 6.264, 2.545, 0.173, 1.653, 7.755, 16.671, 1.487, 0.649, 6.753, 0.017, 1.620,
                             0.034,};
        for (int i = 0; i < alpha.length(); i++) {
            freqs.put(alpha.charAt(i), (int) (probs[i] * 1000));
        }
        ht = new HuffmanTree(freqs);
        readCodeFromTree();
    }

    /**
     * Creates a CodeBook optimized for the supplied text string.
     *
     * @param text
     *         The text to be used in creating this CodeBook. CodeBook will be optomized to the given text.
     */
    public CodeBook(String text) {
        freqs = new FrequencyTable(text);
        n = text.length();
        ht = new HuffmanTree(freqs);
        readCodeFromTree();
    }

    /**
     * Reads the code represented by the HuffmanTree and stores each association in the map.
     */
    private void readCodeFromTree() {
        /**
         * For each Character and Count in the FrequencyTable, put the character
         * and corresponding bit string code into this CodeBook.
         */
        this.freqs.forEach((chars, ints) -> {
            String code = this.ht.lookup(chars);
            this.book.put(chars, code);
        });
    }

    /**
     * Returns the HuffmanTree associated with this CodeBook.
     *
     * @return The HuffmanTree associated with this CodeBook.
     */
    public HuffmanTree getHuffmanTree() {
        return ht;
    }

    /**
     * Returns the size of the alphabet associated with this book.
     *
     * @return The size of the alphabet associated with this CodeBook.
     */
    public int size() {
        return book.size();
    }

    /**
     * Returns a measure of the effectiveness of this code, as a sum of normalized char frequencies weighted by the
     * length of their encodings.
     *
     * @return A measure of the effectiveness of this code, as a sum of normalized char frequencies weighted by the
     * length of their encodings.
     */
    public double getWeightedAverage() {
        double sum = 0;
        for (char ch : freqs.keySet()) {
            sum += book.get(ch).length() * ((double) freqs.get(ch) / n);
        }
        return sum;
    }

    /**
     * Returns a textual representation of this code.
     *
     * @return A textual representation of this CodeBook.
     */
    public String toString() {
        return book.toString();
    }

    /**
     * Returns the bit string encoding for the given character by looking up the character in the map. Important: do not
     * search the HuffmanTree here!
     *
     * @param ch
     *         The character to return the bit string code of.
     *
     * @return The bit string associated with the given character.
     */
    public String encodeChar(Character ch) {
        return this.book.get(ch);
    }
}

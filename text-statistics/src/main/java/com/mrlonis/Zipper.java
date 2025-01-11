package com.mrlonis;

/**
 * A Zipper is used to encode/decode text strings based on a supplied code book. Additionally, a Zipper is capable of
 * compressing a bit string into a string of characters, as well as the inverse (i.e., decompressing a character string
 * into a bit string).
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Zipper {
    private CodeBook book; // used for encoding
    private HuffmanTree ht; // used for decoding

    /** Create a Zipper from the provided code book. */
    public Zipper(CodeBook book) {
        this.book = book;
        ht = book.getHuffmanTree();
    }

    /**
     * Returns the bit string encoding of the given plain text.
     *
     * @param plainText The text to convert to a bit string encoding.
     * @return The bit string encoding of the given text.
     */
    public String encode(String plainText) {
        StringBuilder str = new StringBuilder();
        char[] strToArray = plainText.toCharArray();

        for (char x : strToArray) {
            str.append(this.book.encodeChar(x));
        }

        return str.toString();
    }

    /**
     * Returns the text string corresponding to the given bit string.
     *
     * @param bits The bit string to return the corresponding text string of.
     * @return The text string corresponding to the given bit string.
     */
    public String decode(String bits) {
        StringBuilder str = new StringBuilder();

        while (!bits.isEmpty()) {
            char decode = this.ht.decodeChar(bits);
            int decodeLength = this.book.encodeChar(decode).length();
            str.append(decode);

            if (decodeLength == bits.length()) {
                bits = "";
            } else {
                bits = bits.substring(decodeLength);
            }
        }

        return str.toString();
    }

    /**
     * Returns the result of compressing a string of bits into a string of 8-bit characters.
     *
     * @param bits The string bits to compress into a string of 8-bits.
     * @return The result of compressing the bit string into a string of 8-bits.
     */
    public String compress(String bits) {
        int n = bits.length();
        /**
         * The last byte may not be full. We'll need to know how many bits are in the last byte, so that we can
         * decompress properly later. We will prepend the compressed string with a head byte that holds the size of the
         * last byte
         */
        int lastBiteSize = n % Constants.BITESIZE;
        if (lastBiteSize == 0) {
            lastBiteSize = Constants.BITESIZE; // the last byte is full
        }
        String headByte = Util.padLeft(Integer.toBinaryString(lastBiteSize), Constants.BITESIZE);
        StringBuilder chars = new StringBuilder();
        chars.append(Util.bitsToAscii(headByte));
        for (int i = 0; i < n; i += Constants.BITESIZE) {
            String block = bits.substring(i, Math.min(n, i + Constants.BITESIZE));
            chars.append(Util.bitsToAscii(block));
        }
        return chars.toString();
    }

    /**
     * Returns the result of expanding the given compressed text into a bit string.
     *
     * @param compressedText The compressed text to be expanded.
     * @return The result of expaning the given compressed text into a bit string.
     */
    public String decompress(String compressedText) {
        // Process the head byte to retrieve the size of the last byte.
        int lastBiteSize = (int) compressedText.charAt(0);
        String bits = "";
        int n = compressedText.length();
        for (int i = 1; i < n - 1; i++) {
            bits += Util.asciiToBits(compressedText.charAt(i), Constants.BITESIZE);
        }
        bits += Util.asciiToBits(compressedText.charAt(n - 1), lastBiteSize);
        return bits;
    }
}

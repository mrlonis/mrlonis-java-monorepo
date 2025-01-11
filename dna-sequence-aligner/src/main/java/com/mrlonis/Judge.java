package com.mrlonis;

import java.io.Serial;

/**
 * This class is used to score alignments.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Judge {

    public static final int DEFAULT_MATCH_COST = 2;
    public static final int DEFAULT_MISMATCH_COST = -2;
    public static final int DEFAULT_GAP_COST = -1;
    private final int matchCost;
    private final int mismatchCost;
    private final int gapCost;

    /** Creates the default judge. */
    public Judge() {
        this(DEFAULT_MATCH_COST, DEFAULT_MISMATCH_COST, DEFAULT_GAP_COST);
    }

    /** Creates a judge using the specified costs. */
    public Judge(int matchCost, int mismatchCost, int gapCost) {
        this.matchCost = matchCost;
        this.mismatchCost = mismatchCost;
        this.gapCost = gapCost;
    }

    /** Returns the gap cost used by this judge. */
    public int getGapCost() {
        return gapCost;
    }

    /** Returns the match cost used by this judge. */
    public int getMatchCost() {
        return matchCost;
    }

    /** Returns the mismatch cost used by this judge. */
    public int getMismatchCost() {
        return mismatchCost;
    }

    /**
     * Returns the score associated with the two strings.
     *
     * @param s1 The first string in the scoring.
     * @param s2 The second string in the scoring.
     * @return The score associated with s1 and s2.
     * @throws StringLengthException if s1 and s2 are not the same length.
     */
    public int score(String s1, String s2) {
        int ans = 0;
        int n = s1.length();
        int m = s2.length();

        if (n != m) {
            throw new StringLengthException("String Lengths Don't Match");
        }

        for (int i = 0; i < n; i++) {
            ans += score(s1.charAt(i), s2.charAt(i));
        }

        return ans;
    }

    /**
     * Returns the score associated with the two characters.
     *
     * @param a The first character in the scoring.
     * @param b The second character in the scoring.
     * @return The score associated with the two characters.
     */
    public int score(char a, char b) {
        if (a == Constants.GAP_CHAR || b == Constants.GAP_CHAR) {
            return this.gapCost;
        } else if (a == b) {
            return this.matchCost;
        } else {
            return this.mismatchCost;
        }
    }

    /**
     * Custom RuntimeException that is thrown in the score method if two strings are not of the same length.
     *
     * @author Matthew Lonis (mrlonis)
     */
    public static class StringLengthException extends RuntimeException {

        /** Randomly Generated Serial ID */
        @Serial
        private static final long serialVersionUID = 7428981602646859900L;

        /** Default empty constructor. */
        public StringLengthException() {
            super();
        }

        /**
         * Throws a RuntimeException if two strings being scored are not of the same length.
         *
         * @param str The message returned to the user.
         */
        public StringLengthException(String str) {
            super(str);
        }
    }
}

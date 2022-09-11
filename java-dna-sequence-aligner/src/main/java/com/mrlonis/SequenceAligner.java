package com.mrlonis;

import java.util.Random;

/**
 * Implement the fillCache(), getResult(), and traceback() methods, in that order. This is the biggest part of this project.
 * <p>
 * The Sequence Aligner determines the bets possible alignment between two strings by filling in an array of Results (size: n x m) based on a given Judge.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class SequenceAligner {

    /**
     * Class random object.
     */
    private static final Random gen = new Random();
    /**
     * Class variables.
     */
    private final String x;
	private final String y;
    private final int n;
	private final int m;
    private String alignedX, alignedY;
    private final Result[][] cache;
    private final Judge judge;

    /**
     * Generates a pair of random DNA strands, where x is of length n and y has some length between n/2 and 3n/2, and aligns them using the default judge.
     */
    public SequenceAligner(int n) {
        this(randomDNA(n), randomDNA(n - gen.nextInt(n / 2) * (gen.nextInt(2) * 2 - 1)));
    }

    /**
     * Aligns the given strands using the default judge.
     */
    public SequenceAligner(String x,
                           String y) {
        this(x, y, new Judge());
    }

    /**
     * Returns a DNA strand of length n with randomly selected nucleotides.
     */
    private static String randomDNA(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("ACGT".charAt(gen.nextInt(4)));
        }
        return sb.toString();
    }

    /**
     * Aligns the given strands using the specified judge.
     */
    public SequenceAligner(String x,
                           String y,
                           Judge judge) {
        this.x = x.toUpperCase();
        this.y = y.toUpperCase();
        this.judge = judge;
        n = x.length();
        m = y.length();
        cache = new Result[n + 1][m + 1];
        fillCache();
        traceback();
    }

    /**
     * Solve the alignment problem using bottom-up dynamic programming algorithm described in lecture. When you're done, cache[i][j] will hold the result of solving the alignment
     * problem for the first i characters in x and the first j characters in y.
     * <p>
     * Your algorithm must run in O(n * m) time, where n is the length of x and m is the length of y.
     * <p>
     * Ordering convention: So that your code will identify the same alignment as is expected in Testing, we establish the following preferred order of operations: M (diag), I
     * (left), D (up). This only applies when you are picking the operation with the biggest payoff and two or more operations have the same max score.
     */
    private void fillCache() {
        /*
         * Store GapCost to avoid recurring calls to the method. Makes it easier
         * for the programmer.
         */
        int gapCost = this.judge.getGapCost();

        /*
         * Fill this.cache[0][0].
         */
        this.cache[0][0] = new Result(0);

        /*
         * Fill initial left edge of the array with scores. All directions will
         * be Direction.UP.
         */
        for (int i = 1, j = gapCost; i <= n; i++, j += gapCost) {
            this.cache[i][0] = new Result(j, Direction.UP);
        }

        /*
         * Fill initial upper edge of the array with scores. All directions will
         * be Direction.LEFT.
         */
        for (int j = 1, i = gapCost; j <= m; j++, i += gapCost) {
            this.cache[0][j] = new Result(i, Direction.LEFT);
        }

        /*
         * Fill the rest of the cache.
         */
        for (int i = 1; i <= this.n; i++) {
            for (int j = 1; j <= this.m; j++) {
                int score = this.judge.score(this.x.charAt(i - 1), this.y.charAt(j - 1));
                int diag = this.cache[i - 1][j - 1].getScore() + score;
                int up = this.cache[i - 1][j].getScore() + gapCost;
                int left = this.cache[i][j - 1].getScore() + gapCost;

                if (diag >= up && diag >= left) {
                    this.cache[i][j] = new Result(diag, Direction.DIAGONAL);
                } else if (left >= diag && left >= up) {
                    this.cache[i][j] = new Result(left, Direction.LEFT);
                } else if (up >= diag && up >= left) {
                    this.cache[i][j] = new Result(up, Direction.UP);
                }
            }
        }
    }

    /**
     * Mark the path by tracing back through parent pointers, starting with the Result in the lower right corner of the cache. Run Result.markPath() on each Result along the path.
     * The GUI will highlight all such marked cells when you check 'Show path'. As you're tracing back along the path, build the aligned strings in alignedX and alignedY (using
     * Constants.GAP_CHAR to denote a gap in the strand).
     * <p>
     * Your algorithm must run in O(n + m) time, where n is the length of x and m is the length of y.
     */
    private void traceback() {
        StringBuilder x = new StringBuilder();
        StringBuilder y = new StringBuilder();

        for (int i = n, j = m; i >= 0 && j >= 0; ) {
            this.cache[i][j].markPath();
            Result ans = this.getResult(i, j);
            Direction parent = ans.getParent();

            if (parent.equals(Direction.DIAGONAL)) {
                x.append(this.x.charAt(i - 1));
                y.append(this.y.charAt(j - 1));
                i--;
                j--;
            } else if (parent.equals(Direction.LEFT)) {
                x.append(Constants.GAP_CHAR);
                y.append(this.y.charAt(j - 1));
                j--;
            } else if (parent.equals(Direction.UP)) {
                x.append(this.x.charAt(i - 1));
                y.append(Constants.GAP_CHAR);
                i--;
            } else {
                x.reverse();
                y.reverse();
                alignedX = x.toString();
                alignedY = y.toString();
                return;
            }
        }
    }

    /**
     * Returns the result of solving the alignment problem for the first i characters in x and the first j characters in y.
     *
     * @param i The row in the cache to lookup in.
     * @param j The column in the cache to lookup in.
     * @return The value in the cache at row i and column j.
     */
    public Result getResult(int i,
                            int j) {
        return this.cache[i][j];
    }

    /**
     * Returns the x strand.
     */
    public String getX() {
        return x;
    }

    /**
     * Returns the y strand.
     */
    public String getY() {
        return y;
    }

    /**
     * Returns the judge associated with this pair.
     */
    public Judge getJudge() {
        return judge;
    }

    /**
     * Returns the aligned version of the x strand.
     */
    public String getAlignedX() {
        return alignedX;
    }

    /**
     * Returns the aligned version of the y strand.
     */
    public String getAlignedY() {
        return alignedY;
    }

    /**
     * Returns a nice textual version of this alignment.
     */
    public String toString() {
        if (!isAligned()) {
            return "[X=" + x + ",Y=" + y + "]";
        }
        final char GAP_SYM = '.', MATCH_SYM = '|', MISMATCH_SYM = ':';
        StringBuilder ans = new StringBuilder();
        ans.append(alignedX)
           .append('\n');
        int n = alignedX.length();
        for (int i = 0; i < n; i++) {
            if (alignedX.charAt(i) == Constants.GAP_CHAR || alignedY.charAt(i) == Constants.GAP_CHAR) {
                ans.append(GAP_SYM);
            } else if (alignedX.charAt(i) == alignedY.charAt(i)) {
                ans.append(MATCH_SYM);
            } else {
                ans.append(MISMATCH_SYM);
            }
        }
        ans.append('\n')
           .append(alignedY)
           .append('\n')
           .append("score = ")
           .append(getScore());
        return ans.toString();
    }

    /**
     * Returns true iff these strands are seemingly aligned.
     */
    public boolean isAligned() {
        return alignedX != null && alignedY != null && alignedX.length() == alignedY.length();
    }

    /**
     * Returns the score associated with the current alignment.
     */
    public int getScore() {
        if (isAligned()) {
            return judge.score(alignedX, alignedY);
        }
        return 0;
    }

}

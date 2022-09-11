package com.mrlonis;

/**
 * StringMatch class with static utilities to find patterns in strings. This class features the Naive string matching algorithm, the KMP matching algorithm, and the Boyer-Moore
 * string matching algorithm.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class StringMatch {

    /**
     * Returns the result of running the naive algorithm to match pattern in text.
     *
     * @param pattern The specified pattern to search for in text.
     * @param text    The text to search for pattern in.
     * @return The Result of running the naive algorithm to match pattern in text.
     */
    public static Result matchNaive(String pattern,
                                    String text) {
        int m = pattern.length(), n = text.length();
        Result ans = new Result(-1, 0);
        int i = 0, j = 0;

        while (i < m && j <= n - m + i) {
            if (pattern.charAt(i) == text.charAt(j)) {
                i++;
                j++;
            } else {
                j = j - i + 1;
                i = 0;
            }

            ans.comps++;
        }

        if (i == m) {
            ans.pos = j - m;
            return ans;
        }

        return new Result(-1, ans.comps);
    }

    /**
     * Returns the result of running the KMP algorithm to match pattern in text. The number of comparisons includes the cost of building the machine from the pattern.
     *
     * @param pattern The pattern to be searched for in the specified text.
     * @param text    The text to search for pattern in.
     * @return The Result of running the KMP algorithm to match pattern in text.
     */
    public static Result matchKMP(String pattern,
                                  String text) {
        int m = pattern.length();
        int[] flink = new int[m + 1];
        int comps = buildKMP(pattern, flink);
        Result ans = runKMP(pattern, text, flink);
        return new Result(ans.pos, comps + ans.comps);
    }

    /**
     * Populates flink with the failure links for the KMP machine associated with the given pattern, and returns the cost in terms of the number of character comparisons.
     *
     * @param pattern The pattern to be used to construct the KMP failure links
     * @param flink   The array to fill with failure links.
     * @return The number of character comparisons performed during the process of building flink.
     */
    public static int buildKMP(String pattern,
                               int[] flink) {
        int m = pattern.length();
        int ans = 0;
        flink[0] = -1;

        if (m > 0) {
            flink[1] = 0;
            int i = 2;

            while (i <= m) {
                int j = flink[i - 1];

                while (j != -1 && pattern.charAt(j) != pattern.charAt(i - 1)) {
                    ans++;
                    j = flink[j];
                }

                if (j != -1) {
                    ans++;
                }

                flink[i] = j + 1;
                i++;
            }
        }

        return ans;
    }

    /**
     * Returns the result of running the KMP machine specified by flink (built for the given pattern) on the text.
     *
     * @param pattern The specified pattern to search for in text.
     * @param text    The text to search for pattern in.
     * @param flink   The KMP failure links for the given pattern.
     * @return The Result of running the KMP algorithm to match pattern in text.
     */
    public static Result runKMP(String pattern,
                                String text,
                                int[] flink) {
        int state = -1, j = -1, m = pattern.length(), n = text.length();
        Result ans = new Result(-1, 0);

        while (true) {
            ans.comps++;

            if (state == -1 || text.charAt(j) == pattern.charAt(state)) {
                state++;

                if (state == 0) {
                    ans.comps--;
                }

                if (state == m) {
                    ans.pos = j - m + 1;
                    return ans;
                }

                j++;

                if (j == n) {
                    return ans;
                }
            } else {
                state = flink[state];
            }
        }
    }

    /**
     * Returns the result of running the simplified Boyer-Moore algorithm to match pattern in text.
     */
    public static Result matchBoyerMoore(String pattern,
                                         String text) {
        int[] delta1 = new int[Constants.SIGMA_SIZE];
        buildDelta1(pattern, delta1);
        return runBoyerMoore(pattern, text, delta1);
    }

    /**
     * Populates delta1 with the shift values associated with each character in the alphabet. Assume delta1 is large enough to hold any ASCII value.
     *
     * @param pattern The pattern used to build delta1.
     * @param delta1  The array to be filled that corresponds to the Boyer Moore delta1.
     */
    public static void buildDelta1(String pattern,
                                   int[] delta1) {
        int delta1Length = Constants.SIGMA_SIZE;
        int n = pattern.length();

        for (int i = 0; i < delta1Length; i++) {
            delta1[i] = n;
        }

        for (int i = n - 1; i >= 0; i--) {
            char c = pattern.charAt(i);
            int index = c;
            int value = n - i - 1;

            if (delta1[index] == n && index < delta1Length && index >= 0) {
                delta1[index] = value;
            }
        }
    }

    /**
     * Returns the result of running the simplified Boyer-Moore algorithm using the delta1 table from the pre-processing phase.
     *
     * @param pattern The pattern to search for in text.
     * @param text    The text to search for pattern in.
     * @param delta1  The delta1 that corresponds to pattern.
     * @return The Result of running the simplified Boyer-Moore algorithm using the delta1 from the pre-processing phase.
     */
    public static Result runBoyerMoore(String pattern,
                                       String text,
                                       int[] delta1) {
        int n = text.length(), m = pattern.length(), i = m - 1, j = m - 1, finger = m - 1, partialMatch = 0;
        Result ans = new Result(-1, 0);

        while (true) {
            while (j >= 0 && i < n) {
                char c = text.charAt(finger), d = pattern.charAt(j);

                if (d == c) {
                    partialMatch++;
                    j--;
                    finger--;
                } else {
                    j = m - 1;
                    if ((int) c >= Constants.SIGMA_SIZE || (int) c < 0) {
                        i += Math.max(pattern.length() - partialMatch, 1);
                    } else {
                        i += Math.max(delta1[c] - partialMatch, 1);
                    }
                    finger = i;
                    partialMatch = 0;
                }

                ans.comps++;
            }

            if (j < 0) {
                ans.pos = finger + 1;
            }

            return ans;
        }
    }

}
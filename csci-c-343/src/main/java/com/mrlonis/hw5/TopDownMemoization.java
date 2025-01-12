package com.mrlonis.hw5;

import java.util.HashMap;
import java.util.Map;

/**
 * [hw5] Problem 2:
 *
 * <p>Most of the work that you are to do is in the implementation of the lisHelper() function. However, there are two
 * small, but important, items in the Problem class below that require your attention.
 *
 * <p>We implement a top-down memoized solution to the Longest Increasing Subsequence problem, following the recursive
 * algorithm described in lecture. See the notes from lec9a and lec9b.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class TopDownMemoization {

    private static Map<SubProblem, Integer> cache = new HashMap<>();

    /** Returns the length of the longest increasing subsequence in the array a. */
    public static int lis(int[] a) {
        cache.clear();

        /*
         * Initially, we consider the element at position 0 and our cap, so far,
         * is 0.
         */
        SubProblem p = new SubProblem(0, 0);
        return lisHelper(a, p);
    }

    /** Returns the result of solving the SubProblem described by p. */
    public static int lisHelper(int[] a, SubProblem p) {
        /*
         * Check to see if this problem has been previously solved. If so,
         * return the cached value.
         */
        if (cache.containsKey(p)) {
            return cache.get(p);
        }

        int ans = 0;
        if (p.pos < a.length) {
            int with = 0, without = 0;

            /*
             * Recur to find the "with" and "without" results, so we can use
             * those results to build the answer ans.
             */
            if (a[p.pos] > p.cap) {
                with = lisHelper(a, new SubProblem(p.pos + 1, a[p.pos]));
                without = lisHelper(a, new SubProblem(p.pos + 1, p.cap));
            } else {
                without = lisHelper(a, new SubProblem(p.pos + 1, p.cap));

                /*
                 * If it a[p.pos] is not greater than p.cap, set with equal to
                 * -1 so when the Math.max code is run, with won't equal 1
                 * (after the 0 + 1 code) and instead will equal 0 as expected.
                 */
                with = -1;
            }

            ans = Math.max(1 + with, without);
        }

        /*
         * Store this problem/answer association in the cache for future
         * reference.
         */
        if (p.pos < a.length) {
            cache.put(p, ans);
        }

        return ans;
    }

    /** Write a comprehensive battery of test cases. */
    public static void main(String... args) {
        int[] a;
        a = new int[] {5, 6, 1, 2, 9, 3, 4, 7, 4, 3};
        assert 5 == lis(a);

        a = new int[] {2, 1, 5, 3, 6, 4, 2, 7, 9, 11, 8, 10};
        assert 6 == lis(a);

        a = new int[100];
        for (int i = 0; i < a.length; i++) {
            a[i] = i + 1;
        }
        assert a.length == lis(a);

        /*
         * Added tests. Tests edge cases and on large data.
         */
        a = new int[] {};
        assert 0 == lis(a);

        a = new int[] {1};
        assert 1 == lis(a);

        a = new int[] {2, 1};
        assert 1 == lis(a);

        a = new int[] {1, 2};
        assert 2 == lis(a);

        a = new int[] {1, 2, 6, 7, 3, 6, 3, 7, 9, 3, 1, 3, 0, 6, 2, 7, 8, 9, 10};
        assert 8 == lis(a);
        System.out.println("All tests passed...");
    }
}

/**
 * A SubProblem corresponds to one node in the decision tree. It is described by two pieces of information: the index in
 * the array of the element under consideration (for inclusion in the sequence being constructed), and the largest
 * element in the sequence so far.
 */
class SubProblem {
    /** The index in the array of the element under consideration. */
    int pos;

    /** The largest value in the sequence already selected. */
    int cap;

    /** Constructs a problem from the given position and cap. */
    SubProblem(int pos, int cap) {
        this.pos = pos;
        this.cap = cap;
    }

    /**
     * Returns true iff the given object equals this object, field for field. If we don't override this method, then the
     * hash map will not be able to find previously stored problems.
     *
     * <p>Compares hashCodes in order to determine if the objects are the same.
     */
    @Override
    public boolean equals(Object obj) {
        return obj.hashCode() == this.hashCode();
    }

    /**
     * Returns a nicely packed version of this SubProblem. This promotes good behavior of a hash map that uses
     * SubProblems as keys.
     *
     * <p>Previous attempt were:
     *
     * <p>return ((this.pos * 31) + (this.cap * 31) * 31);
     *
     * <p>&
     *
     * <p>return (this.pos * 31) + (this.cap * 31);
     *
     * <p>&
     *
     * <p>return (this.cap * 31) + this.pos;
     */
    @Override
    public int hashCode() {
        return (this.pos * 31) + this.cap;
    }

    /** Returns a sensible textual version of this SubProblem. */
    public String toString() {
        return String.format("(%d, %d)", pos, cap);
    }
}

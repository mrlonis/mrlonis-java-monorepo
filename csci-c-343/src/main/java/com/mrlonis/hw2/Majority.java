package com.mrlonis.hw2;

import java.util.Arrays;

/**
 * We implement a slightly modified version of the algorithm described in Problem 2.26 (pg 54) of Weiss. This
 * modification handles odd-length arrays.
 *
 * <p>A majority element in an array, A, of size n is an element that appears n / 2 times (thus, there is at most one).
 *
 * <p>Algorithm: First, identify one or two candidates for the majority element using the process described below. If no
 * candidates are identified during this step, then there is no majority in A, so stop. Otherwise, for each candidate,
 * determine if it is actually the majority using a sequential search through the array.
 *
 * <p>To find the candidates in the array A, form a second array B. Then compare A_1 and A_2. If they are equal, add one
 * of these to B; otherwise do nothing. Then compare A_3 and A_4. Again if they are equal, add one of these to B;
 * otherwise do nothing. Continue in this fashion until the entire array is read. If the length of A is odd, then add
 * the last element to B. Then recursively find the one or two majority candidates for B; these will be candidates for
 * A.
 */
public class Majority {

    /** Returns the majority element in a, if one exists. Otherwise, a NoMajorityException is thrown. */
    public static int majority(int[] a) {
        int[] b = findCandidates(a);
        assert b.length <= 2;
        for (int x : b) if (isMajority(x, a)) return x;
        throw new NoMajorityException();
    }

    /**
     * Returns an array of 0, 1, or 2 candidates for majority element in a. Implement the recursive algorithm described
     * in the main comment.
     *
     * <p>To find the candidates in the array A, form a second array B. Then compare A_1 and A_2. If they are equal, add
     * one of these to B; otherwise do nothing. Then compare A_3 and A_4. Again if they are equal, add one of these to
     * B; otherwise do nothing. Continue in this fashion until the entire array is read. If the length of A is odd, then
     * add the last element to B. Then recursively find the one or two majority candidates for B; these will be
     * candidates for A.
     */
    public static int[] findCandidates(int[] a) {
        int[] b = new int[a.length];
        int count = 0;
        int i = 0;
        while (i < a.length) {
            if (i == a.length - 1) {
                b[count] = a[i];
                count++;
                i++;
            } else if (a[i] == a[i + 1]) {
                b[count] = a[i];
                count++;
                i += 2;
            } else {
                i += 2;
            }
        }
        int[] c = Arrays.copyOfRange(b, 0, count);
        if (c.length > 2) {
            c = findCandidates(c);
        }
        return c;
    }

    /** Returns true iff x appears in more than half of the elements of a. */
    public static boolean isMajority(int x, int[] a) {
        int count = 0, n = a.length;
        for (int j : a) if (x == j) count++;
        return count > n / 2;
    }

    /** Asserts that the majority exists in a and is the same as expected. */
    public static void checkExpect(int expected, int[] a) {
        try {
            assert expected == majority(a);
        } catch (NoMajorityException ex) {
            assert false;
        }
    }

    /** Asserts that a has no majority element. */
    public static void checkException(int[] a) {
        try {
            majority(a);
            assert false;
        } catch (NoMajorityException ex) {
            assert true;
        }
    }

    /** Run some tests. */
    public static void main(String[] args) {
        checkExpect(4, new int[] {3, 3, 4, 2, 4, 4, 2, 4, 4});
        checkException(new int[] {3, 3, 4, 2, 4, 4, 2, 4});
        checkExpect(1, new int[] {1, 2, 1, 2, 1, 2, 1, 2, 1});
        checkExpect(2, new int[] {1, 2, 1, 2, 1, 2, 1, 2, 2});
        checkException(new int[] {1, 2, 1, 2, 1, 2, 1, 2, 3});
        checkExpect(1, new int[] {
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1,
        });
        checkException(new int[] {});
        checkExpect(5, new int[] {5});
        checkException(new int[] {1, 2});
        checkExpect(1, new int[] {1, 1});
        checkException(new int[] {1, 2, 3});
        checkExpect(1, new int[] {1, 1, 2});
        checkExpect(1, new int[] {1, 2, 1});
        checkExpect(1, new int[] {2, 1, 1});
        checkExpect(1, new int[] {1, 1, 1});
    }
}

class NoMajorityException extends RuntimeException {

    /** */
    private static final long serialVersionUID = -4216488571583646242L;
}

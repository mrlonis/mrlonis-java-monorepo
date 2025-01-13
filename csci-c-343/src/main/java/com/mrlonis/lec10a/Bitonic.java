package com.mrlonis.lec10a;

/**
 * This is a team challenge! You should work together with your teammates to write, test, and debug the program. Make
 * your code as efficient as possible. Pay attention to programming style.
 *
 * <p>In a comment at the top of the file, list the names of all the teammates and a brief description of what each
 * person contributed.
 *
 * <p>The person who will be Scribe during the next lecture is responsible for submitting the file. It is not necessary
 * to bring a printout to class.
 *
 * <p>At the end of the last lecture, we defined what is meant by a bitonic sequence. Write a predicate named
 * isBitonic() that takes an array of integers, a, and two indices, low and high. Your method should return true iff the
 * elements in the array between low and high, inclusive, form a bitonic sequence.
 *
 * <p>Write a main() method with comprehensive tests of your predicate.
 *
 * <p>Rubric lec10a: isBitonic
 *
 * <p>lec10a: isBitonic
 *
 * <p>Correctly implements the bitonic check in a single pass. Uses only O(1) additional space.
 *
 * <p>Thoroughly tests the edge cases (e.g., low == high, low > high, low + 1 == high, n == 1, n == 2) and tests on
 * large data.
 *
 * <p>Good coding style: formatting, variable names, clarity, comments.
 *
 * @author Matthew Lonis
 * @author Jacob Holdeman
 * @author Canrong Lin
 * @author Thomas Mackey
 * @author Timothy Marlett
 * @author Skylar Meyer
 */
public class Bitonic {

    public static boolean isBitonic(int[] a, int low, int high) {
        int n = a.length;

        /**
         * If low or high is greater than or equal to n (the length of the array a), or if low is greater than high, or
         * i low is less than 0, return false.
         */
        if (low >= n || high >= n || low > high || low < 0) {
            return false;
        }

        /**
         * If low equals high, return true. Since any sequence where the increasing or decreasing part is empty is
         * bitonic, if low equals high, or in other words, when only one number is considered, it will always be
         * bitonic.
         */
        if (low == high) {
            return true;
        }

        int i = low + 1;

        /**
         * Increasing loop to march down i as long as the number at i considered is greater than the previous number. If
         * the number at i is greater than the number at i - 1, the loop breaks.
         */
        increasing:
        while (i <= high) {
            if (!(a[i] > a[i - 1])) {
                break increasing;
            } else {
                i++;
            }
        }

        /**
         * If i is greater than or equal to high, the sequence is only increasing and is therefore bitonic since any
         * sequence with the decreasing part being empty is bitonic.
         */
        if (i >= high) {
            return true;
        }

        /**
         * Decreasing loop to march down i as long as each new number at i is less than the number at i - 1. If the
         * number at i is greater than or equal to the number at i - 1, the loop returns false since the sequence is not
         * bitonic.
         */
        decreasing:
        while (i <= high) {
            if (!(a[i] < a[i - 1])) {
                return false;
            } else {
                i++;
            }
        }

        /** If the program gets to this code the sequence by default must be bitonic and therefore returns true. */
        return true;
    }

    public static void main(String[] args) {
        /** n == 1 */
        int[] a = {1};
        assert isBitonic(a, 0, 0);
        assert !isBitonic(a, 0, 1);
        assert !isBitonic(a, 1, 0);

        /**
         * n == 2
         *
         * <p>Increasing Only
         */
        a = new int[] {1, 2};
        assert isBitonic(a, 0, 1);
        assert isBitonic(a, 0, 0);
        assert isBitonic(a, 1, 1);
        assert !isBitonic(a, 1, 0);

        /** Large Data Set */
        a = new int[] {1, 2, 3, 4, 5, 4, 3, 2, 1};
        assert isBitonic(a, 0, 1);
        assert isBitonic(a, 1, 2);
        assert isBitonic(a, 2, 3);
        assert isBitonic(a, 3, 4);
        assert isBitonic(a, 4, 5);
        assert isBitonic(a, 5, 6);
        assert isBitonic(a, 6, 7);
        assert isBitonic(a, 7, 8);
        assert isBitonic(a, 0, 8);
        assert !isBitonic(a, 0, 9);
        assert !isBitonic(a, -1, 8);

        /**
         * n == 2
         *
         * <p>Decreasing Only
         */
        a = new int[] {1, 0};
        assert isBitonic(a, 0, 1);
        assert isBitonic(a, 0, 0);
        assert !isBitonic(a, 1, 0);

        /** Large Data Set */
        a = new int[] {1, 2, 3, 4, 5, 4, 3, 2, 1, 5};
        assert isBitonic(a, 0, 1);
        assert isBitonic(a, 0, 8);
        assert !isBitonic(a, 0, 9);

        /** Largest Data Set */
        a = new int[] {
            3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5,
            4, 3, 2, 1, 0
        };

        System.out.println("All Tests Passed!");
    }
}

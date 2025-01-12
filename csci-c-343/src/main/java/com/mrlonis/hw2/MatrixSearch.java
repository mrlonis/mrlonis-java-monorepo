package com.mrlonis.hw2;

/**
 * We implement the search algorithm described in Problem 2.27 of Weiss.
 *
 * <p>In the comment at the top of the program, write a brief explanation to justify your running time.
 *
 * <p>Answer:
 *
 * <p>The runtime is O(N). This is because our while loop runs at most N times. The contents inside the while loop all
 * have a RunTime of O(1). 1 * N = N so O(N). The reason this is O(N) and not O(N^2) is because although yes i am
 * manipulating two variables to move throughout the array, i am not using a nested for loop. I also am not analyzing
 * every single cell in the array to search for the value i am looking for. Based on my algorithm, since i start in the
 * top right cell, and the value im looking for is in the bottom left cell, my movement throughout the matrix would
 * appear as a diagonal rather than a scan through the whole matrix.
 *
 * @author mrlonis
 */
public class MatrixSearch {

    /**
     * Returns true iff val is in the n x n array a. Assume that a is arranged so that the elements in every row are in
     * increasing order from left to right, and the elements in every col are in increasing order from top to bottom.
     * The worst-case running time of this method must be O(n).
     *
     * @param val The value that the algorithm uses to search for in an N x N matrix.
     * @param a The N x N Matrix used for the search.
     * @return Returns true iff the value exists t=in the array, otherwise false.
     */
    public static boolean contains(int val, int[][] a) {
        /*
         * Retrieves the length of the array in a matrix.
         * This works since we know the matrix is n x n.
         */
        int n = a.length;

        /*
         * Sets the matrix to the top right element.
         */
        int i = 0;
        int j = n - 1;

        while ((i < n) && (j >= 0)) {
            if (a[i][j] == val) {
                return true;
            }

            /*
             * This allows incrementing in a way that only uses O(N) extra space.
             *
             * If the top right is greater than the desired value that is being searched,
             * shift one cell to the left in the given row. Otherwise move down an entire row
             */
            if (a[i][j] > val) {
                j--;
            } else if (a[i][j] < val) {
                i++;
            }
        }
        return false;
    }

    /** Run some tests. */
    public static void main(String... args) {
        int[][] a;
        a = new int[][] {
            new int[] {
                2, 14, 26, 37, 43, 51,
            },
            new int[] {
                4, 16, 28, 38, 44, 54,
            },
            new int[] {
                6, 18, 30, 39, 45, 57,
            },
            new int[] {
                8, 20, 32, 40, 46, 60,
            },
            new int[] {
                10, 22, 34, 41, 47, 63,
            },
            new int[] {
                12, 24, 36, 42, 48, 66,
            },
        };
        for (int[] row : a) {
            for (int x : row) {
                assert contains(x, a);
            }
        }
        for (int x = 15; x <= 35; x += 2) {
            assert !contains(x, a);
        }
    }
}

package com.mrlonis.hw6;

/**
 * Problem 10.62(a): Let A be an n-by-n matrix of zeros and ones. A submatrix S of A is any group of contiguous entries
 * that forms a square. Design an O(n^2) algorithm that determines the size of the largest submatrix of ones in A.
 */
public class LargestSquareSubmatrix {

    /**
     * Returns the size (i.e., side length) of the largest square of ones found within the first n rows and first n
     * columns of mat. Assume n > 0 and n <= mat.length.
     */
    public static int largestSubSquare(int[][] mat, int n) {
        assert n > 0 && n <= mat.length;
        int returnVal = 0;
        int[][] cache = new int[n][n];

        /* Set first column of cache[][] */
        for (int i = 0; i < n; i++) {
            cache[i][0] = mat[i][0];
        }

        /* Set first row of cache[][] */
        for (int j = 0; j < n; j++) {
            cache[0][j] = mat[0][j];
        }

        /* Construct other entries of cache[][] */
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (mat[i][j] == 1) {
                    int temp = Math.min(cache[i][j - 1], cache[i - 1][j]);
                    temp = Math.min(temp, cache[i - 1][j - 1]);
                    temp += 1;
                    cache[i][j] = temp;
                } else {
                    cache[i][j] = 0;
                }

                if (returnVal < cache[i][j]) {
                    returnVal = cache[i][j];
                }
            }
        }

        return returnVal;
    }

    public static void main(String... args) {
        int[][] mat = {
            {1, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 1, 0},
            {0, 0, 1, 1, 1, 1, 1, 1},
            {0, 1, 0, 1, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 1, 0},
        };

        assert 1 == largestSubSquare(mat, 1);
        assert 1 == largestSubSquare(mat, 2);
        assert 1 == largestSubSquare(mat, 3);
        assert 2 == largestSubSquare(mat, 4);
        assert 3 == largestSubSquare(mat, 5);
        assert 3 == largestSubSquare(mat, 6);
        assert 3 == largestSubSquare(mat, 7);
        assert 4 == largestSubSquare(mat, 8);

        System.out.println("All Tests Pass!");
    }
}

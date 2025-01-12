package com.mrlonis.hw5;

/**
 * [hw5] Problem 3:
 *
 * <p>We implement a bottom-up solution to the Longest Increasing Subsequence problem, following the algorithm described
 * in lec9b.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class BottomUpDynamicProgramming {

    /**
     * Returns an array consisting of the longest increasing subsequence in a. Your solution must follow the code sketch
     * given in lecture.
     */
    public static int[] lis(int[] a) {
        int n = a.length;

        /*
         * Edge Case where a is the empty array. Returns a since it is already
         * empty and the lis array would be empty as well.
         */
        if (n == 0) {
            return a;
        }

        Result[] cache = new Result[n];
        cache[0] = new Result(1, -1);
        int max = 0;

        for (int i = 1; i < n; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (a[i] > a[j]) {

                    if (cache[i] == null) {
                        cache[i] = new Result(cache[j].score + 1, j);
                    } else if (cache[i].score <= cache[j].score) {
                        cache[i].score = cache[j].score + 1;
                        cache[i].parent = j;
                    } else if (cache[cache[i].parent].score == cache[j].score) {
                        cache[i].parent = j;
                    }

                    if (cache[max].score < cache[i].score) {
                        max = i;
                    }

                } else if (j == 0 && cache[i] == null) {
                    cache[i] = new Result(1, -1);
                }
            }
        }

        int sizeAns = cache[max].score;
        int[] ans = new int[sizeAns];

        while (max != -1) {
            ans[sizeAns - 1] = a[max];
            max = cache[max].parent;
            sizeAns--;
        }

        return ans;
    }

    /** Write a comprehensive battery of test cases. */
    public static void main(String... args) {
        int[] a, b;
        a = new int[] {5, 6, 1, 2, 9, 3, 4, 7, 4, 3};
        b = lis(a);
        assert 5 == b.length;
        assert 1 == b[0];
        assert 2 == b[1];
        assert 3 == b[2];
        assert 4 == b[3];
        assert 7 == b[4];

        a = new int[] {2, 1, 5, 3, 6, 4, 2, 7, 9, 11, 8, 10};
        b = lis(a);
        assert 6 == b.length;
        assert 2 == b[0];
        assert 5 == b[1];
        assert 6 == b[2];
        assert 7 == b[3];
        assert 9 == b[4];
        assert 11 == b[5];

        /*
         * Added tests
         */
        a = new int[] {1};
        b = lis(a);
        assert 1 == b.length;
        assert 1 == b[0];

        a = new int[] {};
        b = lis(a);
        assert 0 == b.length;
        assert b.equals(a);

        a = new int[] {2, 1};
        b = lis(a);
        assert 1 == b.length;
        assert 2 == b[0];

        a = new int[] {1, 2};
        b = lis(a);
        assert 2 == b.length;
        assert 1 == b[0];
        assert 2 == b[1];

        a = new int[] {1, 2, 6, 7, 3, 6, 3, 7, 9, 3, 1, 3, 0, 6, 2, 7, 8, 9, 10};
        b = lis(a);
        assert 8 == b.length;
        assert 1 == b[0];
        assert 2 == b[1];
        assert 3 == b[2];
        assert 6 == b[3];
        assert 7 == b[4];
        assert 8 == b[5];
        assert 9 == b[6];
        assert 10 == b[7];

        a = new int[10_000];
        for (int i = 0; i < 10_000; i++) {
            a[i] = i;
        }
        b = lis(a);
        assert 10_000 == b.length;
        for (int i = 0; i < 10_000; i++) {
            assert b[i] == i;
        }
        System.out.println("All tests passed...");
    }
}

/**
 * A subproblem is to compute the length of the longest increasing subsequence that ends with the i-th element in the
 * array. The result of solving such a subproblem is the score (i.e., the length of the sequence) and the parent (i.e.,
 * the index of element in the array that precedes the i-th one in the identified sequence).
 */
class Result {
    int score;
    int parent;

    Result(int score, int parent) {
        this.score = score;
        this.parent = parent;
    }

    public String toString() {
        return String.format("(%d,%d)", score, parent);
    }
}

package com.mrlonis.lec9a;

/**
 * TODO: Report the results of your time trials here. @TimeTrialResults
 *
 * <p>Time input of size 10: 1071.0 lif(a) == 3 Time input of size 100: 4638.0 lif(a) == 5 Time input of size 1000:
 * 29324.0 lif(a) == 6 Time input of size 10000: 335393.0 lif(a) == 6 Time input of size 100000: 2536825.0 lif(a) == 8
 * Time input of size 1000000: 3300584.0 lif(a) == 9 Time input of size 10000000: 3.3254952E7 lif(a) == 10
 *
 * <p>The Big-Oh running time of my algorithm is O(N). This is because my algorithm's biggest dominating factor is the
 * for loop inside my equation. This for loop runs at most N-1 times giving me a runtime of O(N). In some cases, where
 * the array is empty or only has 1 element, the runtime of my algorithm is a Constant time Operation O(1) since there
 * is few comparisons to be made. However, since Big-Oh is based On the worst case scenario, this leave me with O(N).
 *
 * <p>My time trials also show this affect. As the input size increases, The time increases in relative proportion. Once
 * the input size is 100+ each * 10 increase in input size increases the time by roughly a factor of 10, corresponding
 * to a relatively linear runtime.
 *
 * @author Matthew Lonis (mrlonis)
 */
import java.util.Random;

public class LongestIncreasingFragment {

    /**
     * Takes an array of positive integers and returns the length of the longest strictly increasing fragment (of
     * consecutive values).
     */
    public static int lif(int[] a) {
        int answer = 0;
        if (a.length > 0) {
            int temp = 1;
            for (int i = 1; i < a.length; i++) {
                if (a[i] > a[i - 1]) {
                    temp++;
                } else {
                    if (temp > answer) {
                        answer = temp;
                    }
                    temp = 1;
                }
            }
            if (temp > answer) {
                answer = temp;
            }
        }
        return answer;
    }

    public static void main(String[] args) {
        int[] a;
        a = new int[] {};
        assert lif(a) == 0;
        a = new int[] {5};
        assert lif(a) == 1;
        a = new int[] {1, 2, 3, 4, 5, 6};
        assert lif(a) == 6;
        a = new int[] {6, 5, 4, 3, 2, 1};
        assert lif(a) == 1;
        a = new int[] {5, 1, 5, 2, 3, 4};
        assert lif(a) == 3;
        a = new int[] {3, 2, 4, 6, 7, 2, 9, 1};
        assert lif(a) == 4;

        /** Added Tests */
        a = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assert lif(a) == 10;
        a = new int[] {10, 9, 7, 3, 4, 8, 12, 34};
        assert lif(a) == 5;
        a = new int[] {10, 4, 3, 2, 0, 5, 4, 6, 8, 3};
        assert lif(a) == 3;
        a = new int[] {54, 3, 6, 76, 56, 875, 7, 98, 234};
        assert lif(a) == 3;
        a = new int[] {0, 1, 2, 0, 1, 2, 6, 7, 3, 4, 5, 8, 9, 6, 4, 654, 45};
        assert lif(a) == 5;

        /** Time trials. Filling array with random integers. */
        Random num = new Random();
        for (int i = 10; i <= 10_000_000; i *= 10) {
            a = new int[i];
            for (int j = 0; j < i; j++) {
                a[j] = num.nextInt(i);
            }

            double start = System.nanoTime();
            int answer = lif(a);
            double end = System.nanoTime();
            System.out.println("Time input of size " + i + ": " + (end - start) + " lif(a) == " + answer);
        }
    }
}

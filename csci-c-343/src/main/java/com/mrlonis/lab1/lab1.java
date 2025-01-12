package com.mrlonis.lab1;

public class lab1 {

    /*
     * Implement a static Java method named search that takes two arrays of integers, A and B, where A has m
     * elements and B has n elements. You may assume that m <= n. The method should search for the first
     * (i.e., leftmost) occurrence of A as a subarray in B. If found, then return the starting index of A in B.
     * Otherwise, return -1.
     */

    public static int search(int[] a, int[] b) {
        /*
         * Local Variables for this method
         */
        int m = a.length;
        int n = b.length;
        int i = 0;
        boolean found;

        /*
         * Catches any potential empty array inputs. If they're empty the following exception is thrown
         */
        if (m == 0 || n == 0) {
            throw new RuntimeException("Empty array detected");
        }

        /*
         * This loop steps through each element in array b
         * If the element is equal to the first index in a (a[0]) then...
         */
        for (i = 0; i < n; i++) {
            if (a[0] == b[i]) {
                /*
                 * Found is set to true by default because the next loop will step through each element of
                 * a and b to check if the entire array of a exists in b
                 */
                found = true;
                for (int j = 1; j < m; j++) {
                    /*
                     * If one of the elements doesn't match up before j < m turns to false,
                     * found is set to false and we break out of the for loop
                     */
                    if (a[j] != b[i + j]) {
                        found = false;
                        break;
                    }
                }
                /*
                 * Then, if found is true we return the index (i) where the element is located in b
                 */
                if (found) {
                    return i;
                }
            }
        }
        /*
         * If the array a doesn't exist in b we return our default value of -1
         */
        return -1;
    }

    /*
     * Simple testing for this method in out main
     */
    public static void main(String[] args) {
        /*
         * Local variables for main
         */
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] c = {6, 7};
        int[] d = {1, 2, 6};

        /*
         * Tests with asserts
         */
        assert (-1 == search(d, b));
        assert (5 == search(c, b));
        assert (0 == search(a, b));
    }
}

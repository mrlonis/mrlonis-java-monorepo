package com.mrlonis.lab2;

/**
 * The following table shows the first ten numbers in the Fibonacci sequence:
 *
 * <p>n : 0 1 2 3 4 5 6 7 8 9 ... Fib(n) : 1 1 2 3 5 8 13 21 34 55 ...
 *
 * @author Matthew Lonis
 */
public class Fibonacci {
    /** TODO: Implement the recursive definition directly. */
    public static int fib1(int n) {
        if ((n == 0) || (n == 1)) {
            return 1;
        } else {
            return fib1(n - 1) + fib1(n - 2);
        }
    }

    /** TODO: Implement recursively by calling a tail-recursive helper. */
    public static int fib2(int n) {
        if ((n == 0) || (n == 1)) {
            return 1;
        } else {
            return fibTail(n, 2, 1, 2);
        }
    }

    public static int fibTail(int n, int count, int previousNum, int currentNum) {
        if (n == count) {
            return currentNum;
        } else {
            return fibTail(n, (count + 1), currentNum, (previousNum + currentNum));
        }
    }

    /** TODO: Run this class as an application. */
    public static void main(String... args) {
        assert fib1(9) == 55;
        assert fib2(9) == 55;
    }
}

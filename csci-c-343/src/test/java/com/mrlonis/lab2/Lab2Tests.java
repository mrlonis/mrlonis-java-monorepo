package com.mrlonis.lab2;

import static com.mrlonis.lab2.Fibonacci.fib1;
import static com.mrlonis.lab2.Fibonacci.fib2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class Lab2Tests {
    /** TODO: Run this class as a JUnit test. Add additional tests to the following methods. */
    @Test
    void testFib1() {
        assertEquals(1, fib1(0));
        assertEquals(1, fib1(1));
        assertEquals(2, fib1(2));
        assertEquals(3, fib1(3));
        assertEquals(5, fib1(4));
        assertEquals(8, fib1(5));
        assertEquals(13, fib1(6));
        assertEquals(21, fib1(7));
        assertEquals(34, fib1(8));
        assertEquals(55, fib1(9));
    }

    @Test
    void testFib2() {
        assertEquals(1, fib2(0));
        assertEquals(1, fib2(1));
        assertEquals(2, fib2(2));
        assertEquals(3, fib2(3));
        assertEquals(5, fib2(4));
        assertEquals(8, fib2(5));
        assertEquals(13, fib2(6));
        assertEquals(21, fib2(7));
        assertEquals(34, fib2(8));
        assertEquals(55, fib2(9));
    }
}

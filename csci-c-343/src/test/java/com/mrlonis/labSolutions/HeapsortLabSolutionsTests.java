package com.mrlonis.labSolutions;

import static com.mrlonis.labSolutions.Heapsort.getLeft;
import static com.mrlonis.labSolutions.Heapsort.getParent;
import static com.mrlonis.labSolutions.Heapsort.getRight;
import static com.mrlonis.labSolutions.Heapsort.heapify;
import static com.mrlonis.labSolutions.Heapsort.isSorted;
import static com.mrlonis.labSolutions.Heapsort.siftDown;
import static com.mrlonis.labSolutions.Heapsort.sort;
import static com.mrlonis.labSolutions.Heapsort.swap;
import static com.mrlonis.labSolutions.Heapsort.verifyOrderingProperty;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class HeapsortLabSolutionsTests {
    @Test
    void gettersTest() {
        /** 0 / \ 1 2 / \ / 3 4 5 */
        assertEquals(1, getLeft(0));
        assertEquals(3, getLeft(1));
        assertEquals(5, getLeft(2));
        assertEquals(2, getRight(0));
        assertEquals(4, getRight(1));
        assertEquals(0, getParent(1));
        assertEquals(0, getParent(2));
        assertEquals(1, getParent(3));
        assertEquals(1, getParent(4));
        assertEquals(2, getParent(5));
    }

    @Test
    void swapTest() {
        int[] a = new int[] {5, 9, 8, 2, 0, 3};
        swap(a, 0, 1);
        assertArrayEquals(new int[] {9, 5, 8, 2, 0, 3}, a);
        swap(a, 5, 2);
        assertArrayEquals(new int[] {9, 5, 3, 2, 0, 8}, a);
        swap(a, 3, 3);
        assertArrayEquals(new int[] {9, 5, 3, 2, 0, 8}, a);
        swap(a, a.length - 1, 0);
        assertArrayEquals(new int[] {8, 5, 3, 2, 0, 9}, a);
    }

    @Test
    void siftDownTest() {
        int[] a = new int[] {1, 2};
        siftDown(a, 0, 2);
        assertArrayEquals(new int[] {2, 1}, a);
        a = new int[] {5};
        siftDown(a, 0, 1);
        assertArrayEquals(new int[] {5}, a);
        a = new int[] {1, 2};
        siftDown(a, 0, 1);
        assertArrayEquals(new int[] {1, 2}, a);
        a = new int[] {1, 2, 3};
        siftDown(a, 0, 3);
        assertArrayEquals(new int[] {3, 2, 1}, a);
        a = new int[] {1, 9, 8, 7, 6, 5, 4, 3, 2};
        siftDown(a, 0, 9);
        assertArrayEquals(new int[] {9, 7, 8, 3, 6, 5, 4, 1, 2}, a);
        int[] heap = new int[] {25, 23, 19, 10, 18, 15, 17, 7, 5, 13, 9, 8, 14, 1, 4, 3, 6, 2};
        int n = heap.length;
        assertTrue(verifyOrderingProperty(heap));
        heap[0] = 20;
        siftDown(heap, 0, n);
        assertArrayEquals(new int[] {23, 20, 19, 10, 18, 15, 17, 7, 5, 13, 9, 8, 14, 1, 4, 3, 6, 2}, heap);
        assertTrue(verifyOrderingProperty(heap));
    }

    @Test
    void smallHeapify() {
        int[] a = new int[] {0, 3, 9, 4, 8, 2, 5};
        heapify(a);
        assertArrayEquals(new int[] {9, 8, 5, 4, 3, 2, 0}, a);
    }

    @Test
    void bigHeapify() {
        int[] a = new int[10_000];
        java.util.List<Integer> ints = new LinkedList<>();
        for (int i = 0; i < a.length; i++) ints.add(i);
        Random gen = new Random();
        for (int i = 0; i < a.length; i++) a[i] = ints.remove(gen.nextInt(ints.size()));
        heapify(a);
        assertTrue(verifyOrderingProperty(a));
    }

    @Test
    void sortTest() {
        int[] a = new int[] {0, 3, 9, 4, 8, 2, 5};
        sort(a);
        assertArrayEquals(new int[] {0, 2, 3, 4, 5, 8, 9}, a);
        a = new int[10_000];
        List<Integer> ints = new LinkedList<>();
        for (int i = 0; i < a.length; i++) ints.add(i);
        Random gen = new Random();
        for (int i = 0; i < a.length; i++) a[i] = ints.remove(gen.nextInt(ints.size()));
        sort(a);
        assertTrue(isSorted(a));
    }
}

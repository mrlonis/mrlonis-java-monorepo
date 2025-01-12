package com.mrlonis.lab7;

import lombok.experimental.UtilityClass;

/**
 * @author mrlonis (Matthew Lonis)
 * @partner Christian Hutslar
 */
@UtilityClass
public class Heapsort {

    /** Sorts the elements of a, in place, in their natural order. */
    public static void sort(int[] a) {
        heapify(a);
        int n = a.length;
        while (n > 0) {
            swap(a, 0, n - 1);
            n--;
            siftDown(a, 0, n);
        }
    }

    /** Rearranges the elements of a so that they form a heap. */
    public static void heapify(int[] a) {
        int n = a.length;
        int last = getParent(n - 1);
        for (int i = last; i >= 0; i--) {
            siftDown(a, i, n);
        }
    }

    /** Restores the ordering property at node p so that the first n elements of a form a heap. */
    public static void siftDown(int[] a, int p, int n) {
        int leftChild = getLeft(p);
        if (leftChild < n) {
            int maxChild = leftChild;
            int rightChild = getRight(p);

            if (rightChild < n && a[rightChild] > a[leftChild]) {
                maxChild = rightChild;
            }

            if (a[maxChild] > a[p]) {
                swap(a, maxChild, p);
                siftDown(a, maxChild, n);
            }
        }
    }

    /** Exchanges the elements at indices i and j in the array a. */
    public static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /** Returns a logical pointer to the left child of node p. */
    public static int getLeft(int p) {
        return (p * 2) + 1;
    }

    /** Returns a logical pointer to the right child of node p. */
    public static int getRight(int p) {
        return (p * 2) + 2;
    }

    /** Returns a logical pointer to the parent of node p. */
    public static int getParent(int p) {
        return (p - 1) / 2;
    }

    public static boolean verifyOrderingProperty(int[] heap) {
        int n = heap.length;
        int last = getParent(n - 1);
        for (int p = last; p >= 0; p--) {
            int leftChild = getLeft(p);
            if (heap[p] < heap[leftChild]) return false;
            int rightChild = leftChild + 1;
            if (rightChild < n && heap[p] < heap[rightChild]) return false;
        }
        return true;
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }
}

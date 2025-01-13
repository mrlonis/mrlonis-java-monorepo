package com.mrlonis.hw5;

import java.util.Comparator;
import java.util.Iterator;

/**
 * [hw5] Problem 1:
 *
 * <p>Add a sort() method, based on the Quicksort Algorithm, to the DoublyLinkedList implementation.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class DoublyLinkedList<T> implements List<T> {

    /** Node is a pair containing a data field and a pointers to the previous and next nodes in the list. */
    class Node {
        T data;
        Node next, prev;

        Node(T data) {
            this(data, null, null);
        }

        Node(T data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    /** Always points to the headnode for this list */
    protected Node head;

    /** The number of nodes in this list, initially 0 */
    protected int n;

    /** Creates the empty list. */
    public DoublyLinkedList() {
        head = new Node(null);
        head.next = head.prev = head;
    }

    /**
     * Rearranges the elements of this list so they are in order according to the supplied Comparator by using the
     * Quicksort Algorithm. Your implementation must run in O(n log n) time on the average.
     */
    public void sort(Comparator<T> comp) {
        quicksortHelper(comp, head.next, head.prev);
    }

    /**
     * Sorts the elements in the list between low and high, inclusive, in the order specified by the given comparator.
     */
    private void quicksortHelper(Comparator<T> comp, Node low, Node high) {
        if (low != head && high != head && low != high && low.prev != high) {
            Node temp = partition(comp, low, high);

            quicksortHelper(comp, low, temp.prev);
            quicksortHelper(comp, temp.next, high);
        }
    }

    /**
     * Partitions the elements in the list about the pivot (which is the data in the low node), and then returns the
     * node containing the pivot. Note that you should move the data around rather than trying to adjust the links in
     * the nodes.
     */
    private Node partition(Comparator<T> comp, Node low, Node high) {
        T pivot = low.data;
        Node free = low;
        low = low.next;

        outer:
        while (true) {
            while (comp.compare(high.data, pivot) > 0) {
                high = high.prev;
                if (high.next == low) {
                    break outer;
                }
            }

            this.swapData(high, free);
            free = high;
            high = high.prev;

            if (high.next == low) {
                break outer;
            }

            while (comp.compare(low.data, pivot) < 0) {
                low = low.next;
                if (low.prev == high) {
                    break outer;
                }
            }

            this.swapData(low, free);
            free = low;
            low = low.next;

            if (low.prev == high) {
                break outer;
            }
        }
        free.data = pivot;
        return free;
    }

    /**
     * Swaps the data between the two given nodes.
     *
     * @param i The first Node.
     * @param j The second node.
     */
    public void swapData(Node i, Node j) {
        T temp = i.data;
        i.data = j.data;
        j.data = temp;
        return;
    }

    /** Inserts the value x at the end of this list. */
    public void add(T x) {
        n++;
        Node last = head.prev;
        Node curr = new Node(x, last, head);
        last.next = curr;
        head.prev = curr;
    }

    /** Inserts the value x at the front of this list. */
    public void addFront(T x) {
        n++;
        Node curr = new Node(x, head, head.next);
        head.next.prev = curr;
        head.next = curr;
    }

    /**
     * Removes the element at index i from this list.
     *
     * @return the data in the removed node.
     * @throw IndexOutOfBoundsException iff i is out of range for this list.
     */
    public T remove(int i) {
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException();
        Node p = head.next;
        while (i-- > 0) p = p.next;
        remove(p);
        return p.data;
    }

    /**
     * Removes the node pointed to by p. This helper is called by the list's remove() and by the iterator's remove().
     */
    private void remove(Node p) {
        n--;
        p.prev.next = p.next;
        p.next.prev = p.prev;
    }

    /**
     * Returns the i-th element from this list, where i is a zero-based index.
     *
     * @throw IndexOutOfBoundsException iff i is out of range for this list.
     */
    public T get(int i) {
        if (i < 0 || i >= size()) throw new IndexOutOfBoundsException();
        Node p = head.next;
        while (i > 0) {
            p = p.next;
            i--;
        }
        return p.data;
    }

    /** Returns true iff the value x appears somewhere in this list. */
    public boolean contains(T x) {
        Node p = head.next;
        while (p != head)
            if (p.data.equals(x)) return true;
            else p = p.next;
        return false;
    }

    /** Returns the number of elements in this list. */
    public int size() {
        return n;
    }

    /** Returns an iterator for this list. */
    public Iterator<T> iterator() {
        return new Iterator<>() {
            Node p = head.next;

            public boolean hasNext() {
                return p != head;
            }

            public T next() {
                T ans = p.data;
                p = p.next;
                return ans;
            }

            public void remove() {
                DoublyLinkedList.this.remove(p.prev);
            }
        };
    }

    /** Returns a string representing this list (resembling a Racket list). */
    public String toString() {
        if (isEmpty()) return "()";
        Iterator<T> it = iterator();
        StringBuilder ans = new StringBuilder("(").append(it.next());
        while (it.hasNext()) ans.append(" ").append(it.next());
        return ans.append(")").toString();
    }

    /** Write a comprehensive battery of test cases. */
    public static void main(String... args) {
        DoublyLinkedList<Integer> xs = new DoublyLinkedList<>();
        int[] a = new int[] {4, 3, 0, 6, 5, 7, 2, 8, 1};
        for (int x : a) {
            xs.add(x);
        }
        // Sort xs in the natural order:
        xs.sort((x, y) -> x.compareTo(y));
        for (int i = 0; i < xs.size(); i++) {
            assert i == xs.get(i);
        }
        // Sort xs in the reverse of the natural order:
        xs.sort((x, y) -> y.compareTo(x));
        for (int i = 0; i < xs.size(); i++) {
            assert xs.size() - 1 - i == xs.get(i);
        }

        /*
         * Added Tests.
         *
         * Tests On Large Data.
         */
        DoublyLinkedList<Integer> ys = new DoublyLinkedList<>();
        int[] b = new int[10_000];
        for (int i = 0; i < 10_000; i++) {
            b[i] = i;
        }
        for (int x : b) {
            ys.add(x);
        }
        ys.sort((x, y) -> y.compareTo(x));
        for (int i = 0, j = ys.size() - 1; i < ys.size(); i++, j--) {
            assert j == ys.get(i);
        }
        ys.sort((x, y) -> x.compareTo(y));
        for (int i = 0; i < ys.size(); i++) {
            assert i == ys.get(i);
        }

        DoublyLinkedList<Integer> zs = new DoublyLinkedList<>();
        int[] c = new int[] {6, 3, 4};
        for (int x : c) {
            zs.add(x);
        }
        assert zs.get(0) == 6;
        assert zs.get(1) == 3;
        assert zs.get(2) == 4;
        zs.sort((x, y) -> x.compareTo(y));
        assert zs.get(0) == 3;
        assert zs.get(1) == 4;
        assert zs.get(2) == 6;
        zs.sort((x, y) -> y.compareTo(x));
        assert zs.get(0) == 6;
        assert zs.get(1) == 4;
        assert zs.get(2) == 3;

        DoublyLinkedList<Integer> ts = new DoublyLinkedList<>();
        int[] d = new int[] {1};
        for (int x : d) {
            ts.add(x);
        }
        assert ts.get(0) == 1;
        ts.sort((x, y) -> x.compareTo(y));
        assert ts.get(0) == 1;
        ts.sort((x, y) -> y.compareTo(x));
        assert ts.get(0) == 1;

        DoublyLinkedList<Integer> ss = new DoublyLinkedList<>();
        int[] e = new int[] {1, 2};
        for (int x : e) {
            ss.add(x);
        }
        assert ss.get(0) == 1;
        assert ss.get(1) == 2;
        ss.sort((x, y) -> x.compareTo(y));
        assert ss.get(0) == 1;
        assert ss.get(1) == 2;
        ss.sort((x, y) -> y.compareTo(x));
        assert ss.get(0) == 2;
        assert ss.get(1) == 1;
        System.out.println("All tests passed...");
    }
}

/** If you want to call yourself a List, then implement this interface: */
interface List<T> extends Iterable<T> {
    /** simple add */
    void add(T x);

    T remove(int i);

    T get(int i);

    boolean contains(T x);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}

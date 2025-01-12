package com.mrlonis.hw3;

import java.util.Iterator;

/**
 * This is my implementation of deque.
 *
 * <p>A deque is a data structure consisting of a list of items, on which the following operations are possible:
 * push(x): Insert itemx on the front end of the deque. pop(): Remove the front item from the deque and return it.
 * inject(x): Insert itemx on the rear end of the deque. eject(): Remove the rear item from the deque and return it.
 *
 * @author Matthew Lonis
 * @param <T> Any element type.
 */
public class DequeList<T> implements Deque<T> {
    /** The DoublyLinkedList data member for this class. */
    private DoublyLinkedList<T> dll;

    /** Default constructor to set up dll. */
    public DequeList() {
        dll = new DoublyLinkedList<>();
    }

    /** Insert item x on the front end of the Deque. */
    public void push(T x) {
        dll.addFirst(x);
    }

    /** Remove the front item from the Deque and return it. */
    public T pop() {
        return dll.remove(0);
    }

    /** Insert item x on the rear end of the Deque. */
    public void inject(T x) {
        dll.add(x);
    }

    /** Remove the rear item from the Deque and return it. */
    public T eject() {
        return dll.remove(size() - 1);
    }

    /**
     * Returns true iff the value x appears somewhere in this deque.
     *
     * @param x The item to search if this deque contains.
     * @return true iff the value x appears somewhere in this deque.
     */
    public boolean contains(T x) {
        return dll.contains(x);
    }

    /**
     * Returns an Iterator for this deque.
     *
     * @return An Iterator for this deque.
     */
    public Iterator<T> iterator() {
        return dll.iterator();
    }

    /**
     * Return the size of this deque.
     *
     * @return The size of this deque.
     */
    public int size() {
        return dll.size();
    }

    /**
     * Returns a String representation of this deque.
     *
     * @return A String representation of this deque.
     */
    public String toString() {
        return dll.toString();
    }

    /**
     * Testing for this class.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Integer Testing...");
        DequeList<Integer> xs = new DequeList<>();
        int[] a = new int[] {4, 3, 6, 5, 7, 8};
        for (int x : a) {
            xs.push(x);
        }
        Iterator<Integer> itA = xs.iterator();
        int i = 0;
        while (itA.hasNext()) {
            assert (xs.contains(itA.next()));
            i++;
        }
        assert i == a.length;
        assert 6 == xs.size();
        assert "(8 7 5 6 3 4)".equals(xs.toString());
        xs.pop();
        assert "(7 5 6 3 4)".equals(xs.toString());
        for (int x : a) {
            xs.inject(x);
        }
        assert "(7 5 6 3 4 4 3 6 5 7 8)".equals(xs.toString());
        xs.eject();
        xs.eject();
        assert "(7 5 6 3 4 4 3 6 5)".equals(xs.toString());
        System.out.println("...Integer Tests Passed!");

        System.out.println("String Testing...");
        DequeList<String> xsS = new DequeList<>();
        String[] b = new String[] {"Alpha", "Bravo", "Charlie", "Delta", "Epsilon", "Foxtrot"};
        for (String x : b) {
            xsS.push(x);
        }
        Iterator<String> itB = xsS.iterator();
        i = 0;
        while (itB.hasNext()) {
            assert (xsS.contains(itB.next()));
            i++;
        }
        assert i == b.length;
        assert 6 == xsS.size();
        assert "(Foxtrot Epsilon Delta Charlie Bravo Alpha)".equals(xsS.toString());
        xsS.pop();
        assert "(Epsilon Delta Charlie Bravo Alpha)".equals(xsS.toString());
        for (String x : b) {
            xsS.inject(x);
        }
        assert "(Epsilon Delta Charlie Bravo Alpha Alpha Bravo Charlie Delta Epsilon Foxtrot)".equals(xsS.toString());
        xsS.eject();
        xsS.eject();
        assert "(Epsilon Delta Charlie Bravo Alpha Alpha Bravo Charlie Delta)".equals(xsS.toString());
        System.out.println("...String Tests Passed!");

        System.out.println("All Tests Passed!");
    }
}

interface Deque<T> extends Iterable<T> {
    /** Insert item x on the front end of the Deque. */
    void push(T x);

    /** Remove the front item from the Deque and return it. */
    T pop();

    /** Insert item x on the rear end of the Deque. */
    void inject(T x);

    /** Remove the rear item from the Deque and return it. */
    T eject();

    /** Iterator for this Deque. */
    Iterator<T> iterator();
}

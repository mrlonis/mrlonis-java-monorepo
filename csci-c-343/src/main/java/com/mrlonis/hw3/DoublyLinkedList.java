package com.mrlonis.hw3;

/**
 * hw3: Problem 1 starter code.
 *
 * <p>Hallmarks of a DoublyLinkedList: - headnode (also called a dummy node) - backward pointers to the previous node in
 * the list - circularity: last node points forward to the headnode and the headnode points backward to the last node
 *
 * @author Matthew Lonis
 */
import java.util.Iterator;

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

    Node head; // always points to the headnode for this list
    int n; // the number of nodes in this list, initially 0

    /** Creates the empty list. */
    public DoublyLinkedList() {
        head = new Node(null);
        head.prev = head;
        head.next = head;
    }

    /**
     * Inserts the value x at the end of this list.
     *
     * @param x The item being added to the end of this list.
     */
    public void add(T x) {
        n++;
        Node last = head.prev;
        Node curr = new Node(x, last, head);
        last.next = curr;
        head.prev = curr;
    }

    /**
     * Inserts the value x at the front of this list. To be used in Deque.java.
     *
     * @param x The item to be added to the front of this list.
     */
    public void addFirst(T x) {
        n++;
        Node first = head.next;
        Node curr = new Node(x, head, first);
        first.prev = curr;
        head.next = curr;
    }

    /**
     * Removes the element at index i from this list.
     *
     * @param i The index for the element to be removed.
     * @return the data in the removed node.
     * @throw IndexOutOfBoundsException iff i is out of range for this list.
     */
    public T remove(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }

        n--;
        T data;

        /*
         * Quickly removes the last element from the list. Used for Deque.java.
         */
        if (i == size()) {
            Node last = head.prev;
            data = last.data;
            last.prev.next = head;
            head.prev = last.prev;
            return data;
        }

        Node curr = head.next;

        while (i > 0) {
            curr = curr.next;
            i--;
        }

        data = curr.data;

        Node prev = curr.prev;
        Node next = curr.next;

        prev.next = next;
        next.prev = prev;

        return data;
    }

    /**
     * Returns the i-th element from this list, where i is a zero-based index.
     *
     * @param i The index of the element to return.
     * @return The element at index i.
     * @throw IndexOutOfBoundsException iff i is out of range for this list.
     */
    public T get(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }

        T data;
        Node curr = head.next;

        int count = i;

        while (count > 0) {
            curr = curr.next;
            count--;
        }

        data = curr.data;

        return data;
    }

    /**
     * Returns true iff the value x appears somewhere in this list.
     *
     * @param x The item to search if this list contains.
     * @return true iff the value x appears somewhere in this list.
     */
    public boolean contains(T x) {
        Node curr = head.next;

        while (curr != head) {
            if (curr.data.equals(x)) {
                return true;
            }
            curr = curr.next;
        }

        return false;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return The number of elements in this list.
     */
    public int size() {
        return n;
    }

    /**
     * Returns an iterator for this list.
     *
     * @return An Iterator for this list.
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node p = head.next;

            /**
             * Returns true iff the iterator has a next element in the sequence.
             *
             * @return true iff the iterator has a next element in the sequence.
             */
            public boolean hasNext() {
                return p != head;
            }

            /**
             * Returns the element in at the current node in the iterator and advances to the next element.
             *
             * @return The current element in the iterator.
             */
            public T next() {
                T ans = p.data;
                p = p.next;
                return ans;
            }

            /** Removes the last element returned by the iterator. */
            public void remove() {
                n--;
                p.prev.prev.next = p;
                p.prev = p.prev.prev;
            }
        };
    }

    /**
     * Returns a string representing this list (resembling a Racket list).
     *
     * @return A string representation of this list.
     */
    public String toString() {
        if (isEmpty()) {
            return "()";
        }

        Iterator<T> it = iterator();
        StringBuilder ans = new StringBuilder("(").append(it.next());

        while (it.hasNext()) {
            ans.append(" ").append(it.next());
        }

        return ans.append(")").toString();
    }

    /** Simple testing to get you started. Add more tests of your own! */
    public static void main(String... args) {
        /*
         * Integer Testing
         */
        System.out.println("Integer Testing...");
        DoublyLinkedList<Integer> xs = new DoublyLinkedList<>();
        int[] a = new int[] {4, 3, 6, 5, 7, 8};
        for (int x : a) {
            xs.add(x);
        }
        assert 6 == xs.size();
        for (int i = 0; i < a.length; i++) {
            assert xs.get(i) == a[i];
        }
        assert !xs.contains(null);
        for (int x : a) {
            assert xs.contains(x);
        }
        assert "(4 3 6 5 7 8)".equals(xs.toString());
        assert xs.remove(0) == 4;
        assert xs.remove(1) == 6;
        assert 4 == xs.size();
        assert "(3 5 7 8)".equals(xs.toString());
        while (!xs.isEmpty()) {
            xs.remove(xs.size() - 1);
        }
        assert 0 == xs.size();
        for (int x : a) {
            xs.add(x);
        }
        for (int x : xs) {
            assert xs.contains(x);
        }
        Iterator<Integer> it = xs.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) {
                it.remove();
            }
        }
        assert 3 == xs.size();
        assert "(3 5 7)".equals(xs.toString());
        System.out.println("...Integer Tests Passed!");

        /*
         * String Testing
         */
        System.out.println("String Testing...");
        DoublyLinkedList<String> xsS = new DoublyLinkedList<>();
        String[] b = new String[] {"Matthew", "Chris", "Steven", "Jesus", "Loki", "Thor"};
        for (String x : b) {
            xsS.add(x);
        }
        assert 6 == xsS.size();
        for (int i = 0; i < b.length; i++) {
            assert xsS.get(i).equals(b[i]);
        }
        assert !xsS.contains(null);
        for (String x : b) {
            assert xsS.contains(x);
        }
        assert "(Matthew Chris Steven Jesus Loki Thor)".equals(xsS.toString());
        assert "Matthew".equals(xsS.remove(0));
        assert "Steven".equals(xsS.remove(1));
        assert 4 == xsS.size();
        assert "(Chris Jesus Loki Thor)".equals(xsS.toString());
        while (!xsS.isEmpty()) {
            xsS.remove(xsS.size() - 1);
        }
        assert 0 == xsS.size();
        for (String x : b) {
            xsS.add(x);
        }
        for (String x : xsS) {
            assert xsS.contains(x);
        }
        Iterator<String> itS = xsS.iterator();
        while (itS.hasNext()) {
            if (itS.next().length() > 5) {
                itS.remove();
            }
        }
        assert 4 == xsS.size();
        assert "(Chris Jesus Loki Thor)".equals(xsS.toString());
        System.out.println("...String Tests Passed!");

        System.out.println("All tests passed...");
    }
}

/** If you want to call yourself a List, then implement this interface: */
interface List<T> extends Iterable<T> {
    void add(T x); // simple add

    T remove(int i);

    T get(int i);

    boolean contains(T x);

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}

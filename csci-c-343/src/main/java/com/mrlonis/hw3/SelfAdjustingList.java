package com.mrlonis.hw3;

/**
 * hw3: Problem 5 starter code.
 *
 * @author Matthew Lonis
 */
public class SelfAdjustingList<T> extends DoublyLinkedList<T> {

    /** Default class constructor. Calls super(). */
    public SelfAdjustingList() {
        super();
    }

    /**
     * Adds elements to the front of this list. Overrides the add method in the super class DoublyLinkedList<T>.
     *
     * @param x The element to be added to the front of this list.
     */
    public void add(T x) {
        /*
         * Original Implementation before i Created the addFirst()
         * method in DoublyLinkedList.java.
         *
         * n++;
         * Node first = head.next;
         * Node curr = new Node(x, head, first);
         * head.next = curr;
         * first.prev = curr;
         */
        super.addFirst(x);
    }

    /**
     * Finds the element at index i, returns it, and also moves it to the front of this list.
     *
     * @param i The index of the element to be found.
     * @return The element found at index i.
     */
    public T find(int i) {
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

        if (i == 0) {
            return data;
        }

        Node prev = curr.prev;
        Node next = curr.next;
        prev.next = next;
        next.prev = prev;
        head.next.prev = curr;
        curr.next = head.next;
        head.next = curr;

        return data;
    }

    /** Simple testing to get you started. Add more tests of your own! */
    public static void main(String... args) {
        /*
         * Integer Testing.
         */
        System.out.println("Integer Testing...");
        SelfAdjustingList<Integer> xs = new SelfAdjustingList<>();
        for (int x = 1; x <= 10; x++) xs.add(x);
        for (int i = 0; i < xs.size(); i++) assert 10 - i == xs.get(i);
        for (int i = 0; i < xs.size(); i++) {
            int x = xs.get(i);
            assert x == xs.find(i);
        }
        for (int i = 0; i < xs.size(); i++) {
            int x = xs.find(i);
            assert x == xs.get(0);
        }
        System.out.println("...Integer Tests Passed!");

        /*
         * String Testing.
         */
        System.out.println("String Testing...");
        SelfAdjustingList<String> xsS = new SelfAdjustingList<>();
        String[] b = new String[] {"Matthew", "Chris", "Steven", "Jesus", "Loki", "Thor"};
        for (String x : b) {
            xsS.add(x);
        }
        for (int i = 0; i < xsS.size(); i++) {
            String x = xsS.get(i);
            assert x.equals(xsS.find(i));
        }
        for (int i = 0; i < xsS.size(); i++) {
            String x = xsS.find(i);
            assert x.equals(xsS.get(0));
        }
        System.out.println("...String Tests Passed!");

        System.out.println("All tests passed...");
    }
}

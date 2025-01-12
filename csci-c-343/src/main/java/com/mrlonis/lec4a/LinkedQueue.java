package com.mrlonis.lec4a;

/**
 * Here is our interface Queue<E> and LinkedQueue<E> Class for lec4a.
 *
 * <p>We affirm that all members of our team contributed to this solution.
 *
 * @author green1
 * @author Matthew Lonis
 */

/**
 * The LinkedQueue<E> class which is implemented based of a SinglyLinkedList<E> and implements the generic interface
 * Queue<E>.
 *
 * @param <E> Any Element in Java.
 */
public class LinkedQueue<E> implements Queue<E> {
    /** Represents the SinglyLinkedList<E> used for our LinkedQueue<E>. */
    List<E> lls = new SinglyLinkedList<>();

    /**
     * Adds a new Element to the end of the LinkedQueue<E>.
     *
     * @param x The element being added to the end of the LinkedQueue<E>.
     */
    public void enqueue(E x) {
        this.lls.add(x);
    }

    /**
     * Retrieves and removes the element at the front of the LinkedQueue<E>.
     *
     * @return The element that was removed from the LinkedQueue<E>.
     * @throws NullPointerException if the SinglyLinkedList<E> in our LinkedQueue<E> is empty.
     */
    public E dequeue() {
        if (this.lls.isEmpty()) {
            throw new NullPointerException("The Linked Queue is empty!");
        }

        return this.lls.remove(0);
    }

    /**
     * Returns the element at the front of the LinkedQueue<E>. This does not change the LinkedQueue<E>.
     *
     * @return The element at the front of the LinkedQueue<E>.
     */
    public E front() {
        return this.lls.get(0);
    }

    /**
     * Returns the element at the back of the LinkedQueue<E>. This does not change the LinkedQueue<E>.
     *
     * @return The element at the back of the LinkedQueue<E>.
     */
    public E back() {
        return this.lls.get(this.size() - 1);
    }

    /**
     * Returns the size of the LinkedQueue<E>.
     *
     * @return The size of the LinkedQueue<E>.
     */
    public int size() {
        return this.lls.size();
    }

    /**
     * Returns <code>true</code> if the size of the LinkedQueue<E> is equal to <code>0</code>, otherwise returns <code>
     * false</code>.
     *
     * @return Returns <code>true</code> if the size of the LinkedQueue<E> is equal to <code>0</code>, otherwise returns
     *     <code>false</code>.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * This method returns a String representation of the LinkedQueue<E> by simply calling the <code>toString()</code>
     * method on the SinglyLinkedList<E> variable <code>lls</code>.
     *
     * @return The String representation of this LinkedQueue<E>.
     */
    public String toString() {
        return this.lls.toString();
    }

    /**
     * This main method implements some tests for the LinkedQueue<E> class.
     *
     * @param args String args for the main method.
     */
    public static void main(String[] args) {
        /*
         * Integer Testing
         */
        System.out.println("Testing Integer Implementation...");
        Queue<Integer> xsI = new LinkedQueue<>();
        try {
            xsI.dequeue();
        } catch (NullPointerException e) {
            System.out.println("Exception caught!");
        }
        assert "()".equals(xsI.toString());
        int[] aI = new int[] {7, 4, 6, 9, 2};
        int i = 0;
        for (int xI : aI) {
            xsI.enqueue(xI);
            assert aI[0] == xsI.front();
            assert aI[i] == xsI.back();
            i++;
        }
        assert "(7 4 6 9 2)".equals(xsI.toString());
        xsI.dequeue();
        assert "(4 6 9 2)".equals(xsI.toString());
        while (!xsI.isEmpty()) {
            xsI.dequeue();
        }
        assert "()".equals(xsI.toString());
        System.out.println("All Integer Tests Passed!");

        System.out.println("");

        /*
         * Double Testing
         */
        System.out.println("Testing Double Implementation...");
        Queue<Double> xsD = new LinkedQueue<>();
        try {
            xsD.dequeue();
        } catch (NullPointerException e) {
            System.out.println("Exception caught!");
        }
        assert "()".equals(xsD.toString());
        double[] aD = new double[] {7, 4, 6, 9, 2};
        i = 0;
        for (double xD : aD) {
            xsD.enqueue(xD);
            assert aD[0] == xsD.front();
            assert aD[i] == xsD.back();
            i++;
        }
        assert "(7.0 4.0 6.0 9.0 2.0)".equals(xsD.toString());
        xsD.dequeue();
        assert "(4.0 6.0 9.0 2.0)".equals(xsD.toString());
        while (!xsD.isEmpty()) {
            xsD.dequeue();
        }
        assert "()".equals(xsD.toString());
        System.out.println("All Double Tests Passed!");

        System.out.println("");

        /*
         * Character Testing
         */
        System.out.println("Testing Character Implementation...");
        Queue<Character> xsC = new LinkedQueue<>();
        try {
            xsC.dequeue();
        } catch (NullPointerException e) {
            System.out.println("Exception caught!");
        }
        assert "()".equals(xsC.toString());
        char[] aC = new char[] {'a', 'b', 'c', 'd', 'e'};
        i = 0;
        for (char xC : aC) {
            xsC.enqueue(xC);
            assert aC[0] == xsC.front();
            assert aC[i] == xsC.back();
            i++;
        }
        assert "(a b c d e)".equals(xsC.toString());
        xsC.dequeue();
        assert "(b c d e)".equals(xsC.toString());
        while (!xsC.isEmpty()) {
            xsC.dequeue();
        }
        assert "()".equals(xsC.toString());
        System.out.println("All Character Tests Passed!");

        System.out.println("");

        /*
         * String Testing
         */
        System.out.println("Testing String Implementation...");
        Queue<String> xsS = new LinkedQueue<>();
        try {
            xsS.dequeue();
        } catch (NullPointerException e) {
            System.out.println("Exception caught!");
        }
        assert "()".equals(xsS.toString());
        String[] aS = new String[] {"string1", "string2", "string3", "string4", "string5"};
        i = 0;
        for (String xS : aS) {
            xsS.enqueue(xS);
            assert aS[0] == xsS.front();
            assert aS[i] == xsS.back();
            i++;
        }
        assert "(string1 string2 string3 string4 string5)".equals(xsS.toString());
        xsS.dequeue();
        assert "(string2 string3 string4 string5)".equals(xsS.toString());
        while (!xsS.isEmpty()) {
            xsS.dequeue();
        }
        assert "()".equals(xsS.toString());
        System.out.println("All String Tests Passed!");
    }
}

/**
 * This is our generic interface for a Queue<E>. Implements the following abstract classes.
 *
 * @param <E> Can be any element.
 */
interface Queue<E> {
    /**
     * This method will add an Element to the end of the Queue<E>.
     *
     * @param x The element being added to the Queue<E>.
     */
    void enqueue(E x);

    /**
     * This element will retrieve and return the first element in the Queue<E>. It will also remove the first element in
     * the Queue<E>.
     *
     * @return The first element in the Queue<E>.
     */
    E dequeue();

    /**
     * Retrieves and returns the first element in the Queue<E>. Does not change the Queue<E>.
     *
     * @return The first element in the Queue<E>.
     */
    E front();

    /**
     * Retrieves and returns the element in the back of the Queue<E>. Does not change the Queue<E>.
     *
     * @return The element in the back (or rear) or the Queue<E>.
     */
    E back();

    /**
     * Returns the size of the Queue<E>.
     *
     * @return The size of the Queue<E>.
     */
    int size();

    /**
     * Returns <code>true</code> iff the size of the Queue<E> is 0, <code>false</code> otherwise.
     *
     * @return <code>true</code> iff the size of the Queue<E> is 0, <code>false</code> otherwise.
     */
    boolean isEmpty();
}

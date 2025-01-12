package com.mrlonis.lec4a;

/**
 * Here is the List interface and SinglyLinkedList class from Lecture 3a. Do the following:
 *
 * <p>(1) Add appropriate Javadoc style comments to all methods. (2) Implement the remove() method. (3) Implement the
 * toString() method. (4) Turn List into a generic interface and SinglyLinkedList into a generic class so that the
 * structure can be used to hold any type T of data. (5) Test thoroughly in main() with a variety of data types.
 *
 * <p>We affirm that all members of our team contributed to this solution.
 *
 * @author green1
 * @author Nova Richardson
 */

/**
 * This Class is an implementation of the List<T> interface. This class works on any and all primitive data type and
 * contains the void add(T x); T remove(int i); T get(int i); boolean contains(T x); int size(); and boolean isEmpty();
 * methods from List<T>.
 *
 * @param <T> The type for this generic class.
 */
public class SinglyLinkedList<E> implements List<E> {

    /**
     * This class represents a node in a linked-list. The node contains two data fields: 1. for data of type T and 2.
     * Another Node variable called next which points to the next node in the linked-list.
     */
    public class Node {
        /** The data for a Node. Can be of any primitive Java type. */
        E data;

        /** A Node which points to the next Node in a linked-list sequence. */
        Node next;

        /**
         * A Node constructor which constructs a Node with the given data and makes the next field null.
         *
         * @param data The data for the Node being created.
         */
        Node(E data) {
            this(data, null);
        }

        /**
         * A Node constructor which constructs a Node with the given data and makes the next field equal to the given
         * Node pointer.
         *
         * @param data The data for the Node being created.
         * @param next The pointer to the Next node in the linked-list.
         */
        Node(E data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /** Stores the pointer that points to the first Node in the linked-list. */
    private Node head;

    /** Stores the size of the linked-list chain. */
    private int n;

    /** Default constructor for the SinglyLinkedList class with no arguments. */
    public SinglyLinkedList() {
        this.head = null;
        this.n = 0;
    }

    /**
     * This method adds the data x to the end of the linked-list.
     *
     * @param x The data for the Node being added to the end of the linked-list.
     */
    public void add(E x) {
        this.n++;
        if (head == null) head = new Node(x);
        else {
            Node p = head;
            while (p.next != null) {
                p = p.next;
            }
            p.next = new Node(x);
        }
    }

    /**
     * This method removes the Node at index i in the linked-list.
     *
     * @param i The index for the Node being removed.
     * @return Returns the data of the Node that was removed from the linked-list.
     * @throws IndexOutOfBoundsException if the index is less than zero or if i is greater than or equal to the size of
     *     the linked-list.
     */
    public E remove(int i) {
        if ((i >= this.n) || (i < 0)) {
            throw new IndexOutOfBoundsException();
        }

        if (this.head == null) {
            throw new NullPointerException();
        }

        Node current = this.head;

        if (i == 0) {
            E returnData = head.data;
            head = head.next;
            this.n--;
            return returnData;
        } else {
            while (i > 1) {
                current = current.next;
                i--;
            }
        }

        E returnData = current.next.data;
        Node temp = current.next.next;
        current.next = temp;
        this.n--;

        return returnData;
    }

    /**
     * This method finds the value of the Node at index i in the linked-list and returns the value of type T.
     *
     * @param i The index Node to return the value of.
     * @return The data value of the Node at index i.
     * @throws IndexOutOfBoundsException if i is less than zero or if i is greater than or equal to the size of the
     *     linked-list chain.
     */
    public E get(int i) {
        if (i < 0 || i >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node p = head;
        while (i > 0) {
            p = p.next;
            i--;
        }
        return p.data;
    }

    /**
     * This method returns <code>true</code> iff the data x is in one of the Nodes in the linked-list. Returns <code>
     * false</code> otherwise.
     *
     * @param x The data that the method will check if exists in one of the Nodes in the linked-list.
     * @return <code>true</code> if x exists in one of the Nodes in the linked-list, otherwise <code>false</code>.
     */
    public boolean contains(E x) {
        Node p = this.head;
        while (p != null) {
            if (p.data.equals(x)) {
                return true;
            } else {
                p = p.next;
            }
        }
        return false;
    }

    /**
     * This method returns the size of the linked-list by returning the n variable in SinglyLinkedList<T>().
     *
     * @return The size of the linked-list.
     */
    public int size() {
        return this.n;
    }

    /**
     * This method returns <code>true</code> iff the size of the linked-list equals zero, otherwise it returns <code>
     * false</code>.
     *
     * @return <code>true</code> if the size of the linked-list equals zero, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * This method returns a String representation of the linked-list.
     *
     * @return The String representation of this linked-list.
     */
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("(");
        Node current = this.head;

        if (this.n == 0) {
            string.append(")");
            return string.toString();
        } else {
            while (current != null) {
                string.append(current.data);
                string.append(" ");
                current = current.next;
            }
        }

        if (string.length() > 1) {
            string.setLength(string.length() - 1);
        }

        string.append(")");
        return string.toString();
    }

    /**
     * Simple Testing
     *
     * @param args
     */
    public static void main(String[] args) {
        /*
         * Integer Testing
         */
        List<Integer> xsI = new SinglyLinkedList<Integer>();
        assert "()".equals(xsI.toString());
        int[] aI = new int[] {7, 4, 6, 9, 2};
        for (int xI : aI) {
            xsI.add(xI);
        }
        assert "(7 4 6 9 2)".equals(xsI.toString());
        for (int xI : aI) {
            assert xsI.contains(xI);
        }
        for (int i = 0; i < xsI.size(); i++) {
            assert aI[i] == xsI.get(i);
        }
        assert "(7 4 6 9 2)".equals(xsI.toString());
        xsI.remove(3);
        assert "(7 4 6 2)".equals(xsI.toString());
        while (!xsI.isEmpty()) {
            xsI.remove(0);
        }
        assert "()".equals(xsI.toString());

        /*
         * Double Testing
         */
        List<Double> xsD = new SinglyLinkedList<Double>();
        assert "()".equals(xsD.toString());
        double[] aD = new double[] {7.0, 4.0, 6.0, 9.0, 2.0};
        for (double x : aD) {
            xsD.add(x);
        }
        assert "(7.0 4.0 6.0 9.0 2.0)".equals(xsD.toString());
        for (double x : aD) {
            assert xsD.contains(x);
        }
        for (int i = 0; i < xsD.size(); i++) {
            assert aD[i] == xsD.get(i);
        }
        assert "(7.0 4.0 6.0 9.0 2.0)".equals(xsD.toString());
        xsD.remove(3);
        assert "(7.0 4.0 6.0 2.0)".equals(xsD.toString());
        while (!xsD.isEmpty()) {
            xsD.remove(0);
        }
        assert "()".equals(xsD.toString());

        /*
         * Float Testing
         */
        List<Float> xsF = new SinglyLinkedList<Float>();
        assert "()".equals(xsF.toString());
        float[] aF = new float[] {7, 4, 6, 9, 2};
        for (float x : aF) {
            xsF.add(x);
        }
        assert "(7.0 4.0 6.0 9.0 2.0)".equals(xsF.toString());
        for (float x : aF) {
            assert xsF.contains(x);
        }
        for (int i = 0; i < xsF.size(); i++) {
            assert aF[i] == xsF.get(i);
        }
        assert "(7.0 4.0 6.0 9.0 2.0)".equals(xsF.toString());
        xsF.remove(3);
        assert "(7.0 4.0 6.0 2.0)".equals(xsF.toString());
        while (!xsF.isEmpty()) {
            xsF.remove(0);
        }
        assert "()".equals(xsF.toString());

        /*
         * String Testing
         */
        List<String> xsS = new SinglyLinkedList<String>();
        assert "()".equals(xsS.toString());
        String[] aS = new String[] {"7", "4", "6", "9", "2"};
        for (String x : aS) {
            xsS.add(x);
        }
        assert "(7 4 6 9 2)".equals(xsS.toString());
        for (String x : aS) {
            assert xsS.contains(x);
        }
        for (int i = 0; i < xsS.size(); i++) {
            assert aS[i] == xsS.get(i);
        }
        assert "(7 4 6 9 2)".equals(xsS.toString());
        xsS.remove(3);
        assert "(7 4 6 2)".equals(xsS.toString());
        while (!xsS.isEmpty()) {
            xsS.remove(0);
        }
        assert "()".equals(xsS.toString());

        /*
         * Character Testing
         */
        List<Character> xsC = new SinglyLinkedList<Character>();
        assert "()".equals(xsC.toString());
        char[] aC = new char[] {'a', 'b', 'c', 'd', 'e'};
        for (char x : aC) {
            xsC.add(x);
        }
        assert "(a b c d e)".equals(xsC.toString());
        for (char x : aC) {
            assert xsC.contains(x);
        }
        for (int i = 0; i < xsC.size(); i++) {
            assert aC[i] == xsC.get(i);
        }
        assert "(a b c d e)".equals(xsC.toString());
        xsC.remove(3);
        assert "(a b c e)".equals(xsC.toString());
        while (!xsC.isEmpty()) {
            xsC.remove(0);
        }
        assert "()".equals(xsS.toString());

        /*
         * Boolean Testing
         */
        List<Boolean> xsB = new SinglyLinkedList<Boolean>();
        assert "()".equals(xsB.toString());
        Boolean[] aB = new Boolean[] {true, false, true, false, true};
        for (Boolean x : aB) {
            xsB.add(x);
        }
        assert "(true false true false true)".equals(xsB.toString());
        for (Boolean x : aB) {
            assert xsB.contains(x);
        }
        for (int i = 0; i < xsB.size(); i++) {
            assert aB[i] == xsB.get(i);
        }
        assert "(true false true false true)".equals(xsB.toString());
        xsB.remove(3);
        assert "(true false true true)".equals(xsB.toString());
        while (!xsB.isEmpty()) {
            xsB.remove(0);
        }
        assert "()".equals(xsB.toString());

        System.out.println("...Testing Complete!");
    }
}

/**
 * This interface is of the type T and contains the methods void add(T x); T remove(int i); T get(int i); boolean
 * contains(T x); int size(); and boolean isEmpty(); to be used in the SinglyLinkedList<T> Class.
 *
 * @param <T> The type for this generic interface.
 */
interface List<E> {

    /**
     * This method will add a Node to the end of a linked-list.
     *
     * @param x The data for the Node added to the end of this linked-list.
     */
    void add(E x);

    /**
     * This method will remove the Node at index i in a linked-list
     *
     * @param i The index Node to be removed.
     * @return The data of type T of the removed Node.
     */
    E remove(int i);

    /**
     * This method will return the data of the Node at index i in a linked-list.
     *
     * @param i The index Node to return the data of.
     * @return The data of the Node at index i. Is of type T.
     */
    E get(int i);

    /**
     * This method will return <code>true</code> iff the linked-list contains x, <code>false</code> otherwise.
     *
     * @param x The data that a Node in the linked-list must contain.
     * @return <code>true</code> if the linked-list contains a Node with data equal to x, otherwise <code>false</code>.
     */
    boolean contains(E x);

    /**
     * This method will return the size of the linked-list chain.
     *
     * @return The size of the linked-list chain.
     */
    int size();

    /**
     * This method will return <code>true</code> iff the linked-list is empty, otherwise <code>false</code>.
     *
     * @return <code>true</code> if the linked-list is empty, otherwise <code>false</code>.
     */
    boolean isEmpty();
}

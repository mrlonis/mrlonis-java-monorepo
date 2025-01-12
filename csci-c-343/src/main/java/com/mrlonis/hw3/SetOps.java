package com.mrlonis.hw3;

import java.util.Iterator;

/**
 * hw3: Problem 2 starter code.
 *
 * @author Matthew Lonis
 */
public class SetOps {

    /**
     * Returns a list (without duplicates) containing all the items in ls1 plus all the items in ls2. Note: ls1 and ls2
     * are unchanged by this method.
     */
    public static <T> List<T> union(List<T> ls1, List<T> ls2) {
        List<T> ans = new DoublyLinkedList<>();
        Iterator<T> ls1IT = ls1.iterator();
        Iterator<T> ls2IT = ls2.iterator();

        while (ls1IT.hasNext() || ls2IT.hasNext()) {
            T data1 = null;
            T data2 = null;

            if (ls1IT.hasNext()) {
                data1 = ls1IT.next();
            }

            if (ls2IT.hasNext()) {
                data2 = ls2IT.next();
            }

            if (data1 == null && !ans.contains(data2)) {
                ans.add(data2);
            } else if (data2 == null && !ans.contains(data1)) {
                ans.add(data1);
            } else {
                if (!ans.contains(data1)) {
                    ans.add(data1);
                }

                if (!ans.contains(data2)) {
                    ans.add(data2);
                }
            }
        }

        /** This is my first try at the code. It works. */
        /*
        while (ls1IT.hasNext()) {
        	T data = ls1IT.next();

        	if (!ans.contains(data)) {
        		ans.add(data);
        	}
        }

        while (ls2IT.hasNext()) {
        	T data = ls2IT.next();

        	if (!ans.contains(data)) {
        		ans.add(data);
        	}
        }
        */

        return ans;
    }

    /**
     * Returns a list (without duplicates) of all the items which appear both in ls1 and in ls2. Note: ls1 and ls2 are
     * unchanged by this method.
     */
    public static <T> List<T> intersection(List<T> ls1, List<T> ls2) {
        List<T> ans = new DoublyLinkedList<>();
        Iterator<T> ls1IT = ls1.iterator();
        Iterator<T> ls2IT = ls2.iterator();

        while (ls1IT.hasNext() || ls2IT.hasNext()) {
            T data1 = null;
            T data2 = null;

            if (ls1IT.hasNext()) {
                data1 = ls1IT.next();
            }

            if (ls2IT.hasNext()) {
                data2 = ls2IT.next();
            }

            if (data1 == null && !ans.contains(data2) && ls1.contains(data2)) {
                ans.add(data2);
            } else if (data2 == null && !ans.contains(data1) && ls2.contains(data1)) {
                ans.add(data1);
            } else {
                if (!ans.contains(data1) && ls2.contains(data1)) {
                    ans.add(data1);
                }

                if (!ans.contains(data2) && ls1.contains(data2)) {
                    ans.add(data2);
                }
            }
        }

        /** This is my first try at the code. It works. */
        /*
        while (ls1IT.hasNext()) {
        	T data = ls1IT.next();

        	if (ls2.contains(data) && !ans.contains(data)) {
        		ans.add(data);
        	}
        }

        while (ls2IT.hasNext()) {
        	T data = ls2IT.next();

        	if (!ans.contains(data) && ls1.contains(data)) {
        		ans.add(data);
        	}
        }
        */

        return ans;
    }

    /** Simple testing to get you started. Add more tests of your own! */
    public static void main(String... args) {
        List<String> ls1 = new DoublyLinkedList<>();
        ls1.add("ant");
        ls1.add("bat");
        ls1.add("cat");
        ls1.add("ant"); // this is a duplicate element
        ls1.add("fox");
        int n1 = ls1.size();
        System.out.println("ls1 = " + ls1);

        List<String> ls2 = new DoublyLinkedList<>();
        ls2.add("cat");
        ls2.add("dog");
        ls2.add("dog"); // this is a duplicate element
        ls2.add("emu");
        ls2.add("fox");
        ls2.add("gnu");
        int n2 = ls2.size();
        System.out.println("ls2 = " + ls2);

        List<String> ls3, ls4;
        ls3 = union(ls1, ls2);
        assert n1 == ls1.size();
        assert n2 == ls2.size();
        assert 7 == ls3.size();
        System.out.println("ls3 = " + ls3);

        ls4 = intersection(ls1, ls2);
        assert n1 == ls1.size();
        assert n2 == ls2.size();
        assert 2 == ls4.size();
        System.out.println("ls4 = " + ls4);

        List<Integer> ls5 = new DoublyLinkedList<>();
        ls5.add(1);
        ls5.add(2);
        ls5.add(3);
        ls5.add(2); // this is a duplicate element
        ls5.add(4);
        int n3 = ls5.size();
        System.out.println("ls5 = " + ls5);

        List<Integer> ls6 = new DoublyLinkedList<>();
        ls6.add(2);
        ls6.add(8);
        ls6.add(8); // this is a duplicate element
        ls6.add(6);
        ls6.add(1);
        ls6.add(9);
        int n4 = ls6.size();
        System.out.println("ls6 = " + ls6);

        List<Integer> ls7, ls8;
        ls7 = union(ls5, ls6);
        assert n3 == ls5.size();
        assert n4 == ls6.size();
        assert 7 == ls7.size();
        System.out.println("ls7 = " + ls7);

        ls8 = intersection(ls5, ls6);
        assert n3 == ls5.size();
        assert n4 == ls6.size();
        assert 2 == ls8.size();
        System.out.println("ls8 = " + ls8);

        System.out.println("All Tests Passed!");
    }
}

package com.mrlonis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinarySearchTreeTests {

    @Test
    public void shouldReturn8_whenInsertedData() {
        int[] a;
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        a = new int[]{5, 3, 1, 2, 7, 6, 9, 8, -1, -7, -5, -2, -3, 10, 15, 13, 12, 19, 20, 21};
        for (Integer x : a) {
            bst.insert(x);
        }
        System.out.println(bst.keys().toString());
        bst.printAll();
        System.out.println(bst);
        assertEquals(8, bst.height());
    }

}

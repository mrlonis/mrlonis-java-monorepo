package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AVLTreeTests {

    @Test
    public void shouldReturn5_whenInsertedData() {
        int[] a;
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        a = new int[] {5, 3, 1, 2, 7, 6, 9, 8, -1, -7, -5, -2, -3, 10, 15, 13, 12, 19, 20, 21};
        for (Integer x : a) {
            avl.insert(x);
        }
        System.out.println(avl.keys().toString());
        avl.printAll();
        System.out.println(avl.toString());
        assertEquals(5, avl.height());
    }

}

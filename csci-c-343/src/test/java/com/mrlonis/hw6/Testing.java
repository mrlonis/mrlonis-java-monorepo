package com.mrlonis.hw6;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Testing class to test functionality of ListDigraph.java
 *
 * @author Matthew Lonis (mrlonis)
 */
class Testing {

    /** Test for the general setup of a Digraph */
    @Test
    void setUpTest() {
        ListDigraph gr = new ListDigraph(4);
        assertEquals(4, gr.numVertices());
        assertEquals(0, gr.numEdges());
        gr.addEdge(0, 1, 1);
        gr.addEdge(0, 2, 2);
        gr.addEdge(1, 1, 3);
        gr.addEdge(1, 3, 4);
        gr.addEdge(2, 1, 5);
        gr.addEdge(2, 3, 5);
        gr.addEdge(2, 3, 6);
        gr.addEdge(3, 2, 7);
        assertEquals(4, gr.numVertices());
        assertEquals(7, gr.numEdges());
        assertTrue(gr.hasEdge(0, 1));
        assertTrue(gr.hasEdge(0, 2));
        assertTrue(gr.hasEdge(1, 1));
        assertTrue(gr.hasEdge(1, 3));
        assertTrue(gr.hasEdge(2, 1));
        assertTrue(gr.hasEdge(2, 3));
        assertTrue(gr.hasEdge(3, 2));
        assertEquals(1, gr.weight(0, 1));
        assertEquals(2, gr.weight(0, 2));
        assertEquals(3, gr.weight(1, 1));
        assertEquals(4, gr.weight(1, 3));
        assertEquals(5, gr.weight(2, 1));
        assertEquals(6, gr.weight(2, 3));
        assertEquals(7, gr.weight(3, 2));
        assertFalse(gr.hasEdge(0, 0));
        assertFalse(gr.hasEdge(1, 0));
        assertFalse(gr.hasEdge(10, 11));
        assertEquals(Integer.MAX_VALUE, gr.weight(10, 11));
    }

    /** Test for the out() operation. */
    @Test
    void outTest() {
        ListDigraph gr = new ListDigraph(4);
        gr.addEdge(0, 1, 1);
        gr.addEdge(0, 2, 2);
        gr.addEdge(1, 1, 3);
        gr.addEdge(1, 3, 4);
        gr.addEdge(2, 1, 5);
        gr.addEdge(2, 3, 6);
        gr.addEdge(3, 2, 7);

        assertEquals(2, gr.out(0).size());
        assertTrue(gr.out(0).contains(1));
        assertTrue(gr.out(0).contains(2));

        assertEquals(2, gr.out(1).size());
        assertTrue(gr.out(1).contains(1));
        assertTrue(gr.out(1).contains(3));

        assertEquals(2, gr.out(2).size());
        assertTrue(gr.out(2).contains(1));
        assertTrue(gr.out(2).contains(3));

        assertEquals(1, gr.out(3).size());
        assertTrue(gr.out(3).contains(2));
    }

    /** Test for the in() operation. */
    @Test
    void inTest() {
        ListDigraph gr = new ListDigraph(4);
        gr.addEdge(0, 1, 1);
        gr.addEdge(0, 2, 2);
        gr.addEdge(1, 1, 3);
        gr.addEdge(1, 3, 4);
        gr.addEdge(2, 1, 5);
        gr.addEdge(2, 3, 6);
        gr.addEdge(3, 2, 7);

        assertEquals(0, gr.in(0).size());
        assertFalse(gr.in(0).contains(1));
        assertFalse(gr.in(0).contains(2));

        assertEquals(3, gr.in(1).size());
        assertTrue(gr.in(1).contains(0));
        assertTrue(gr.in(1).contains(1));
        assertTrue(gr.in(1).contains(2));

        assertEquals(2, gr.in(2).size());
        assertTrue(gr.in(2).contains(0));
        assertTrue(gr.in(2).contains(3));

        assertEquals(2, gr.in(3).size());
        assertTrue(gr.in(3).contains(1));
        assertTrue(gr.in(3).contains(2));
    }

    /** Test for the twoHopsAway() operation. */
    @Test
    void twoHopTest() {
        ListDigraph gr = new ListDigraph(4);
        gr.addEdge(0, 1, 1);
        gr.addEdge(0, 2, 2);
        gr.addEdge(1, 1, 3);
        gr.addEdge(1, 3, 4);
        gr.addEdge(2, 1, 5);
        gr.addEdge(2, 3, 6);
        gr.addEdge(3, 2, 7);

        assertEquals(2, gr.twoHopsAway(0).size());
        assertTrue(gr.twoHopsAway(0).contains(1));
        assertTrue(gr.twoHopsAway(0).contains(3));

        assertEquals(3, gr.twoHopsAway(1).size());
        assertTrue(gr.twoHopsAway(1).contains(1));
        assertTrue(gr.twoHopsAway(1).contains(2));
        assertTrue(gr.twoHopsAway(1).contains(3));

        assertEquals(3, gr.twoHopsAway(2).size());
        assertTrue(gr.twoHopsAway(2).contains(1));
        assertTrue(gr.twoHopsAway(2).contains(2));
        assertTrue(gr.twoHopsAway(2).contains(3));

        assertEquals(2, gr.twoHopsAway(3).size());
        assertTrue(gr.twoHopsAway(3).contains(1));
        assertTrue(gr.twoHopsAway(3).contains(3));
    }
}

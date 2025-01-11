package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import org.junit.jupiter.api.Test;

/**
 * Assorted JUnit tests for ColorTable.
 *
 * <p>To "turn off" any test, put double slashes ( "//" ) in front of the "@Test" that precedes that method. For
 * example, "//@Test".
 */
class Testing {

    private static Color c1;
    private static Color c2;
    private static Color c3;
    private static Color c5;
    private static Color c6;
    private static Color c7;
    private static ColorTable table;

    @Test
    void testPutAndGet() {
        table = new ColorTable(3, 6, Constants.LINEAR, 0.9);
        assertEquals(0, table.getSize());
        assertEquals(3, table.getCapacity());
        table.put(Color.BLACK, 5);
        assertEquals(1, table.getSize());
        assertEquals(5, table.get(Color.BLACK));
        table.put(Color.WHITE, 5);
        assertEquals(2, table.getSize());
        table.put(Color.RED, 5);
        table.put(Color.RED, 23);
        assertEquals(3, table.getSize());
        assertEquals(23, table.get(Color.RED));
    }

    @Test
    void testPutAndGetAt() {
        table = new ColorTable(3, 2, Constants.LINEAR, 0.9);
        assertEquals(0, table.getSize());
        assertEquals(3, table.getCapacity());
        assertTrue(table.isEmpty());
        table.put(Color.BLACK, 5);
        assertEquals(1, table.getSize());
        assertEquals(3, table.getCapacity());
        assertFalse(table.isEmpty());
        assertEquals(0, ColorTable.getNumCollisions());
        table.put(Color.WHITE, 5);
        table.put(Color.RED, 5);
        table.put(Color.BLUE, 5);
        assertEquals(7, table.getCapacity());
        table.put(Color.RED, 23);
        assertEquals(7, table.getCapacity());
        assertEquals(23, table.getCountAt(6));
        assertEquals(5, table.getCountAt(0));
    }

    @Test
    void testImplicitCounts() {
        table = new ColorTable(3, 6, Constants.LINEAR, 0.9);
        assertEquals(0, table.get(Color.BLUE));
        assertEquals(0, table.get(Color.PINK));
        assertEquals(0, table.get(Color.CYAN));
        assertEquals(0, table.getSize());
    }

    @Test
    void testIncrementAndGet() {
        table = new ColorTable(3, 4, Constants.LINEAR, 0.9);
        table.increment(Color.RED);
        table.increment(Color.RED);
        assertEquals(2, table.get(Color.RED));
        for (int i = 0; i < 100; i++) {
            table.increment(Color.BLUE);
        }
        assertEquals(100, table.get(Color.BLUE));
        assertEquals(2, table.get(Color.RED));
    }

    @Test
    void testIncrementTruncated() {
        table = new ColorTable(3, 1, Constants.LINEAR, 0.9); // just using 3 bits for a color
        c1 = new Color(0xf0, 0xe0, 0xd0);
        c2 = new Color(0xff, 0xee, 0xdd);
        c3 = new Color(0xd1, 0xf2, 0xe3);

        table.put(c1, 5);
        assertEquals(5, table.get(c1));
        table.increment(c2);
        table.increment(c3);
        assertEquals(7, table.get(c1));
    }

    @Test
    void testIncrementAndResizing() {
        table = new ColorTable(3, 6, Constants.LINEAR, 0.8);
        table.increment(Color.BLACK);
        assertEquals(1, table.get(Color.BLACK));
        table.increment(Color.WHITE);
        assertEquals(1, table.get(Color.WHITE));
        table.increment(Color.BLACK);
        assertEquals(2, table.get(Color.BLACK));
        table.increment(Color.BLACK);
        assertEquals(3, table.get(Color.BLACK));
        table.increment(Color.WHITE);
        assertEquals(2, table.get(Color.WHITE));
        table.increment(Color.BLACK);
        assertEquals(4, table.get(Color.BLACK));

        // System.out.println(table);

        // Check status prior to rehashing.
        assertEquals(2, table.getSize());
        assertEquals(3, table.getCapacity());
        // This increment will trigger a rehash event.
        table.increment(Color.RED);
        // Check status after rehashing.
        assertEquals(3, table.getSize());
        assertEquals(7, table.getCapacity());

        // System.out.println(table);

        // Check the final frequency counts.
        assertEquals(1, table.get(Color.RED));
        assertEquals(4, table.get(Color.BLACK));
        assertEquals(2, table.get(Color.WHITE));
    }

    @Test
    void testQuadraticProbingAndResizing() {
        testQuadraticProbing(); // This sets up the table to just before resizing.
        assertEquals(0, table.getCountAt(7)); // This fails if you rehash too soon.
        assertEquals(6, table.getSize());
        assertEquals(13, table.getCapacity());

        table.put(c5, 999); // This is an update -- do not rehash now.
        assertEquals(0, table.getCountAt(7)); // Again, this fails if you rehash too soon.
        assertEquals(6, table.getSize());
        assertEquals(13, table.getCapacity());

        // The next operation should force a resize from 13 to 31.
        table.put(c6, 7);
        // System.out.println(table);
        assertEquals(7, table.getCountAt(16));
        table.put(c7, 8);
        assertEquals(8, table.getCountAt(29));
        assertEquals(8, table.getSize());
        assertEquals(31, table.getCapacity());
    }

    @Test
    void testQuadraticProbing() {
        table = new ColorTable(13, 8, Constants.QUADRATIC, 0.49);
        // For a tableSize of 13, these keys all collide.
        Color c0 = new Color(0);
        c1 = new Color(13);
        c2 = new Color(2 * 13);
        c3 = new Color(3 * 13);
        Color c4 = new Color(4 * 13);
        c5 = new Color(5 * 13);
        c6 = new Color(6 * 13);
        c7 = new Color(7 * 13);
        table.put(c0, 1);
        table.put(c1, 2);
        table.put(c2, 3);
        assertEquals(3, table.getSize());
        assertEquals(13, table.getCapacity());
        assertEquals(3, table.getCountAt(4));
        table.put(c3, 4);
        assertEquals(4, table.getCountAt(9));
        table.put(c4, 5);
        assertEquals(5, table.getCountAt(3));
        table.put(c5, 6);
        assertEquals(6, table.getCountAt(12));
        assertEquals(6, table.getSize());
        assertEquals(13, table.getCapacity());
    }

    @Test
    void testLowLoadFactor() {
        table = new ColorTable(3, 4, Constants.LINEAR, 0.1);
        assertEquals(0, table.getSize());
        assertEquals(3, table.getCapacity());
        assertTrue(table.getLoadFactor() < 0.1);
        table.put(Color.BLACK, 5);
        assertEquals(1, table.getSize());
        assertEquals(19, table.getCapacity());
        assertTrue(table.getLoadFactor() < 0.1);
        assertEquals(5, table.get(Color.BLACK));
        table.put(Color.WHITE, 5);
        assertEquals(2, table.getSize());
        assertEquals(43, table.getCapacity());
        assertTrue(table.getLoadFactor() < 0.1);
        for (int c = 0xFF; c >= 0xF0; c--) {
            table.put(new Color(c, c, c), c);
        }
        assertEquals(0xF0, table.get(Color.WHITE));
        assertEquals(2, table.getSize());
        for (int c = 0x0F; c <= 0xFF; c += 0x10) {
            table.put(new Color(c, c, c), c);
        }
        assertEquals(211, table.getCapacity());
        assertTrue(table.getLoadFactor() < 0.1);
    }

    @Test
    void testIterator() {
        table = new ColorTable(13, 2, Constants.LINEAR, 0.49);
        for (int i = 0; i < 20 * 17; i += 17) {
            table.put(new Color(i * i), i);
        }
        assertEquals(9, table.getSize());
        int[] expected = {272, 119, 102, 0, 0, 289, 0, 306, 0, 0, 323, 221, 255, 238};
        Iterator it = table.iterator();
        int k = 0;
        while (it.hasNext() && k < expected.length) {
            assertEquals(expected[k], it.next());
            k++;
        }

        table = new ColorTable(13, 2, Constants.QUADRATIC, 0.49);
        for (int i = 0; i < 20 * 17; i += 17) {
            table.put(new Color(i * i), i);
        }
        assertEquals(9, table.getSize());
        it = table.iterator();
        k = 0;
        while (it.hasNext() && k < expected.length) {
            assertEquals(expected[k], it.next());
            k++;
        }
    }

    @Test
    void testCosineSimilarity() {
        ColorTable ct1 = Driver.vectorize(Painting.BLUE_DANCERS.get(), 1);
        ColorTable ct2 = Driver.vectorize(Painting.STARRY_NIGHT.get(), 1);
        System.out.println("ct1 = " + ct1);
        System.out.println("ct2 = " + ct2);
        /*
         * Expected output:
         * ct1 = [0:0,185844, 1:1,22927, 2:2,4639, 3:3,42667, 4:4,1202, 5:5,4, 6:6,1465,
         * 7:7,25652]
         * ct2 = [0:0,93892, 1:1,53325, 2:2,2416, 3:3,19151, 4:4,356, 5:5,64, 6:6,8108,
         * 7:7,24688]
         */
        System.out.println("Util.vectorMagnitude(ct1) = " + Util.vectorMagnitude(ct1));
        assertEquals(193_954.368_628_293_6, Util.vectorMagnitude(ct1), .01);
        System.out.println("Util.vectorMagnitude(ct2) = " + Util.vectorMagnitude(ct2));
        assertEquals(112_726.345_571_920_32, Util.vectorMagnitude(ct2), .01);
        System.out.println("Util.dotProduct(ct1, ct2) = " + Util.dotProduct(ct1, ct2));
        assertEquals(2.0155270821E10, Util.dotProduct(ct1, ct2), .01);
        System.out.println("Util.cosineSimilarity(ct1, ct2) = " + Util.cosineSimilarity(ct1, ct2));
        assertEquals(0.922_048_625_428_344_1, Util.cosineSimilarity(ct1, ct2), .01);
    }
}

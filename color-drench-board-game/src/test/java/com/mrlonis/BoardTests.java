package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

/**
 * JUnit tests for all methods.
 *
 * @author Matthew Lonis
 */
class BoardTests {

    @Test
    void testOnBoard() {
        assertFalse(new Coord(3, 4).onBoard(4));
        assertTrue(new Coord(3, 4).onBoard(5));
        assertFalse(new Coord(1, 8).onBoard(4));
        assertTrue(new Coord(8, 8).onBoard(9));
        assertFalse(new Coord(6, 1).onBoard(4));
        assertTrue(new Coord(0, 0).onBoard(2));
        assertFalse(new Coord(2, 7).onBoard(2));
        assertTrue(new Coord(8, 3).onBoard(9));
    }

    @Test
    void testNeighbors() {
        List<Coord> arrayList = new ArrayList<>();
        assertEquals(arrayList, new Coord(0, 0).neighbors(1));

        arrayList.add(new Coord(0, 1));
        arrayList.add(new Coord(1, 0));
        assertEquals(arrayList, new Coord(0, 0).neighbors(2));

        arrayList = new ArrayList<>();
        arrayList.add(new Coord(2, 1));
        arrayList.add(new Coord(2, 3));
        arrayList.add(new Coord(1, 2));
        arrayList.add(new Coord(3, 2));
        assertEquals(arrayList, new Coord(2, 2).neighbors(5));

        arrayList = new ArrayList<>();
        arrayList.add(new Coord(2, 1));
        arrayList.add(new Coord(1, 2));
        assertEquals(arrayList, new Coord(2, 2).neighbors(3));

        arrayList = new ArrayList<>();
        arrayList.add(new Coord(0, 1));
        arrayList.add(new Coord(0, 3));
        arrayList.add(new Coord(1, 2));
        // System.out.println(new Coord(0, 2).neighbors(5).toString());
        assertEquals(arrayList, new Coord(0, 2).neighbors(5));

        arrayList = new ArrayList<>();
        arrayList.add(new Coord(0, 3));
        arrayList.add(new Coord(1, 4));
        // System.out.println(new Coord(0, 2).neighbors(5).toString());
        assertEquals(arrayList, new Coord(0, 4).neighbors(5));

        arrayList = new ArrayList<>();
        arrayList.add(new Coord(4, 1));
        arrayList.add(new Coord(3, 0));
        // System.out.println(new Coord(0, 2).neighbors(5).toString());
        assertEquals(arrayList, new Coord(4, 0).neighbors(5));
    }

    @Test
    void testHashCode() {
        Coord someCoord = new Coord(0, 0);
        assertEquals(0, someCoord.hashCode());
        someCoord = new Coord(1, 1);
        assertEquals(32, someCoord.hashCode());
        someCoord = new Coord(5, 8);
        assertEquals(253, someCoord.hashCode());
    }

    @Test
    void testFullyFlooded() {
        int size = 1;
        Board testBoard = new Board(size);
        WaterColor color = WaterColor.BLUE;
        assertTrue(testBoard.fullyFlooded());

        WaterColor[][] colorArray = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray[i][j] = color;
            }
        }
        colorArray[0][0] = WaterColor.RED;
        testBoard = new Board(colorArray);
        testBoard.flood(WaterColor.BLUE);
        assertTrue(testBoard.fullyFlooded());
    }

    @Test
    void testFlood() {
        int size = 5;
        Board testBoard = new Board(size);
        WaterColor color = WaterColor.BLUE;
        testBoard.flood(color);
        assertSame(testBoard.get(Coord.ORIGIN).getColor(), color);

        WaterColor[][] colorArray = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray[i][j] = color;
            }
        }
        colorArray[0][0] = WaterColor.RED;
        colorArray[0][1] = WaterColor.PINK;
        testBoard = new Board(colorArray);
        testBoard.flood(WaterColor.PINK);

        WaterColor[][] colorArray1 = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray1[i][j] = color;
            }
        }
        colorArray1[0][0] = WaterColor.PINK;
        colorArray1[0][1] = WaterColor.PINK;
        Board anotherBoard = new Board(colorArray1);

        assertEquals(testBoard.inside, anotherBoard.inside);
        assertEquals(testBoard.outside, anotherBoard.outside);

        testBoard.flood(WaterColor.BLUE);
        assertTrue(testBoard.fullyFlooded());
    }

    @Test
    void testFlood1() {
        int size = 5;
        Board testBoard = new Board(size);
        WaterColor color = WaterColor.BLUE;
        testBoard.flood1(color);
        assertSame(testBoard.get(Coord.ORIGIN).getColor(), color);

        WaterColor[][] colorArray = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray[i][j] = color;
            }
        }
        colorArray[0][0] = WaterColor.RED;
        colorArray[0][1] = WaterColor.PINK;
        testBoard = new Board(colorArray);
        testBoard.flood1(WaterColor.PINK);

        WaterColor[][] colorArray1 = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray1[i][j] = color;
            }
        }
        colorArray1[0][0] = WaterColor.PINK;
        colorArray1[0][1] = WaterColor.PINK;
        Board anotherBoard = new Board(colorArray1);

        assertEquals(testBoard.inside, anotherBoard.inside);
        assertEquals(testBoard.outside, anotherBoard.outside);

        testBoard.flood1(WaterColor.BLUE);
        assertTrue(testBoard.fullyFlooded());
    }

    @Test
    void testFlood2() {
        int size = 5;
        Board testBoard = new Board(size);
        WaterColor color = WaterColor.BLUE;
        testBoard.flood2(color);
        assertSame(testBoard.get(Coord.ORIGIN).getColor(), color);

        WaterColor[][] colorArray = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray[i][j] = color;
            }
        }
        colorArray[0][0] = WaterColor.RED;
        colorArray[0][1] = WaterColor.PINK;
        testBoard = new Board(colorArray);
        testBoard.flood2(WaterColor.PINK);

        WaterColor[][] colorArray1 = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray1[i][j] = color;
            }
        }
        colorArray1[0][0] = WaterColor.PINK;
        colorArray1[0][1] = WaterColor.PINK;
        Board anotherBoard = new Board(colorArray1);

        assertEquals(testBoard.inside, anotherBoard.inside);
        assertEquals(testBoard.outside, anotherBoard.outside);

        testBoard.flood2(WaterColor.BLUE);
        assertTrue(testBoard.fullyFlooded());
    }

    @Test
    void testSuggest() {
        int size = 5;
        Board testBoard = new Board(size);
        WaterColor color = WaterColor.BLUE;
        testBoard.flood(color);
        try {
            assertNotSame(testBoard.suggest(), color);
        } catch (AssertionFailedError e) {
            testBoard = new Board(size);
            testBoard.flood(color);
            assertNotSame(testBoard.suggest(), color);
        }

        WaterColor[][] colorArray = new WaterColor[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                colorArray[i][j] = color;
            }
        }
        colorArray[0][0] = WaterColor.RED;
        colorArray[0][1] = WaterColor.PINK;
        testBoard = new Board(colorArray);
        assertEquals(WaterColor.BLUE, testBoard.suggest());
    }
}

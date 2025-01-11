package com.mrlonis;

/*
 * To test with JUnit, add JUnit to your project. To do this, go to Project->Properties. Select "Java Build Path". Select the "Libraries" tab and "Add Library". Select JUnit, then
 * JUnit 4.
 *
 * @author Matthew Lonis (mrlonis)
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mrlonis.Judge.StringLengthException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class JudgeTests {

    @Test
    void defaultJudgeTest() {
        Judge judge = new Judge();
        assertEquals(2, judge.score('A', 'A'));
        assertEquals(-2, judge.score('A', 'C'));
        assertEquals(-1, judge.score('A', '_'));
        assertEquals(-1, judge.score('_', 'A'));
        assertEquals(-1, judge.score('_', '_'));
        assertEquals(2, judge.score("A", "A"));
        assertEquals(-2, judge.score("A", "C"));
        assertEquals(-1, judge.score("A", "_"));
        assertEquals(-1, judge.score("_", "A"));
        assertEquals(-1, judge.score("_", "_"));
        assertEquals(8, judge.score("ACTG", "ACTG"));
        assertEquals(-8, judge.score("ACTG", "CTGA"));
        assertEquals(-4, judge.score("AAAA", "____"));
        assertEquals(-4, judge.score("____", "AAAA"));
        assertEquals(-4, judge.score("____", "____"));
        assertEquals(-2, judge.score("AC_G", "A_TA"));
    }

    /** Create custom judge class and custom tests/strings */
    @Test
    void customJudgeTest() {
        Judge judge = new Judge(10, -10, -5);
        assertEquals(10, judge.score('M', 'M'));
        assertEquals(-10, judge.score('M', 'T'));
        assertEquals(-5, judge.score('M', '_'));
        assertEquals(-5, judge.score('_', 'M'));
        assertEquals(-5, judge.score('_', '_'));
        assertEquals(-20, judge.score("AM", "MA"));
        assertEquals(-35, judge.score("ABRA___", "CADABRA"));
        try {
            judge.score("MATTHEW", "LONIS");
        } catch (StringLengthException ignored) {

        }
        assertEquals(-30, judge.score("ATANDT", "______"));
        assertEquals(-20, judge.score("____", "AELI"));
        assertEquals(-5, judge.score("_", "_"));
        assertEquals(60, judge.score("BARBRA", "BARBRA"));
        assertEquals(-70, judge.score("BARBRABAR", "SUPERCITY"));
        assertEquals(-30, judge.score("DONALD", "______"));
        assertEquals(-55, judge.score("___________", "MASDGSGREAG"));
        assertEquals(-40, judge.score("________", "________"));
        assertEquals(-10 - 10 - 5 - 5 - 5 - 10 - 10 - 10, judge.score("MATT_HEW", "LO___NIS"));
    }

    /**
     * ******************************************** Testing SequenceAligner.fillCache()
     * ********************************************
     */
    @Test
    void empties() {
        SequenceAligner sa;
        Result result;
        sa = new SequenceAligner("", "");
        result = sa.getResult(0, 0);
        assertNotNull(result);
        assertEquals(0, result.getScore());
        assertEquals(Direction.NONE, result.getParent());
    }

    @Test
    void singletons() {
        SequenceAligner sa;
        Result result;
        sa = new SequenceAligner("A", "A");
        result = sa.getResult(0, 0);
        assertNotNull(result);
        assertEquals(0, result.getScore());
        assertEquals(Direction.NONE, result.getParent());
        result = sa.getResult(0, 1);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        result = sa.getResult(1, 0);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.UP, result.getParent());
        result = sa.getResult(1, 1);
        assertNotNull(result);
        assertEquals(2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        sa = new SequenceAligner("A", "C");
        result = sa.getResult(0, 0);
        assertNotNull(result);
        assertEquals(0, result.getScore());
        assertEquals(Direction.NONE, result.getParent());
        result = sa.getResult(0, 1);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        result = sa.getResult(1, 0);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.UP, result.getParent());
        result = sa.getResult(1, 1);
        assertNotNull(result);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
    }

    @Test
    void singletonsWithCustomJudge() {
        Judge judge;
        SequenceAligner sa;
        Result result;
        judge = new Judge(3, -3, -2);
        sa = new SequenceAligner("A", "A", judge);
        result = sa.getResult(0, 0);
        assertNotNull(result);
        assertEquals(0, result.getScore());
        assertEquals(Direction.NONE, result.getParent());
        result = sa.getResult(0, 1);
        assertNotNull(result);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        result = sa.getResult(1, 0);
        assertNotNull(result);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.UP, result.getParent());
        result = sa.getResult(1, 1);
        assertNotNull(result);
        assertEquals(3, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        judge = new Judge(3, -3, -1);
        sa = new SequenceAligner("A", "C", judge);
        result = sa.getResult(0, 0);
        assertNotNull(result);
        assertEquals(0, result.getScore());
        assertEquals(Direction.NONE, result.getParent());
        result = sa.getResult(0, 1);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        result = sa.getResult(1, 0);
        assertNotNull(result);
        assertEquals(-1, result.getScore());
        assertEquals(Direction.UP, result.getParent());
        result = sa.getResult(1, 1);
        assertNotNull(result);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
    }

    @Test
    void doubletons() {
        SequenceAligner sa;
        Result result;
        sa = new SequenceAligner("AG", "AG");
        assertEquals(-2, sa.getResult(0, 2).getScore());
        assertEquals(-2, sa.getResult(2, 0).getScore());
        assertEquals(Direction.LEFT, sa.getResult(0, 2).getParent());
        assertEquals(Direction.UP, sa.getResult(2, 0).getParent());
        result = sa.getResult(1, 1);
        assertEquals(2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(1, 2);
        assertEquals(1, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        result = sa.getResult(2, 1);
        assertEquals(1, result.getScore());
        assertEquals(Direction.UP, result.getParent());
        result = sa.getResult(2, 2);
        assertEquals(4, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        sa = new SequenceAligner("AG", "GA");
        assertEquals(-2, sa.getResult(0, 2).getScore());
        assertEquals(-2, sa.getResult(2, 0).getScore());
        assertEquals(Direction.LEFT, sa.getResult(0, 2).getParent());
        assertEquals(Direction.UP, sa.getResult(2, 0).getParent());
        result = sa.getResult(1, 1);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(1, 2);
        assertEquals(1, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(2, 1);
        assertEquals(1, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(2, 2);
        assertEquals(0, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
    }

    @Test
    void oneTwos() {
        SequenceAligner sa;
        Result result;
        sa = new SequenceAligner("A", "AG");
        assertEquals(-2, sa.getResult(0, 2).getScore());
        assertEquals(Direction.LEFT, sa.getResult(0, 2).getParent());
        result = sa.getResult(1, 1);
        assertEquals(2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(1, 2);
        assertEquals(1, result.getScore());
        assertEquals(Direction.LEFT, result.getParent());
        sa = new SequenceAligner("AG", "G");
        assertEquals(-2, sa.getResult(2, 0).getScore());
        assertEquals(Direction.UP, sa.getResult(2, 0).getParent());
        result = sa.getResult(1, 1);
        assertEquals(-2, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
        result = sa.getResult(2, 1);
        assertEquals(1, result.getScore());
        assertEquals(Direction.DIAGONAL, result.getParent());
    }

    /**
     * ******************************************** Try running the Driver to see what happens.
     * ********************************************
     */
    @Test
    void bigBases() {
        SequenceAligner sa;
        sa = new SequenceAligner(1000);
        for (int i = 0; i < sa.getX().length() + 1; i++) {
            assertEquals(-i, sa.getResult(i, 0).getScore());
        }
        for (int j = 0; j < sa.getY().length() + 1; j++) {
            assertEquals(-j, sa.getResult(0, j).getScore());
        }
        sa = new SequenceAligner("AAAAAAAAAA", "CCCCCCCCCCCC", new Judge(4, 5, 6));
        for (int i = 0; i < sa.getX().length() + 1; i++) {
            assertEquals(6 * i, sa.getResult(i, 0).getScore());
        }
        for (int j = 0; j < sa.getY().length() + 1; j++) {
            assertEquals(6 * j, sa.getResult(0, j).getScore());
        }
    }

    /**
     * ******************************************** Testing SequenceAligner.traceback()
     * ********************************************
     */
    @Test
    void simpleAlignment() {
        SequenceAligner sa;
        sa = new SequenceAligner("ACGT", "ACGT");
        assertTrue(sa.isAligned());
        assertEquals("ACGT", sa.getAlignedX());
        assertEquals("ACGT", sa.getAlignedY());
        sa = new SequenceAligner("ACT", "ACGT");
        assertTrue(sa.isAligned());
        assertEquals("AC_T", sa.getAlignedX());
        assertEquals("ACGT", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "CGT");
        assertTrue(sa.isAligned());
        assertEquals("ACGT", sa.getAlignedX());
        assertEquals("_CGT", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "AGT");
        assertTrue(sa.isAligned());
        assertEquals("ACGT", sa.getAlignedX());
        assertEquals("A_GT", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "ACT");
        assertTrue(sa.isAligned());
        assertEquals("ACGT", sa.getAlignedX());
        assertEquals("AC_T", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "ACG");
        assertTrue(sa.isAligned());
        assertEquals("ACGT", sa.getAlignedX());
        assertEquals("ACG_", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "AC");
        assertTrue(sa.isAligned());
        assertEquals("AC__", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "CG");
        assertTrue(sa.isAligned());
        assertEquals("_CG_", sa.getAlignedY());
        sa = new SequenceAligner("ACGT", "GT");
        assertTrue(sa.isAligned());
        assertEquals("__GT", sa.getAlignedY());
        sa = new SequenceAligner("A", "ACGT");
        assertEquals("A___", sa.getAlignedX());
        sa = new SequenceAligner("C", "ACGT");
        assertEquals("_C__", sa.getAlignedX());
        sa = new SequenceAligner("G", "ACGT");
        assertEquals("__G_", sa.getAlignedX());
        sa = new SequenceAligner("T", "ACGT");
        assertEquals("___T", sa.getAlignedX());
        sa = new SequenceAligner("AA", "A");
        assertEquals("_A", sa.getAlignedY());
        System.out.println(sa);
        sa = new SequenceAligner("AACCGGTT", "ACGT");
        assertEquals("_A_C_G_T", sa.getAlignedY());
        sa = new SequenceAligner("AAGGTT", "AACCGG");
        assertEquals("AA__GGTT", sa.getAlignedX());
        assertEquals("AACCGG__", sa.getAlignedY());
    }

    @Test
    void pathMarks() {
        SequenceAligner sa;
        sa = new SequenceAligner("AGACG", "CCGCT");
        assertEquals("_AGACG", sa.getAlignedX());
        assertEquals("CCG_CT", sa.getAlignedY());
        // check that start and end are on the path
        assertTrue(sa.getResult(0, 0).onPath());
        assertTrue(sa.getResult(5, 5).onPath());
        int[][] expectedScores = {
            {0, -1, -2, -3, -4, -5},
            {-1, -2, -3, -4, -5, -6},
            {-2, -3, -4, -1, -2, -3},
            {-3, -4, -5, -2, -3, -4},
            {-4, -1, -2, -3, 0, -1},
            {-5, -2, -3, 0, -1, -2},
        };
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                assertEquals(expectedScores[i][j], sa.getResult(i, j).getScore());
            }
        }
        // expected coords on optimal path
        int[] is = {0, 0, 1, 2, 3, 4, 5};
        int[] js = {0, 1, 2, 3, 3, 4, 5};
        int k = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == is[k] && j == js[k]) {
                    assertTrue(sa.getResult(i, j).onPath());
                    k++;
                } else {
                    assertFalse(sa.getResult(i, j).onPath());
                }
            }
        }
    }

    /** ******************************************** Benchmarks ******************************************** */
    @Test
    void runBenchmarks() {
        System.out.println("Running Test Suite:\n");
        String filename = Constants.SUITE_FILENAME;
        SequenceAligner strands;
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader in = new BufferedReader(reader);
            String testCase;
            int testNo = 1;
            while ((testCase = in.readLine()) != null) {
                System.out.println("Test " + testNo++ + ":");
                String[] field = testCase.split(",");
                String X = field[0], Y = field[1];
                System.out.println("X = " + X + "\nY = " + Y + "\n");
                strands = new SequenceAligner(X, Y);
                int expectedScore = Integer.parseInt(field[2]);
                System.out.println(strands + " (expecting " + expectedScore + ")\n");
                assert strands.getScore() == expectedScore;
                assert strands.getAlignedX().replaceAll("_", "").equals(strands.getX());
                assert strands.getAlignedY().replaceAll("_", "").equals(strands.getY());
                System.out.println("------------------\n");
            }
            in.close();
            System.out.println("All tests passed...\n");
        } catch (FileNotFoundException e) {
            System.out.println(
                    "The test suite file, " + filename + ", is not found.\nYou probably deleted it by mistake.");
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}

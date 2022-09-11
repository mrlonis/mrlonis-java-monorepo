package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class Testing {

    private static final Random random = new Random();
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

    @Test
    public void testEmpty() {
        System.out.println("testEmpty");
        match("", "");
        match("", "ab");
        System.out.println();
    }

    private static void match(String pattern,
                              String text) {
        // run all three algorithms and test for correctness
        Result ansNaive = StringMatch.matchNaive(pattern, text);
        int expected = text.indexOf(pattern);
        assertEquals(expected, ansNaive.pos);
        Result ansKMP = StringMatch.matchKMP(pattern, text);
        assertEquals(expected, ansKMP.pos);
        Result ansBoyerMoore = StringMatch.matchBoyerMoore(pattern, text);
        assertEquals(expected, ansBoyerMoore.pos);
        System.out.println(String.format("%5d %5d %5d : %s", ansNaive.comps, ansKMP.comps, ansBoyerMoore.comps,
                                         (ansNaive.comps < ansKMP.comps && ansNaive.comps < ansBoyerMoore.comps) ? "Naive" :
                                         (ansKMP.comps < ansNaive.comps && ansKMP.comps < ansBoyerMoore.comps) ? "KMP" : "Boyer-Moore"));
    }

    @Test
    public void testOneChar() {
        System.out.println("testOneChar");
        match("a", "a");
        match("a", "b");
        System.out.println();
    }

    @Test
    public void testRepeat() {
        System.out.println("testRepeat");
        match("aaa", "aaaaa");
        match("aaa", "abaaba");
        match("abab", "abacababc");
        match("abab", "babacaba");
        System.out.println();
    }

    @Test
    public void testPartialRepeat() {
        System.out.println("testPartialRepeat");
        match("aaacaaaaac", "aaacacaacaaacaaaacaaaaac");
        match("ababcababdabababcababdaba", "ababcababdabababcababdaba");
        System.out.println();
    }

    // @Test
    public void testRandomly() {
        System.out.println("testRandomly");
        for (int i = 0; i < 100; i++) {
            String pattern = makeRandomPattern();
            for (int j = 0; j < 100; j++) {
                String text = makeRandomText(pattern);
                match(pattern, text);
            }
        }
        System.out.println();
    }

    private static String makeRandomPattern() {
        StringBuilder sb = new StringBuilder();
        int steps = random.nextInt(10) + 1;
        for (int i = 0; i < steps; i++) {
            if (sb.length() == 0 || random.nextBoolean()) { // Add literal
                int len = random.nextInt(5) + 1;
				for (int j = 0; j < len; j++) {
					sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
				}
            } else { // Repeat prefix
                int len = random.nextInt(sb.length()) + 1;
                int reps = random.nextInt(3) + 1;
				if (sb.length() + len * reps > 1000) {
					break;
				}
				for (int j = 0; j < reps; j++) {
					sb.append(sb.substring(0, len));
				}
            }
        }
        return sb.toString();
    }

    private static String makeRandomText(String pattern) {
        StringBuilder sb = new StringBuilder();
        int steps = random.nextInt(100);
        for (int i = 0; i < steps && sb.length() < 10000; i++) {
            if (random.nextDouble() < 0.7) { // Add prefix of pattern
                int len = random.nextInt(pattern.length()) + 1;
                sb.append(pattern, 0, len);
            } else { // Add literal
                int len = random.nextInt(30) + 1;
				for (int j = 0; j < len; j++) {
					sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
				}
            }
        }
        return sb.toString();
    }

    /**
     * Tests Added after Lab 13
     */
    @Test
    public void lec13aNaive() {
        String[] pats = new String[]{"AAAA", "BAAA", "AAAB", "AAAC", "ABAB"};
        String text = "AAAAAAAAABAAAAAAAAAB";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 4), new Result(9, 13), new Result(6, 28), new Result(-1, 62), new Result(-1, 35),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchNaive(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void lec13bNaive() {
        String[] pats = new String[]{"AABC", "ABCDE", "AABAACAABABA", "ABRACADABRA",};
        String text = "AAAAAABRACADABAAAAAAAAAAAAAAAAAAAAAAABCAAAAAAAAAAABAABAAAAAAAAAAAAAAA";
        assertEquals(69, text.length());
        Result[] results = new Result[]{new Result(35, 96), new Result(-1, 127), new Result(-1, 157), new Result(-1, 121),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchNaive(pat, text);
            assertEquals(results[i].pos, res.pos);
            assertEquals(results[i].comps, res.comps);
            i++;
        }
    }

    @Test
    public void lec14aNaive() {
        String[] pats = new String[]{"00000", "11111", "00011", "10100", "10000",};
        String text = "00000000000000000000";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 5), new Result(-1, 16), new Result(-1, 64), new Result(-1, 16), new Result(-1, 16),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchNaive(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void lec13aBulidKMP() {
        String[] pats = new String[]{"AAAA", "BAAA", "AAAB", "AAAC", "ABAB",};

        int[][] flinks = new int[][]{new int[5], new int[5], new int[5], new int[5], new int[5]};

        int[] comps = new int[]{3, 3, 5, 5, 3};

        int[][] results = new int[][]{{-1, 0, 1, 2, 3}, {-1, 0, 0, 0, 0}, {-1, 0, 1, 2, 0}, {-1, 0, 1, 2, 0}, {-1, 0, 0, 1, 2}};

        int i = 0;
        for (String pat : pats) {
            int ans = StringMatch.buildKMP(pat, flinks[i]);
            assertTrue(Arrays.equals(results[i], flinks[i]));
            assertEquals(comps[i], ans);
            i++;
        }
    }

    @Test
    public void lec13bBuildKMP() {
        String[] pats = new String[]{"AABC", "ABCDE", "AABAACAABABA", "ABRACADABRA",};
        int[][] flinks = new int[][]{{-1, 0, 1, 0, 0}, {-1, 0, 0, 0, 0, 0}, {-1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1}, {-1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4},};
        int[][] results = new int[][]{new int[5], new int[6], new int[13], new int[12]};
        int[] comps = new int[]{4, 4, 16, 12};
        int i = 0;
        for (String pat : pats) {
            int res = StringMatch.buildKMP(pat, results[i]);
            assertTrue(Arrays.equals(flinks[i], results[i]));
            assertEquals(comps[i], res);
            i++;
        }
    }

    @Test
    public void lec14aBuildKMP() {
        String[] pats = new String[]{"00000", "11111", "00011", "10100", "10000",};

        int[][] flinks = new int[][]{new int[6], new int[6], new int[6], new int[6], new int[6]};

        int[] comps = new int[]{4, 4, 6, 5, 4};

        int[][] flinkResults = new int[][]{{-1, 0, 1, 2, 3, 4}, {-1, 0, 1, 2, 3, 4}, {-1, 0, 1, 2, 0, 0}, {-1, 0, 0, 1, 2, 0}, {-1, 0, 0, 0, 0, 0}};

        int i = 0;
        for (String pat : pats) {
            int ans = StringMatch.buildKMP(pat, flinks[i]);
            assertTrue(Arrays.equals(flinkResults[i], flinks[i]));
            assertEquals(comps[i], ans);
            i++;
        }
    }

    @Test
    public void lec13aKMP() {
        String[] pats = new String[]{"AAAA", "BAAA", "AAAB", "AAAC", "ABAB",};
        String text = "AAAAAAAAABAAAAAAAAAB";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 7), new Result(9, 16), new Result(6, 21), new Result(-1, 43), new Result(-1, 40),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchKMP(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void lec13bKMP() {
        String[] pats = new String[]{"AABC", "ABCDE", "AABAACAABABA", "ABRACADABRA",};
        int[][] flinks = new int[][]{{-1, 0, 1, 0, 0}, {-1, 0, 0, 0, 0, 0}, {-1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1}, {-1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4},};
        String text = "AAAAAABRACADABAAAAAAAAAAAAAAAAAAAAAAABCAAAAAAAAAAABAABAAAAAAAAAAAAAAA";
        assertEquals(69, text.length());
        Result[] results = new Result[]{new Result(35, 68), new Result(-1, 128), new Result(-1, 123), new Result(-1, 126),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.runKMP(pat, text, flinks[i]);
            assertEquals(results[i].pos, res.pos);
            assertEquals(results[i].comps, res.comps);
            i++;
        }
    }

    @Test
    public void lec14aKMP() {
        String[] pats = new String[]{"00000", "11111", "00011", "10100", "10000",};
        String text = "00000000000000000000";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 9), new Result(-1, 24), new Result(-1, 43), new Result(-1, 25), new Result(-1, 24),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchKMP(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void smallMachines() {
        String[] pats = new String[]{"", "A", "AB", "AA", "AAAA", "BAAA", "AAAB", "AAAC", "ABAB", "ABCD", "ABBA", "AABC", "ABAAB", "AABAACAABABA", "ABRACADABRA",};
        int[][] flinks = new int[][]{{-1}, {-1, 0}, {-1, 0, 0}, {-1, 0, 1}, {-1, 0, 1, 2, 3}, {-1, 0, 0, 0, 0}, {-1, 0, 1, 2, 0}, {-1, 0, 1, 2, 0}, {-1, 0, 0, 1, 2},
                                     {-1, 0, 0, 0, 0}, {-1, 0, 0, 0, 1}, {-1, 0, 1, 0, 0}, {-1, 0, 0, 1, 1, 2}, {-1, 0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 0, 1},
                                     {-1, 0, 0, 0, 1, 0, 1, 0, 1, 2, 3, 4},};
        int[] comps = new int[]{0, 0, 1, 1, 3, 3, 5, 5, 3, 3, 3, 4, 5, 16, 12};
        int i = 0;
        for (String pat : pats) {
            int[] flink = new int[pat.length() + 1];
            assertEquals(comps[i], StringMatch.buildKMP(pat, flink));
            assertArrayEquals(flinks[i], flink);
            i++;
        }
    }

    @Test
    public void lec13aBulidBoyerMoore() {
        String[] pats = new String[]{"AAAA", "BAAA", "AAAB", "AAAC", "ABAB",};

        int[][] delta1s = new int[][]{new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE],
                                      new int[Constants.SIGMA_SIZE]};

        int[][] results = new int[][]{
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}};

        int i = 0;
        for (String pat : pats) {
            StringMatch.buildDelta1(pat, delta1s[i]);
            assertTrue(Arrays.equals(results[i], delta1s[i]));
            i++;
        }
    }

    @Test
    public void lec13bBulidBoyerMoore() {
        String[] pats = new String[]{"AABC", "ABCDE", "AABAACAABABA", "ABRACADABRA",};

        int[][] delta1s = new int[][]{new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE]};

        int[][] results = new int[][]{
                {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 1, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
                 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 4, 3, 2, 1, 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                {12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
                 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 0, 1, 6, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
                 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
                 12, 12, 12, 12, 12},
                {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
                 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 0, 2, 6, 4, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 1,
                 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11,
                 11, 11, 11, 11}};

        int i = 0;
        for (String pat : pats) {
            StringMatch.buildDelta1(pat, delta1s[i]);
            assertTrue(Arrays.equals(results[i], delta1s[i]));
            i++;
        }
    }

    @Test
    public void lec14aBuildBoyerMoore() {
        String[] pats = new String[]{"00000", "11111", "00011", "10100", "10000",};

        int[][] delta1s = new int[][]{new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE], new int[Constants.SIGMA_SIZE],
                                      new int[Constants.SIGMA_SIZE]};

        int[][] results = new int[][]{
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 0, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 2, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0, 4, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
                 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5}};

        int i = 0;
        for (String pat : pats) {
            StringMatch.buildDelta1(pat, delta1s[i]);
            assertTrue(Arrays.equals(results[i], delta1s[i]));
            i++;
        }
    }

    @Test
    public void lec13aBoyerMoore() {
        String[] pats = new String[]{"AAAA", "BAAA", "AAAB", "AAAC", "ABAB",};
        String text = "AAAAAAAAABAAAAAAAAAB";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 4), new Result(9, 29), new Result(6, 10), new Result(-1, 14), new Result(-1, 21),};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchBoyerMoore(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void lec13bBoyerMoore() {
        String[] pats = new String[]{"AABC", "ABCDE", "AABAACAABABA", "ABRACADABRA",};
        String text = "AAAAAABRACADABAAAAAAAAAAAAAAAAAAAAAAABCAAAAAAAAAAABAABAAAAAAAAAAAAAAA";
        assertEquals(69, text.length());
        Result[] results = new Result[]{new Result(35, 20), new Result(-1, 17), new Result(-1, 83), new Result(-1, 93)};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchBoyerMoore(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void lec14aBoyerMoore() {
        String[] pats = new String[]{"00000", "11111", "00011", "10100", "10000",};
        String text = "00000000000000000000";
        assertEquals(20, text.length());
        Result[] results = new Result[]{new Result(0, 5), new Result(-1, 4), new Result(-1, 8), new Result(-1, 48), new Result(-1, 80)};
        int i = 0;
        for (String pat : pats) {
            Result res = StringMatch.matchBoyerMoore(pat, text);
            assertEquals(res.pos, results[i].pos);
            assertEquals(res.comps, results[i].comps);
            i++;
        }
    }

    @Test
    public void mediumNaive() {
        String pattern = "ABCABCDEBACBDACBTBAC";
        String text = "AAAAABBBBBCCCCCDDDDDABCABCDEBACBDACBTBAC";
        assertEquals(pattern.length(), 20);
        assertEquals(text.length(), 40);
        Result result = new Result(20, 46);
        Result res = StringMatch.matchNaive(pattern, text);
        assertEquals(result.pos, res.pos);
        assertEquals(result.comps, res.comps);
    }

    @Test
    public void mediumBuildKMP() {
        String pattern = "ABCABCDEBACBDACBTBAC";
        int[] flink = new int[21];
        int[] result = new int[]{-1, 0, 0, 0, 1, 2, 3, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0};
        int ansCount = 23;
        assertEquals(pattern.length(), 20);
        assertEquals(flink.length, 21);
        assertEquals(result.length, 21);
        int count = StringMatch.buildKMP(pattern, flink);
        assertEquals(ansCount, count);
        assertTrue(Arrays.equals(flink, result));
    }

    @Test
    public void mediumKMP() {
        String pattern = "ABCABCDEBACBDACBTBAC";
        String text = "AAAAABBBBBCCCCCDDDDDABCABCDEBACBDACBTBAC";
        assertEquals(pattern.length(), 20);
        assertEquals(text.length(), 40);
        Result result = new Result(20, 68);
        Result res = StringMatch.matchKMP(pattern, text);
        assertEquals(result.pos, res.pos);
        assertEquals(result.comps, res.comps);
    }

    @Test
    public void mediumBuildDelta1() {
        String pattern = "ABCABCDEBACBDACBTBAC";
        int[] flink = new int[Constants.SIGMA_SIZE];
        int[] result = new int[]{20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
                                 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 1, 2, 0, 7, 12, 20, 20, 20, 20, 20,
                                 20, 20, 20, 20, 20, 20, 20, 20, 20, 3, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20,
                                 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20};
        assertEquals(pattern.length(), 20);
        assertEquals(flink.length, 128);
        assertEquals(result.length, 128);
        StringMatch.buildDelta1(pattern, flink);
        assertTrue(Arrays.equals(flink, result));
    }

    @Test
    public void mediumBoyerMoore() {
        String pattern = "ABCABCDEBACBDACBTBAC";
        String text = "AAAAABBBBBCCCCCDDDDDABCABCDEBACBDACBTBAC";
        assertEquals(pattern.length(), 20);
        assertEquals(text.length(), 40);
        Result result = new Result(20, 26);
        Result res = StringMatch.matchBoyerMoore(pattern, text);
        assertEquals(result.pos, res.pos);
        assertEquals(result.comps, res.comps);
    }

    // @Test
    public void largeNaive() {
        for (int i = 0; i < 100; i++) {
            String pattern = makeRandomPattern();
            for (int j = 0; j < 100; j++) {
                String text = makeRandomText(pattern);
                int ans = text.indexOf(pattern);
                int count = text.length() * pattern.length();
                Result res = StringMatch.matchNaive(pattern, text);
                assertEquals(ans, res.pos);
                assertTrue(res.comps <= count);
            }
        }
    }

    /* Helper functions */

    // @Test
    public void largeKMP() {
        for (int i = 0; i < 100; i++) {
            String pattern = makeRandomPattern();
            for (int j = 0; j < 100; j++) {
                String text = makeRandomText(pattern);
                int ans = text.indexOf(pattern);
                Result res = StringMatch.matchKMP(pattern, text);
                assertEquals(ans, res.pos);
            }
        }
    }

    // @Test
    public void largeBoyerMoore() {
        for (int i = 0; i < 100; i++) {
            String pattern = makeRandomPattern();
            for (int j = 0; j < 100; j++) {
                String text = makeRandomText(pattern);
                int ans = text.indexOf(pattern);
                int count = text.length() * text.length();
                Result res = StringMatch.matchBoyerMoore(pattern, text);
                assertEquals(ans, res.pos);
                assertTrue(res.comps <= count);
            }
        }
    }

    // @Test
    public void matchBotRealDonaldTrump() {
        /*
         * Set the user to be creeped on.
         */
        String handle = "realDonaldTrump", pattern = "China";
        System.out.println("Creeping on @" + handle + " and looking for the pattern " + pattern + "!");
        MatchBot bot = new MatchBot(handle, 500);

        /*
         * Search all tweets for the pattern.
         */
        List<String> ansNaive = new ArrayList<>();
        int compsNaive = bot.searchTweetsNaive(pattern, ansNaive);
        List<String> ansKMP = new ArrayList<>();
        int compsKMP = bot.searchTweetsKMP(pattern, ansKMP);
        List<String> ansBoyerMoore = new ArrayList<>();
        int compsBoyerMoore = bot.searchTweetsKMP(pattern, ansBoyerMoore);

        /*
         * Print out total comparisons for each algorithm.
         */
        System.out.println("naive comps = " + compsNaive + ", KMP comps = " + compsKMP + ", Moyer-Moore comps = " + compsBoyerMoore);

        /*
         * Test for consistency between Naive, KMP & Boyer-Moore
         */
        assert ansNaive.size() == ansKMP.size();
        assert ansBoyerMoore.size() == ansKMP.size();

        for (int i = 0; i < ansKMP.size(); i++) {
            String tweet = ansKMP.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
            System.out.println(i++ + ". " + tweet);
            System.out.println(pattern + " appears at index " + tweet.toLowerCase()
                                                                     .indexOf(pattern.toLowerCase()));
        }

        for (int i = 0; i < ansNaive.size(); i++) {
            String tweet = ansNaive.get(i);
            assert tweet.equals(ansKMP.get(i));
            assert tweet.equals(ansBoyerMoore.get(i));
        }

        for (int i = 0; i < ansBoyerMoore.size(); i++) {
            String tweet = ansBoyerMoore.get(i);
            assert tweet.equals(ansNaive.get(i));
            assert tweet.equals(ansKMP.get(i));
        }

        /*
         * Show stats for the algorithms on real-time tweets.
         */
        bot.tweets.forEach((String) -> {
            match(pattern, String);
        });
    }
}

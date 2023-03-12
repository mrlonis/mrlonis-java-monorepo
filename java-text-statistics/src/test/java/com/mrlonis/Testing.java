package com.mrlonis;

/**
 * To test with JUnit, add JUnit to your project. To do this, go to Project->Properties. Select "Java Build Path".
 * Select the "Libraries" tab and "Add Library". Select JUnit, then JUnit 4.
 *
 * @author Matthew Lonis (mrlonis)
 */

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testing {

    /**********************************************************************************
     * When you've reached this point, run the Driver and start writing your
     * report.
     **********************************************************************************/

    private static String makeUniformRandomText(int n) {
        Random rand = new Random();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int m = alpha.length();
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            ans.append(alpha.charAt(rand.nextInt(m)));
        }
        return ans.toString();
    }

    private static String makeBiasedRandomText(int n) {
        Random rand = new Random();
        String alphaFrequent = "ABCDEFGH";
        String alphaRare = "IJKLMNOPQRSTUVWXYZ";
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (rand.nextInt(10) == 0) {
                ans.append(alphaRare.charAt(rand.nextInt(alphaRare.length())));
            } else {
                ans.append(alphaFrequent.charAt(rand.nextInt(alphaFrequent.length())));
            }
        }
        return ans.toString();
    }

    /**
     * Note: These tests are not necessarily exhaustive, although they do
     * provide you with a good roadmap to follow in developing your code for
     * this project.
     *
     * Add some of your own tests, but do not remove the provided tests or
     * change any of the expected results.
     */

    @Test
    public void emptyFreqTable() {
        FrequencyTable ft = new FrequencyTable();
        assertEquals(0, ft.size());
        assertTrue(ft.isEmpty());
        assertTrue(0 == ft.get('a'));
        assertTrue(0 == ft.get('b'));
        assertTrue(0 == ft.get('c'));
    }

    @Test
    public void smallFreqTable() {
        FrequencyTable ft = new FrequencyTable("abacca");
        assertEquals(3, ft.size());
        assertFalse(ft.isEmpty());
        assertTrue(3 == ft.get('a'));
        assertTrue(1 == ft.get('b'));
        assertTrue(2 == ft.get('c'));
    }

    @Test
    public void mediumFreqTable() {
        FrequencyTable ft = new FrequencyTable("a");
        assertTrue(1 == ft.get('a'));
        ft = new FrequencyTable("aaaaaaaa");
        assertTrue(8 == ft.get('a'));
        ft = new FrequencyTable("ab");
        assertTrue(1 == ft.get('a'));
        assertTrue(1 == ft.get('b'));
        assertTrue(0 == ft.get('c'));
        assertTrue(0 == ft.get('d'));
    }

    /**********************************************************************************
     * We leave it to you to write tests for Heap.swap(), Heap.siftUp(), and
     * Heap.siftDown(). Here would be a good place to put those tests.
     **********************************************************************************/

    @Test
    public void bigFreqTable() {
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        alpha += alpha.toUpperCase();
        Random gen = new Random();
        int[] freqs = new int[alpha.length()];
        for (int i = 0; i < freqs.length; i++) {
            freqs[i] = gen.nextInt(1000);
        }
        String text = "";
        for (int i = 0; i < alpha.length(); i++) {
            for (int j = 0; j < freqs[i]; j++) {
                text += alpha.charAt(i);
            }
        }
        // Do some cuts.
        for (int i = 0; i < freqs.length; i++) {
            int pos = gen.nextInt(text.length());
            text = text.substring(pos) + text.substring(0, pos);
        }
        FrequencyTable ft = new FrequencyTable(text);
        for (int i = 0; i < alpha.length(); i++) {
            assertTrue(freqs[i] == ft.get(alpha.charAt(i)));
        }
        String other = "0123456789!@#$%^&*()";
        for (int i = 0; i < other.length(); i++) {
            assertTrue(0 == ft.get(other.charAt(i)));
        }
    }

    @Test
    public void heapUtils() {
        /**
         * 0 / \ 1 2 / \ / 3 4 5
         */
        assertEquals(1, Heap.getLeft(0));
        assertEquals(3, Heap.getLeft(1));
        assertEquals(5, Heap.getLeft(2));
        assertEquals(2, Heap.getRight(0));
        assertEquals(4, Heap.getRight(1));
        assertEquals(0, Heap.getParent(1));
        assertEquals(0, Heap.getParent(2));
        assertEquals(1, Heap.getParent(3));
        assertEquals(1, Heap.getParent(4));
        assertEquals(2, Heap.getParent(5));
    }

    /**
     * Testing Heap.swap()
     */
    @Test
    public void swapTest() {
        Heap<Integer> heap = new Heap<>((x, y) -> y.compareTo(x));
        for (int i = 0; i < 2; i++) {
            heap.keys.add(i);
        }
        assertEquals(2, heap.size());
        heap.swap(0, 1);
        assertEquals((Integer) 1, heap.keys.get(0));
        assertEquals((Integer) 0, heap.keys.get(1));

        heap = new Heap<>((x, y) -> y.compareTo(x));
        for (int i = 0; i < 10; i++) {
            heap.keys.add(i);
        }
        assertEquals(10, heap.size());
        for (int i = 0; i < 9; i++) {
            heap.swap(i, i + 1);
        }

        assertEquals((Integer) 1, heap.keys.get(0));
        assertEquals((Integer) 2, heap.keys.get(1));
        assertEquals((Integer) 3, heap.keys.get(2));
        assertEquals((Integer) 4, heap.keys.get(3));
        assertEquals((Integer) 5, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 7, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 9, heap.keys.get(8));
        assertEquals((Integer) 0, heap.keys.get(9));

        heap.swap(3, 5);

        assertEquals((Integer) 6, heap.keys.get(3));
        assertEquals((Integer) 4, heap.keys.get(5));
    }

    /**
     * Heap.siftDown() Testing
     */
    @Test
    public void siftDownTest() {
        Heap<Integer> heap = new Heap<>((x, y) -> y.compareTo(x));
        for (int i = 0; i < 10; i++) {
            heap.keys.add(i);
        }
        assertEquals(10, heap.size());
        heap.siftDown(0);
        assertEquals((Integer) 2, heap.keys.get(0));
        assertEquals((Integer) 1, heap.keys.get(1));
        assertEquals((Integer) 6, heap.keys.get(2));
        assertEquals((Integer) 3, heap.keys.get(3));
        assertEquals((Integer) 4, heap.keys.get(4));
        assertEquals((Integer) 5, heap.keys.get(5));
        assertEquals((Integer) 0, heap.keys.get(6));
        assertEquals((Integer) 7, heap.keys.get(7));
        assertEquals((Integer) 8, heap.keys.get(8));
        assertEquals((Integer) 9, heap.keys.get(9));

        heap.siftDown(0);
        assertEquals((Integer) 6, heap.keys.get(0));
        assertEquals((Integer) 1, heap.keys.get(1));
        assertEquals((Integer) 5, heap.keys.get(2));
        assertEquals((Integer) 3, heap.keys.get(3));
        assertEquals((Integer) 4, heap.keys.get(4));
        assertEquals((Integer) 2, heap.keys.get(5));
        assertEquals((Integer) 0, heap.keys.get(6));
        assertEquals((Integer) 7, heap.keys.get(7));
        assertEquals((Integer) 8, heap.keys.get(8));
        assertEquals((Integer) 9, heap.keys.get(9));

        heap.siftDown(1);
        assertEquals((Integer) 6, heap.keys.get(0));
        assertEquals((Integer) 4, heap.keys.get(1));
        assertEquals((Integer) 5, heap.keys.get(2));
        assertEquals((Integer) 3, heap.keys.get(3));
        assertEquals((Integer) 9, heap.keys.get(4));
        assertEquals((Integer) 2, heap.keys.get(5));
        assertEquals((Integer) 0, heap.keys.get(6));
        assertEquals((Integer) 7, heap.keys.get(7));
        assertEquals((Integer) 8, heap.keys.get(8));
        assertEquals((Integer) 1, heap.keys.get(9));
    }

    /**
     * Heap.siftUp() Testing
     */
    @Test
    public void siftUpTest() {
        Heap<Integer> heap = new Heap<>((x, y) -> x.compareTo(y));
        for (int i = 1; i < 11; i++) {
            heap.keys.add(i);
        }
        assertEquals(10, heap.size());
        heap.keys.add(0);
        assertEquals(11, heap.size());
        heap.siftUp(10);
        assertEquals((Integer) 0, heap.keys.get(0));
        assertEquals((Integer) 1, heap.keys.get(1));
        assertEquals((Integer) 3, heap.keys.get(2));
        assertEquals((Integer) 4, heap.keys.get(3));
        assertEquals((Integer) 2, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 7, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 9, heap.keys.get(8));
        assertEquals((Integer) 10, heap.keys.get(9));
        assertEquals((Integer) 5, heap.keys.get(10));

        heap = new Heap<>((x, y) -> y.compareTo(x));
        for (int i = 1; i < 11; i++) {
            heap.keys.add(i);
        }
        assertEquals(10, heap.size());
        heap.keys.add(100);
        assertEquals(11, heap.size());
        heap.siftUp(10);
        assertEquals((Integer) 100, heap.keys.get(0));
        assertEquals((Integer) 1, heap.keys.get(1));
        assertEquals((Integer) 3, heap.keys.get(2));
        assertEquals((Integer) 4, heap.keys.get(3));
        assertEquals((Integer) 2, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 7, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 9, heap.keys.get(8));
        assertEquals((Integer) 10, heap.keys.get(9));
        assertEquals((Integer) 5, heap.keys.get(10));

        heap.siftUp(9);
        assertEquals((Integer) 100, heap.keys.get(0));
        assertEquals((Integer) 10, heap.keys.get(1));
        assertEquals((Integer) 3, heap.keys.get(2));
        assertEquals((Integer) 4, heap.keys.get(3));
        assertEquals((Integer) 1, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 7, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 9, heap.keys.get(8));
        assertEquals((Integer) 2, heap.keys.get(9));
        assertEquals((Integer) 5, heap.keys.get(10));

        heap.siftUp(8);
        assertEquals((Integer) 100, heap.keys.get(0));
        assertEquals((Integer) 10, heap.keys.get(1));
        assertEquals((Integer) 3, heap.keys.get(2));
        assertEquals((Integer) 9, heap.keys.get(3));
        assertEquals((Integer) 1, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 7, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 4, heap.keys.get(8));
        assertEquals((Integer) 2, heap.keys.get(9));
        assertEquals((Integer) 5, heap.keys.get(10));

        heap.siftUp(6);
        assertEquals((Integer) 100, heap.keys.get(0));
        assertEquals((Integer) 10, heap.keys.get(1));
        assertEquals((Integer) 7, heap.keys.get(2));
        assertEquals((Integer) 9, heap.keys.get(3));
        assertEquals((Integer) 1, heap.keys.get(4));
        assertEquals((Integer) 6, heap.keys.get(5));
        assertEquals((Integer) 3, heap.keys.get(6));
        assertEquals((Integer) 8, heap.keys.get(7));
        assertEquals((Integer) 4, heap.keys.get(8));
        assertEquals((Integer) 2, heap.keys.get(9));
        assertEquals((Integer) 5, heap.keys.get(10));
    }

    @Test
    public void smallMinHeap() {
        Heap<Integer> heap = new Heap<>((x, y) -> x.compareTo(y));
        Comparator<Integer> comp = heap.comparator();
        assertTrue(comp.compare(2, 3) < 0);
        assertTrue(comp.compare(4, 4) == 0);
        assertTrue(comp.compare(5, 4) > 0);
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        heap.insert(5);
        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());
        assertTrue(5 == heap.peek());
        assertEquals(1, heap.size());
        assertTrue(5 == heap.delete());
        assertTrue(heap.isEmpty());
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        assertEquals(3, heap.size());
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
        heap.insert(1);
        heap.insert(3);
        heap.insert(2);
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
        heap.insert(2);
        heap.insert(3);
        heap.insert(1);
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
        heap.insert(3);
        heap.insert(2);
        heap.insert(1);
        assertTrue(1 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(3 == heap.delete());
    }

    @Test
    public void smallMaxHeap() {
        Heap<Integer> heap = new Heap<>((x, y) -> y.compareTo(x));
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
        heap.insert(1);
        heap.insert(3);
        heap.insert(2);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
        heap.insert(2);
        heap.insert(1);
        heap.insert(3);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
        heap.insert(2);
        heap.insert(3);
        heap.insert(1);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
        heap.insert(3);
        heap.insert(1);
        heap.insert(2);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
        heap.insert(3);
        heap.insert(2);
        heap.insert(1);
        assertTrue(3 == heap.delete());
        assertTrue(2 == heap.delete());
        assertTrue(1 == heap.delete());
    }

    @Test
    public void heapOfStrings() {
        // Shorter strings come first.
        Heap<String> heap = new Heap<>((s, t) -> s.length() - t.length());
        Comparator<String> comp = heap.comparator();
        assertTrue(comp.compare("zz", "aaaa") < 0);
        assertTrue(comp.compare("cat", "dog") == 0);
        assertTrue(comp.compare("bbbbbb", "aa") > 0);
        heap.insert("aaa");
        heap.insert("bb");
        heap.insert("cccccc");
        assertEquals("bb", heap.delete());
        assertEquals("aaa", heap.delete());
        assertEquals("cccccc", heap.delete());
    }

    @Test
    public void mediumMinHeap() {
        Heap<Integer> heap = new Heap<>((x, y) -> x.compareTo(y));
        for (int x : new int[]{9, 3, 4, 7, 6, 8, 2, 1, 5, 10}) {
            heap.insert(x);
        }
        for (int i = 1; i <= 10; i++) {
            assertTrue(i == heap.peek());
            assertTrue(i == heap.delete());
        }
    }

    @Test
    public void heapFormation() {
        Heap<Integer> heap = new Heap<>((x, y) -> y.compareTo(x));
        for (int x : new int[]{14, 18, 9, 3, 12, 4, 19, 7, 11, 6, 16, 8, 13, 2, 20, 1, 5, 10, 15, 17}) {
            heap.insert(x);
        }
        assertEquals("[20, 17, 19, 15, 16, 13, 18, 5, 11, 14, 12, 4, 8, 2, 9, 1, 3, 7, 10, 6]", heap.toString());
        for (int i = heap.size(); i >= 1; i--) {
            assertTrue(i == heap.peek());
            assertTrue(i == heap.delete());
        }
        assertTrue(heap.isEmpty());
    }

    @Test
    public void smallHuff() {
        FrequencyTable ft = new FrequencyTable("aaaabbc");
        assertTrue(4 == ft.get('a'));
        assertTrue(2 == ft.get('b'));
        assertTrue(1 == ft.get('c'));
        HuffmanTree ht = new HuffmanTree(ft);
        assertTrue(7 == ht.root.priority);
        assertTrue(3 == ht.root.left.priority);
        assertTrue(4 == ht.root.right.priority);
        assertTrue(1 == ht.root.left.left.priority);
        assertTrue(2 == ht.root.left.right.priority);
        assertTrue('a' == ht.root.right.key);
        assertTrue('c' == ht.root.left.left.key);
        assertTrue('b' == ht.root.left.right.key);
    }

    @Test
    public void lookupHuff() {
        HuffmanTree ht = new HuffmanTree(new FrequencyTable("aaaabbc"));
        assertEquals("1", ht.lookup('a'));
        assertEquals("01", ht.lookup('b'));
        assertEquals("00", ht.lookup('c'));
        try {
            ht.lookup('d');
        } catch (EncodeException e) {
            System.out.println("Caught Encode Exception!");
        }
    }

    @Test
    public void decodeHuff() {
        HuffmanTree ht = new HuffmanTree(new FrequencyTable("aaaabbc"));
        assertEquals('a', ht.decodeChar("1"));
        assertEquals('b', ht.decodeChar("01"));
        assertEquals('c', ht.decodeChar("00"));
        try {
            ht.decodeChar("0");
        } catch (DecodeException e) {
            System.out.println("Decode Exception Caught!");
        }
    }

    @Test
    public void catInHat() {
        HuffmanTree ht = new HuffmanTree(new FrequencyTable("THE CAT IN THE HAT"));
        assertTrue(18 == ht.root.priority);
        assertTrue(8 == ht.root.left.priority);
        assertTrue(10 == ht.root.right.priority);
        assertTrue(4 == ht.root.left.left.priority);
        assertTrue(4 == ht.root.left.right.priority);
        assertTrue(2 == ht.root.left.right.left.priority);
        assertTrue(2 == ht.root.left.right.right.priority);
        assertTrue(1 == ht.root.left.right.right.left.priority);
        assertTrue(1 == ht.root.left.right.right.right.priority);
        assertTrue(4 == ht.root.right.left.priority);
        assertTrue(6 == ht.root.right.right.priority);
        assertTrue(3 == ht.root.right.right.left.priority);
        assertTrue(3 == ht.root.right.right.right.priority);
        assertTrue(1 == ht.root.right.right.right.left.priority);
        assertTrue(2 == ht.root.right.right.right.right.priority);
        assertEquals("10", ht.lookup('T'));
        assertEquals("110", ht.lookup('H'));
        assertEquals("010", ht.lookup('E'));
        assertEquals("1110", ht.lookup('C'));
        assertEquals("1111", ht.lookup('A'));
        assertEquals("0111", ht.lookup('I'));
        assertEquals("0110", ht.lookup('N'));
        assertEquals("00", ht.lookup(' '));
        assertEquals('T', ht.decodeChar("10"));
        assertEquals('H', ht.decodeChar("110"));
        assertEquals('E', ht.decodeChar("010"));
        assertEquals('C', ht.decodeChar("1110"));
        assertEquals('A', ht.decodeChar("1111"));
        assertEquals('I', ht.decodeChar("0111"));
        assertEquals('N', ht.decodeChar("0110"));
        assertEquals(' ', ht.decodeChar("00"));
        assertEquals('T', ht.decodeChar("100000000"));
        assertEquals('H', ht.decodeChar("1100000000000"));
        assertEquals('E', ht.decodeChar("010000000000000"));
        assertEquals('C', ht.decodeChar("111000000000000"));
        assertEquals('A', ht.decodeChar("11110000000000000000"));
        assertEquals('I', ht.decodeChar("011100000000000000000000"));
        assertEquals('N', ht.decodeChar("011000000000000"));
        assertEquals(' ', ht.decodeChar("00000000000000"));
    }

    @Test
    public void simpleCodeBook() {
        String text =
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "bbbbbbbbbbbbb" + "ccccccccccc" + "dddddddddddddddd" +
                        "eeeeeeeee" + "fffff";
        CodeBook book = new CodeBook(text);
        assertEquals(6, book.size());
        assertEquals("0", book.encodeChar('a'));
        assertEquals("101", book.encodeChar('b'));
        assertEquals("100", book.encodeChar('c'));
        assertEquals("111", book.encodeChar('d'));
        assertEquals("1101", book.encodeChar('e'));
        assertEquals("1100", book.encodeChar('f'));
        HuffmanTree ht = book.getHuffmanTree();
        assertTrue('a' == ht.decodeChar("0"));
        assertTrue('b' == ht.decodeChar("101"));
        assertTrue('c' == ht.decodeChar("100"));
        assertTrue('d' == ht.decodeChar("111"));
        assertTrue('e' == ht.decodeChar("1101"));
        assertTrue('f' == ht.decodeChar("1100"));
        assertTrue('a' == ht.decodeChar("0101"));
        assertTrue('b' == ht.decodeChar("101101001010"));
        assertTrue('c' == ht.decodeChar("1001010101"));
        assertTrue('d' == ht.decodeChar("111001010101"));
        assertTrue('e' == ht.decodeChar("110100010101010"));
        assertTrue('f' == ht.decodeChar("110000101010101010101001"));
    }

    @Test
    public void notInCodeBook() {
        CodeBook book = new CodeBook("a");
        try {
            book.encodeChar('b');
        } catch (EncodeException e) {
            assertTrue(e.getMessage().endsWith("b"));
        }
        try {
            book.getHuffmanTree().decodeChar("101");
        } catch (DecodeException e) {
            assertTrue(e.getMessage().endsWith("101"));
        }
    }

    @Test
    public void englishCodeBook() {
        CodeBook english = new CodeBook();
        assertEquals("010", english.encodeChar('a'));
        assertEquals("0010", english.encodeChar('b'));
        assertEquals("10011", english.encodeChar('c'));
        assertEquals("01100", english.encodeChar('d'));
        assertEquals("00000", english.encodeChar('e'));
        assertEquals("11001", english.encodeChar('f'));
        assertEquals("110001", english.encodeChar('g'));
        assertEquals("1011", english.encodeChar('h'));
        assertEquals("1000", english.encodeChar('i'));
        assertEquals("0000110", english.encodeChar('j'));
        assertEquals("0000101", english.encodeChar('k'));
        assertEquals("01101", english.encodeChar('l'));
        assertEquals("0001", english.encodeChar('m'));
        assertEquals("00110", english.encodeChar('n'));
        assertEquals("0111", english.encodeChar('o'));
        assertEquals("00111", english.encodeChar('p'));
        assertEquals("00001001", english.encodeChar('q'));
        assertEquals("110000", english.encodeChar('r'));
        assertEquals("1101", english.encodeChar('s'));
        assertEquals("111", english.encodeChar('t'));
        assertEquals("100100", english.encodeChar('u'));
        assertEquals("0000111", english.encodeChar('v'));
        assertEquals("1010", english.encodeChar('w'));
        assertEquals("000010000", english.encodeChar('x'));
        assertEquals("100101", english.encodeChar('y'));
        assertEquals("000010001", english.encodeChar('z'));
    }

    @Test
    public void smallZips() {
        String text =
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + "bbbbbbbbbbbbb" + "ccccccccccc" + "dddddddddddddddd" +
                        "eeeeeeeee" + "fffff";
        Zipper zipper = new Zipper(new CodeBook(text));
        assertEquals("0", zipper.encode("a"));
        assertEquals("101", zipper.encode("b"));
        assertEquals("100", zipper.encode("c"));
        assertEquals("111", zipper.encode("d"));
        assertEquals("1101", zipper.encode("e"));
        assertEquals("1100", zipper.encode("f"));
        assertEquals("00", zipper.encode("aa"));
        assertEquals("101101", zipper.encode("bb"));
        assertEquals("100100", zipper.encode("cc"));
        assertEquals("111111", zipper.encode("dd"));
        assertEquals("11011101", zipper.encode("ee"));
        assertEquals("11001100", zipper.encode("ff"));
        assertEquals("0101", zipper.encode("ab"));
        assertEquals("101100", zipper.encode("bc"));
        assertEquals("100111", zipper.encode("cd"));
        assertEquals("1111101", zipper.encode("de"));
        assertEquals("11011100", zipper.encode("ef"));
        assertEquals("0101100", zipper.encode("abc"));
        assertEquals("101100111", zipper.encode("bcd"));
        assertEquals("1001111101", zipper.encode("cde"));
        assertEquals("11111011100", zipper.encode("def"));

        assertEquals("a", zipper.decode("0"));
        assertEquals("b", zipper.decode("101"));
        assertEquals("c", zipper.decode("100"));
        assertEquals("d", zipper.decode("111"));
        assertEquals("e", zipper.decode("1101"));
        assertEquals("f", zipper.decode("1100"));
        assertEquals("aa", zipper.decode("00"));
        assertEquals("bb", zipper.decode("101101"));
        assertEquals("cc", zipper.decode("100100"));
        assertEquals("dd", zipper.decode("111111"));
        assertEquals("ee", zipper.decode("11011101"));
        assertEquals("ff", zipper.decode("11001100"));
        assertEquals("ab", zipper.decode("0101"));
        assertEquals("bc", zipper.decode("101100"));
        assertEquals("cd", zipper.decode("100111"));
        assertEquals("de", zipper.decode("1111101"));
        assertEquals("ef", zipper.decode("11011100"));
        assertEquals("abc", zipper.decode("0101100"));
        assertEquals("bcd", zipper.decode("101100111"));
        assertEquals("cde", zipper.decode("1001111101"));
        assertEquals("def", zipper.decode("11111011100"));

        assertEquals(text, zipper.decode(zipper.encode(text)));
    }

    @Test
    public void bigRandomZips() {
        String text = makeUniformRandomText(10000);
        CodeBook book = new CodeBook(text);
        Zipper zipper = new Zipper(book);
        assertEquals(text, zipper.decode(zipper.encode(text)));
        text = makeBiasedRandomText(10000);
        book = new CodeBook(text);
        zipper = new Zipper(book);
        assertEquals(text, zipper.decode(zipper.encode(text)));
    }

}
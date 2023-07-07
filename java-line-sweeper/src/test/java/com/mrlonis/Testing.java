package com.mrlonis;

/*
 * To test with JUnit, add JUnit to your project. To do this, go to Project->Properties. Select "Java Build Path".
 * Select the "Libraries" tab and "Add Library". Select JUnit, then JUnit 4.
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Testing {

    /**
     * Start with this test. Make appropriate changes to BinarySearchTree.Node.
     */
    @Test
    public void nodeProperties() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        BinarySearchTree<Integer>.Node p;
        p = bst.new Node(5);
        assertNull(p.left);
        assertNull(p.right);
        assertNull(p.parent);
        assertNotNull(p.get());
        assertEquals(5, (int) p.data);
        assertNotNull(p.get());
        assertEquals(5, (int) p.get());
        assertEquals(1, p.height);
        assertFalse(p.dirty);
        assertTrue(p.isLeaf());
        p = bst.new Node(6, p, null);
        assertEquals(6, (int) p.data);
        assertEquals(6, (int) p.get());
        p.fixHeight();
        assertEquals(2, p.height);
        p = bst.new Node(7, null, p);
        assertEquals(7, (int) p.data);
        assertEquals(7, (int) p.get());
        p.fixHeight();
        assertEquals(3, p.height);
        p.left = bst.new Node(8);
        assertEquals(8, (int) p.left.data);
        assertEquals(8, (int) p.left.get());
        p.fixHeight();
        assertEquals(3, p.height);
        p.left.left = p.right;
        p.left.fixHeight();
        assertEquals(3, p.height);
        p.fixHeight();
        assertEquals(4, p.height);
    }

    /**
     * When you're ready to run this test, remove the // from the above line.
     */
    @Test
    public void insertSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        int n = 0;
        for (Integer key : a) {
            bst.insert(key);
            n++;
            assertEquals(n, bst.size());
        }
        /* 4 / \ 0 8 \ / \ 2 6 10 */
        assertEquals(4, (int) bst.root.data);
        assertEquals(0, (int) bst.root.left.data);
        assertEquals(2, (int) bst.root.left.right.data);
        assertEquals(8, (int) bst.root.right.data);
        assertEquals(6, (int) bst.root.right.left.data);
        assertEquals(10, (int) bst.root.right.right.data);

        assertNull(bst.root.parent);
        assertEquals(bst.root, bst.root.left.parent);
        assertEquals(bst.root, bst.root.right.parent);
        assertEquals(bst.root.left, bst.root.left.right.parent);
        assertEquals(bst.root.right, bst.root.right.left.parent);
        assertEquals(bst.root.right, bst.root.right.right.parent);
    }

    @Test
    public void clearSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        bst.clear();
        assertEquals(0, bst.size());
        assertNull(bst.root);
        for (Integer key : a) {
            bst.insert(key);
        }
        assertEquals(a.length, bst.size());
        assertNotNull(bst.root);
    }

    @Test
    public void insertReturnBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            assertEquals(key, bst.insert(key).get());
        }
    }

    @Test
    public void skinnyBST() {
        BinarySearchTree<String> bst = new BinarySearchTree<>((String x, String y) -> x.compareTo(y) < 0);
        String[] a = new String[]{"ape", "boa", "cat", "dog", "emu", "fox", "gnu", "hog"};
        for (String key : a) {
            bst.insert(key);
        }
        assertEquals("ape", bst.root.data);
        assertEquals("boa", bst.root.right.data);
        assertEquals("cat", bst.root.right.right.data);
        assertEquals("dog", bst.root.right.right.right.data);
        assertEquals("emu", bst.root.right.right.right.right.data);
        assertEquals("fox", bst.root.right.right.right.right.right.data);
        assertEquals("gnu", bst.root.right.right.right.right.right.right.data);
        assertEquals("hog", bst.root.right.right.right.right.right.right.right.data);
    }

    @Test
    public void parentsSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        assertNull(bst.root.parent);
        assertSame(bst.root, bst.root.left.parent);
        assertSame(bst.root.left, bst.root.left.right.parent);
        assertSame(bst.root, bst.root.right.parent);
        assertSame(bst.root.right, bst.root.right.left.parent);
        assertSame(bst.root.right, bst.root.right.right.parent);
    }

    @Test
    public void beforeAndAfterSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        /*
         * Before and After on left side of tree. (before on root)
         */
        assertSame(bst.root.getBefore(), bst.root.left.right);
        assertNull(bst.root.left.getBefore());
        assertSame(bst.root.left.getAfter(), bst.root.left.right);
        assertSame(bst.root.left.right.getBefore(), bst.root.left);
        assertSame(bst.root.left.right.getAfter(), bst.root);
        /*
         * Before and After on right side of tree. (after on root)
         */
        assertSame(bst.root.getAfter(), bst.root.right.left);
        assertSame(bst.root.right.getBefore(), bst.root.right.left);
        assertSame(bst.root.right.left.getBefore(), bst.root);
        assertSame(bst.root.right.left.getAfter(), bst.root.right);
        assertSame(bst.root.right.getAfter(), bst.root.right.right);
        assertSame(bst.root.right.right.getBefore(), bst.root.right);
        assertNull(bst.root.right.right.getAfter());
    }

    @Test
    public void searchSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        Location<Integer> loc;
        for (Integer key : a) {
            assertTrue(bst.contains(key));
            assertFalse(bst.contains(key + 1));
            loc = bst.search(key);
            assertNotNull(loc);
            assertEquals(key, loc.get());
            loc = bst.search(key + 1);
            assertNull(loc);
        }
    }

    @Test
    public void heightSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        assertEquals(3, bst.height());
        assertEquals(3, bst.root.height);
        assertEquals(2, bst.root.left.height);
        assertEquals(2, bst.root.right.height);
        assertEquals(1, bst.root.left.right.height);
        assertEquals(1, bst.root.right.left.height);
        assertEquals(1, bst.root.right.right.height);
        bst.insert(7);
        assertEquals(4, bst.height());
        assertEquals(4, bst.root.height);
        assertEquals(2, bst.root.left.height);
        assertEquals(3, bst.root.right.height);
        assertEquals(1, bst.root.left.right.height);
        assertEquals(2, bst.root.right.left.height);
        assertEquals(1, bst.root.right.right.height);
        assertEquals(1, bst.root.right.left.right.height);
    }

    @Test
    public void removeSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        int size = a.length, dirtyCount = 0;
        for (Integer key : a) {
            bst.remove(key);
            size--;
            dirtyCount++;
            assertEquals(size, bst.size());
            assertTrue(bst.search(key).dirty);
            assertEquals(dirtyCount, countDirtyNodes(bst.root));
            assertFalse(bst.contains(key));
            // Removing a key not in the tree should do nothing.
            bst.remove(key + 1);
            assertEquals(size, bst.size());
            assertTrue(bst.search(key).dirty);
            assertEquals(dirtyCount, countDirtyNodes(bst.root));
            assertFalse(bst.contains(key));
            // Removing an already removed key should do nothing.
            bst.remove(key);
            assertEquals(size, bst.size());
            assertTrue(bst.search(key).dirty);
            assertEquals(dirtyCount, countDirtyNodes(bst.root));
            assertFalse(bst.contains(key));
        }
        assertTrue(bst.isEmpty());
        assertTrue(verifyParentPointers(bst.root));
    }

    @Test
    public void dupsSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        // Attempts to insert a duplicate key should be ignored.
        for (Integer key : a) {
            bst.insert(key);
            assertEquals(6, bst.size());
            assertEquals(0, countDirtyNodes(bst.root));
        }
        // Attempts to insert a previously removed key should reuse the dirty
        // node.
        int size = a.length, dirtyCount = 0;
        for (Integer key : a) {
            bst.remove(key);
            size--;
            dirtyCount++;
            assertEquals(size, bst.size());
            assertTrue(bst.search(key).dirty);
            assertEquals(dirtyCount, countDirtyNodes(bst.root));
            assertFalse(bst.contains(key));
        }
        assertTrue(bst.isEmpty());
        size = 0;
        for (Integer key : a) {
            bst.insert(key);
            dirtyCount--;
            size++;
            assertEquals(size, bst.size());
            assertFalse(bst.search(key).dirty);
            assertEquals(dirtyCount, countDirtyNodes(bst.root));
        }
    }

    @Test
    public void keysSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        List<Integer> ks = bst.keys();
        assertEquals(a.length, ks.size());
        for (int i = 1; i < ks.size(); i++) {
            assertTrue(ks.get(i - 1) <= ks.get(i));
        }
        bst.clear();
        assertTrue(bst.keys().isEmpty());
    }

    @Test
    public void keysDirtyBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{26, 5, 28, 32, 4, 8, 0, 2, 3, 6, 10, 12, 22, 1, 14, 20, 16, 18, 7, 24, 30};
        for (Integer key : a) {
            bst.insert(key);
        }
        List<Integer> ks = bst.keys();
        for (Integer key : ks) {
            if (key % 2 == 1) {
                bst.remove(key);
            }
        }
        ks = bst.keys();
        assertEquals(a.length - 4, ks.size());
        for (Integer key : ks) {
            assertEquals(0, key % 2);
        }
        for (int i = 1; i < ks.size(); i++) {
            assertTrue(ks.get(i - 1) <= ks.get(i));
        }
        bst.clear();
        assertTrue(bst.keys().isEmpty());
    }

    @Test
    public void rebuildSmallBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            bst.insert(key);
        }
        int n = a.length;
        for (Integer key : a) {
            bst.remove(key);
            n--;
            bst.rebuild();
            assertTrue(verifyParentPointers(bst.root));
            assertEquals(n, bst.size());
            assertNull(bst.search(key));
            assertFalse(bst.contains(key));
        }
        assertNull(bst.root);
        assertEquals(0, bst.height());
    }

    @Test
    public void bigBSTtest() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        Random gen = new Random();
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            a.add(gen.nextInt(i + 1), i);
        }
        for (Integer x : a) {
            bst.insert(x);
            assertTrue(verifyParentPointers(bst.root));
            assertTrue(verifyOrderingProperty(bst.root, bst.lessThan));
            assertTrue(verifyHeights(bst.root));
        }
        //System.out.println(bst.toString());
        while (!a.isEmpty()) {
            int i = gen.nextInt(a.size());
            int x = a.get(i);
            a.remove(i);
            bst.remove(x);
            bst.rebuild();
            assertTrue(verifyParentPointers(bst.root));
            assertTrue(verifyOrderingProperty(bst.root, bst.lessThan));
            assertTrue(verifyHeights(bst.root));
        }
    }

    @Test
    public void beforeBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{12, 4, 18, 5, 11, 8, 15, 9, 17, 20, 3, 13, 19, 2, 14, 7, 6, 10, 1, 16};
        int n = a.length;
        for (Integer key : a) {
            bst.insert(key);
        }
        assertNull(bst.search(1).getBefore());
        for (int i = 2; i <= n; i++) {
            assertEquals(i - 1, (int) bst.search(i).getBefore().get());
        }
    }

    @Test
    public void afterBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        int[] a = new int[]{12, 4, 18, 5, 11, 8, 15, 9, 17, 20, 3, 13, 19, 2, 14, 7, 6, 10, 1, 16};
        int n = a.length;
        for (Integer key : a) {
            bst.insert(key);
        }
        for (int i = 1; i < n; i++) {
            assertEquals(i + 1, (int) bst.search(i).getAfter().get());
        }
        assertNull(bst.search(n).getAfter());
    }

    @Test
    public void beforeWithRemoveBST() {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>((Integer x, Integer y) -> x < y);
        assertTrue(bst.isEmpty());
        for (int key = 1; key <= 100; key++) {
            bst.insert(key);
        }
        for (int key = 2; key <= 100; key += 2) {
            bst.remove(key);
        }
        for (int key = 3; key <= 100; key += 2) {
            assertEquals(key - 2, (int) bst.search(key).getBefore().get());
        }
    }

    /**********************************************************************************
     * When you've reached this point, run the Driver to see the Line Sweep
     * Algorithm in action.
     **********************************************************************************/

    @Test
    public void insertAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 8, 1, 2, 7, 9};
        for (Integer key : a) {
            avl.insert(key);
        }
        assertEquals(3, avl.height());
        for (Integer key : a) {
            assertNotNull(avl.search(key));
            assertEquals(key, avl.search(key).get());
        }
        for (Integer key : a) {
            avl.remove(key);
            assertTrue(avl.search(key).dirty);
            assertFalse(avl.contains(key));
        }
        assertTrue(avl.isEmpty());
        assertEquals(0, avl.size());
        assertEquals(3, avl.height());
        assertEquals(3, avl.root.height);
        avl.rebuild();
        assert verifyParentPointers(avl.root);
        assertEquals(0, avl.height());
    }

    @Test
    public void beforeAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        assertTrue(avl.isEmpty());
        int[] a = new int[]{12, 4, 18, 5, 11, 8, 15, 9, 17, 20, 3, 13, 19, 2, 14, 7, 6, 10, 1, 16};
        int n = a.length;
        for (Integer key : a) {
            avl.insert(key);
        }
        assertNull(avl.search(1).getBefore());
        for (int i = 2; i <= n; i++) {
            assertEquals(i - 1, (int) avl.search(i).getBefore().get());
        }
    }

    @Test
    public void afterAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        assertTrue(avl.isEmpty());
        int[] a = new int[]{12, 4, 18, 5, 11, 8, 15, 9, 17, 20, 3, 13, 19, 2, 14, 7, 6, 10, 1, 16};
        int n = a.length;
        for (Integer key : a) {
            avl.insert(key);
        }
        for (int i = 1; i < n; i++) {
            assertEquals(i + 1, (int) avl.search(i).getAfter().get());
        }
        assertNull(avl.search(n).getAfter());
    }

    @Test
    public void LLtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 2, 1};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertEquals(2, (int) avl.root.data);
        assertEquals(1, (int) avl.root.left.data);
        assertEquals(3, (int) avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(2, avl.height());
        assertEquals(2, avl.root.height);
        assertEquals(1, avl.root.left.height);
        assertEquals(1, avl.root.right.height);
    }

    @Test
    public void RRtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{1, 2, 3};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertEquals(2, (int) avl.root.data);
        assertEquals(1, (int) avl.root.left.data);
        assertEquals(3, (int) avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(2, avl.height());
        assertEquals(2, avl.root.height);
        assertEquals(1, avl.root.left.height);
        assertEquals(1, avl.root.right.height);
    }

    @Test
    public void LRtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{3, 1, 2};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertEquals(2, (int) avl.root.data);
        assertEquals(1, (int) avl.root.left.data);
        assertEquals(3, (int) avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(2, avl.height());
        assertEquals(2, avl.root.height);
        assertEquals(1, avl.root.left.height);
        assertEquals(1, avl.root.right.height);
    }

    @Test
    public void RLtinyAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{1, 3, 2};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(3, avl.size());
        // Check keys
        assertEquals(2, (int) avl.root.data);
        assertEquals(1, (int) avl.root.left.data);
        assertEquals(3, (int) avl.root.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        // Check heights
        assertEquals(2, avl.height());
        assertEquals(2, avl.root.height);
        assertEquals(1, avl.root.left.height);
        assertEquals(1, avl.root.right.height);
    }

    @Test
    public void LLsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{5, 6, 3, 2, 4, 1};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(6, avl.size());
        // Check keys
        assertEquals(3, (int) avl.root.data);
        assertEquals(2, (int) avl.root.left.data);
        assertEquals(5, (int) avl.root.right.data);
        assertEquals(1, (int) avl.root.left.left.data);
        assertEquals(4, (int) avl.root.right.left.data);
        assertEquals(6, (int) avl.root.right.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        assertEquals(avl.root.left, avl.root.left.left.parent);
        assertEquals(avl.root.right, avl.root.right.left.parent);
        assertEquals(avl.root.right, avl.root.right.right.parent);
        // Check heights
        assertEquals(3, avl.height());
        assertEquals(3, avl.root.height);
        assertEquals(2, avl.root.left.height);
        assertEquals(2, avl.root.right.height);
        assertEquals(1, avl.root.left.left.height);
        assertEquals(1, avl.root.right.left.height);
        assertEquals(1, avl.root.right.right.height);
    }

    @Test
    public void RRsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{2, 1, 4, 3, 5, 6};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check size
        assertEquals(6, avl.size());
        // Check keys
        assertEquals(4, (int) avl.root.data);
        assertEquals(2, (int) avl.root.left.data);
        assertEquals(1, (int) avl.root.left.left.data);
        assertEquals(3, (int) avl.root.left.right.data);
        assertEquals(5, (int) avl.root.right.data);
        assertEquals(6, (int) avl.root.right.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        assertEquals(avl.root.left, avl.root.left.left.parent);
        assertEquals(avl.root.left, avl.root.left.right.parent);
        assertEquals(avl.root.right, avl.root.right.right.parent);
        // Check heights
        assertEquals(3, avl.height());
        assertEquals(3, avl.root.height);
        assertEquals(2, avl.root.left.height);
        assertEquals(2, avl.root.right.height);
        assertEquals(1, avl.root.left.left.height);
        assertEquals(1, avl.root.left.right.height);
        assertEquals(1, avl.root.right.right.height);
    }

    @Test
    public void LRsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{5, 2, 6, 1, 3};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check keys before the rotation
        assertEquals(5, (int) avl.root.data);
        assertEquals(2, (int) avl.root.left.data);
        assertEquals(6, (int) avl.root.right.data);
        assertEquals(1, (int) avl.root.left.left.data);
        assertEquals(3, (int) avl.root.left.right.data);
        // Do the rotation
        avl.insert(4);
        // Check keys after the rotation
        assertEquals(6, avl.size());
        assertEquals(3, (int) avl.root.data);
        assertEquals(2, (int) avl.root.left.data);
        assertEquals(5, (int) avl.root.right.data);
        assertEquals(1, (int) avl.root.left.left.data);
        assertEquals(4, (int) avl.root.right.left.data);
        assertEquals(6, (int) avl.root.right.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        assertEquals(avl.root.left, avl.root.left.left.parent);
        assertEquals(avl.root.right, avl.root.right.left.parent);
        assertEquals(avl.root.right, avl.root.right.right.parent);
        // Check heights
        assertEquals(3, avl.height());
        assertEquals(3, avl.root.height);
        assertEquals(2, avl.root.left.height);
        assertEquals(2, avl.root.right.height);
        assertEquals(1, avl.root.left.left.height);
        assertEquals(1, avl.root.right.left.height);
        assertEquals(1, avl.root.right.right.height);
    }

    @Test
    public void RLsmallAVL() {
        BinarySearchTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{2, 1, 5, 3, 6};
        for (Integer key : a) {
            avl.insert(key);
        }
        // Check keys before the rotation
        assertEquals(2, (int) avl.root.data);
        assertEquals(1, (int) avl.root.left.data);
        assertEquals(5, (int) avl.root.right.data);
        assertEquals(3, (int) avl.root.right.left.data);
        assertEquals(6, (int) avl.root.right.right.data);
        // Do the rotation
        avl.insert(4);
        // Check keys after the rotation
        assertEquals(6, avl.size());
        assertEquals(3, (int) avl.root.data);
        assertEquals(2, (int) avl.root.left.data);
        assertEquals(5, (int) avl.root.right.data);
        assertEquals(1, (int) avl.root.left.left.data);
        assertEquals(4, (int) avl.root.right.left.data);
        assertEquals(6, (int) avl.root.right.right.data);
        // Check parents
        assertNull(avl.root.parent);
        assertEquals(avl.root, avl.root.left.parent);
        assertEquals(avl.root, avl.root.right.parent);
        assertEquals(avl.root.left, avl.root.left.left.parent);
        assertEquals(avl.root.right, avl.root.right.left.parent);
        assertEquals(avl.root.right, avl.root.right.right.parent);
        // Check heights
        assertEquals(3, avl.height());
        assertEquals(3, avl.root.height);
        assertEquals(2, avl.root.left.height);
        assertEquals(2, avl.root.right.height);
        assertEquals(1, avl.root.left.left.height);
        assertEquals(1, avl.root.right.left.height);
        assertEquals(1, avl.root.right.right.height);
    }

    @Test
    public void keysSmallAVL() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            avl.insert(key);
        }
        List<Integer> ks = avl.keys();
        assertEquals(a.length, ks.size());
        for (int i = 1; i < ks.size(); i++) {
            assertTrue(ks.get(i - 1) <= ks.get(i));
        }
    }

    @Test
    public void mediumAVLtest() {
        int[] a;
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        a = new int[]{5, 3, 1, 2, 7, 6, 9, 8, -1, -7, -5, -2, -3, 10, 15, 13, 12, 19, 20, 21};
        for (Integer x : a) {
            avl.insert(x);
        }
        System.out.println(avl.keys().toString());
        avl.printAll();
        System.out.println(avl);
        assertEquals(5, avl.height());
        assert verifyParentPointers(avl.root);
        for (int i = a.length - 1; i >= 0; i--) {
            avl.remove(a[i]);
            assert verifyParentPointers(avl.root);
            assert verifyOrderingProperty(avl.root, avl.lessThan);
        }
        for (Integer x : a) {
            avl.insert(x);
        }
        Arrays.sort(a);
        for (int i = 1; i < a.length - 1; i++) {
            assertEquals(Integer.valueOf(a[i - 1]), avl.search(a[i]).getBefore().get());
            assertEquals(Integer.valueOf(a[i + 1]), avl.search(a[i]).getAfter().get());
        }
        for (Integer x : a) {
            avl.remove(x);
            assert verifyParentPointers(avl.root);
        }

        a = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        for (Integer x : a) {
            avl.insert(x);
            assert verifyOrderingProperty(avl.root, avl.lessThan);
            assert verifyBFs(avl.root);
        }
        assertEquals(4, avl.height());
        assert verifyParentPointers(avl.root);
        a = new int[]{5, 3, 1, 2, 7, 6, 9, 8, -1, -7, -5, -2, -3, 10, 15, 13, 12, 19, 20, 21};
        avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        for (Integer x : a) {
            avl.insert(x);
            assert verifyOrderingProperty(avl.root, avl.lessThan);
            assert verifyBFs(avl.root);
        }
        assertEquals(5, avl.height());
        assert verifyParentPointers(avl.root);
        int n = a.length;
        assertEquals(n, avl.size());
        for (int i = a.length - 1; i >= 0; i--) {
            avl.remove(a[i]);
            n--;
            assertEquals(n, avl.size());
            assert verifyOrderingProperty(avl.root, avl.lessThan);
            assert verifyParentPointers(avl.root);
            assert verifyBFs(avl.root);
        }
        assertEquals(5, avl.height());
        for (Integer x : a) {
            avl.insert(x);
            assert verifyOrderingProperty(avl.root, avl.lessThan);
        }
        Arrays.sort(a);
        for (int i = 1; i < a.length - 1; i++) {
            assertEquals(Integer.valueOf(a[i - 1]), avl.search(a[i]).getBefore().get());
            assertEquals(Integer.valueOf(a[i + 1]), avl.search(a[i]).getAfter().get());
        }
        for (Integer x : a) {
            avl.remove(x);
            assert verifyParentPointers(avl.root);
        }
    }

    @Test
    public void bigAVLtest() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        Random gen = new Random();
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            a.add(gen.nextInt(i + 1), i);
        }
        for (Integer x : a) {
            avl.insert(x);
            assertTrue(verifyParentPointers(avl.root));
            assertTrue(verifyOrderingProperty(avl.root, avl.lessThan));
            assertTrue(verifyHeights(avl.root));
            assertTrue(verifyBFs(avl.root));
        }
        while (!a.isEmpty()) {
            int i = gen.nextInt(a.size());
            int x = a.get(i);
            a.remove(i);
            avl.remove(x);
            assertTrue(verifyParentPointers(avl.root));
            assertTrue(verifyOrderingProperty(avl.root, avl.lessThan));
            assertTrue(verifyHeights(avl.root));
            assert verifyBFs(avl.root);
        }
    }

    @Test
    public void keysDirtyAVL() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{26, 5, 28, 32, 4, 8, 0, 2, 3, 6, 10, 12, 22, 1, 14, 20, 16, 18, 7, 24, 30};
        for (Integer key : a) {
            avl.insert(key);
        }
        List<Integer> ks = avl.keys();
        for (Integer key : ks) {
            if (key % 2 == 1) {
                avl.remove(key);
            }
        }
        ks = avl.keys();
        assertEquals(a.length - 4, ks.size());
        for (Integer key : ks) {
            assertEquals(0, key % 2);
        }
        for (int i = 1; i < ks.size(); i++) {
            assertTrue(ks.get(i - 1) <= ks.get(i));
        }
    }

    @Test
    public void rebuildSmallAVL() {
        AVLTree<Integer> avl = new AVLTree<>((Integer x, Integer y) -> x < y);
        int[] a = new int[]{4, 8, 0, 2, 6, 10};
        for (Integer key : a) {
            avl.insert(key);
        }
        int n = a.length;
        for (Integer key : a) {
            avl.remove(key);
            n--;
            avl.rebuild();
            assertEquals(n, avl.size());
            assertNull(avl.search(key));
            assertFalse(avl.contains(key));
            assertTrue(verifyParentPointers(avl.root));
            assertTrue(verifyOrderingProperty(avl.root, avl.lessThan));
            assertTrue(verifyHeights(avl.root));
            assertTrue(verifyBFs(avl.root));
        }
        assertNull(avl.root);
        assertEquals(0, avl.height());
    }

    /**********************************************************************************
     * When you've reached this point, you're ready to replace the BST with AVL
     * in the Driver.
     **********************************************************************************/

    private <K> int countDirtyNodes(BinarySearchTree<K>.Node p) {
        if (p == null) {
            return 0;
        }
        return (p.dirty ? 1 : 0) + countDirtyNodes(p.left) + countDirtyNodes(p.right);
    }

    private <K> boolean verifyHeights(BinarySearchTree<K>.Node p) {
        if (p == null) {
            return true;
        }
        int h1 = p.left == null ? 0 : p.left.height;
        int h2 = p.right == null ? 0 : p.right.height;
        return p.height == 1 + Math.max(h1, h2) && verifyHeights(p.left) && verifyHeights(p.right);
    }

    private <K> boolean verifyBFs(BinarySearchTree<K>.Node p) {
        if (p == null) {
            return true;
        }
        int h1 = p.left == null ? 0 : p.left.height;
        int h2 = p.right == null ? 0 : p.right.height;
        return Math.abs(h1 - h2) <= 1 && verifyBFs(p.left) && verifyBFs(p.right);
    }

    private <K> boolean verifyParentPointers(BinarySearchTree<K>.Node root) {
        if (root == null) {
            return true;
        }
        if (root.parent != null) {
            return false;
        }
        return verifyParentPointersHelper(root, root.left) && verifyParentPointersHelper(root, root.right);
    }

    private <K> boolean verifyParentPointersHelper(BinarySearchTree<K>.Node p, BinarySearchTree<K>.Node q) {
        if (q == null) {
            return true;
        }
        if (q.parent != p) {
            return false;
        }
        return verifyParentPointersHelper(q, q.left) && verifyParentPointersHelper(q, q.right);
    }

    private <K> boolean verifyOrderingProperty(BinarySearchTree<K>.Node p, BiPredicate<K, K> lessThan) {
        if (p == null) {
            return true;
        }
        K key = p.data;
        return allLessThan(p.left, key, lessThan) && allGreaterThan(p.right, key, lessThan) &&
                verifyOrderingProperty(p.left, lessThan) && verifyOrderingProperty(p.right, lessThan);
    }

    private <K> boolean allGreaterThan(BinarySearchTree<K>.Node p, K x, BiPredicate<K, K> lessThan) {
        if (p == null) {
            return true;
        }
        return !lessThan.test(p.data, x) && allGreaterThan(p.left, x, lessThan) && allGreaterThan(p.right, x, lessThan);
    }

    private <K> boolean allLessThan(BinarySearchTree<K>.Node p, K x, BiPredicate<K, K> lessThan) {
        if (p == null) {
            return true;
        }
        return lessThan.test(p.data, x) && allLessThan(p.left, x, lessThan) && allLessThan(p.right, x, lessThan);
    }

}

package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Dimension;
import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * TODO: Write a comprehensive suite of unit tests!!!!
 *
 * <p>We include some very simple tests to get you started.
 */
public class Testing {

    /** Runs the benchmarks on all filenames starting with the give prefix. */
    public static void runBenchmarksFor(String prefix) {
        System.out.printf("Routing chips %s/%s*%s\n%n", Constants.INPUTS_FOLDER, prefix, Constants.EXTENSION);
        File folder = new File(Constants.INPUTS_FOLDER);
        for (File file : folder.listFiles()) {
            if (file.isFile()
                    && file.getName().startsWith(prefix)
                    && file.getName().endsWith(Constants.EXTENSION)) {
                System.out.println("========== " + file.getName() + " ==========");
                Chip chip = new Chip(file);
                System.out.println("before:\n" + chip);
                Map<Integer, Path> layout = PathFinder.connectAllWires(chip);
                chip.markWirePaths(layout);
                if (layout == null || layout.size() != chip.wires.size()) {
                    System.out.println();
                }
                System.out.println("after:\n" + chip);
                chip.layout();
                if (!validateLayout(layout, chip)) {
                    System.out.println(file.getName());
                }
                assertTrue(validateLayout(layout, chip));
                System.out.println("cost: " + PathFinder.totalWireUsage(layout));
            }
        }
        System.out.println("==============================");
        System.out.println("Benchmarks completed...\n");
    }

    /** Returns true iff the given wire layout is legal on the given grid. */
    public static boolean validateLayout(Map<Integer, Path> layout, Chip chip) {
        String msg = "Incorrect %s of path for wire %d, found %s, expected %s.";
        Dimension dim = chip.dim;
        List<Obstacle> obstacles = chip.obstacles;
        List<Wire> wires = chip.wires;
        int numWires = wires.size();
        for (int i = 1; i <= numWires; i++) {
            Path path = layout.get(i);
            if (path != null) {
                Coord start = path.getFirst(), end = path.getLast();
                if (!start.equals(wires.get(i - 1).from)) {
                    System.out.printf((msg) + "%n", "start", i, start, wires.get(i - 1).from);
                    return false;
                }
                if (!end.equals(wires.get(i - 1).to)) {
                    System.out.printf((msg) + "%n", "end", i, start, wires.get(i - 1).to);
                    return false;
                }
                Set<Coord> used = new HashSet<>();
                for (int j = 0; j < path.size(); j++) {
                    Coord cell = path.get(j);
                    // Make sure the cell coordinates are in range for the grid.
                    if (!cell.onBoard(dim)) {
                        return false;
                    }
                    // Make sure none of the wires cross each other.
                    if (used.contains(cell)) {
                        return false;
                    }
                    // Make sure that the path consists only of connected
                    // neighbors.
                    if (j > 0 && !path.get(j - 1).neighbors(dim).contains(cell)) {
                        return false;
                    }
                    // Make sure that the path doesn't pass through an obstacle.
                    for (Obstacle obs : obstacles) {
                        if (obs.contains(cell)) {
                            return false;
                        }
                    }
                    used.add(cell);
                }
            }
        }
        return true;
    }

    // @Test
    void obstacleListOfCoords() {
        Coord upperLeft = new Coord(1, 1);
        Coord lowerRight = new Coord(3, 3);
        Obstacle obs = new Obstacle(upperLeft, lowerRight);
        List<Coord> listOfCoord = obs.listOfCoords();
        assertTrue(listOfCoord.contains(new Coord(1, 1)));
        assertTrue(listOfCoord.contains(new Coord(1, 2)));
        assertTrue(listOfCoord.contains(new Coord(1, 3)));
        assertTrue(listOfCoord.contains(new Coord(2, 1)));
        assertTrue(listOfCoord.contains(new Coord(2, 2)));
        assertTrue(listOfCoord.contains(new Coord(2, 3)));
        assertTrue(listOfCoord.contains(new Coord(3, 1)));
        assertTrue(listOfCoord.contains(new Coord(3, 2)));
        assertTrue(listOfCoord.contains(new Coord(3, 3)));

        assertFalse(listOfCoord.contains(new Coord(0, 0)));
        assertFalse(listOfCoord.contains(new Coord(0, 1)));
        assertFalse(listOfCoord.contains(new Coord(0, 2)));
        assertFalse(listOfCoord.contains(new Coord(0, 3)));
        assertFalse(listOfCoord.contains(new Coord(0, 4)));
        assertFalse(listOfCoord.contains(new Coord(1, 0)));
        assertFalse(listOfCoord.contains(new Coord(1, 4)));
        assertFalse(listOfCoord.contains(new Coord(2, 0)));
        assertFalse(listOfCoord.contains(new Coord(2, 4)));
        assertFalse(listOfCoord.contains(new Coord(3, 0)));
        assertFalse(listOfCoord.contains(new Coord(3, 4)));
        assertFalse(listOfCoord.contains(new Coord(4, 0)));
        assertFalse(listOfCoord.contains(new Coord(4, 1)));
        assertFalse(listOfCoord.contains(new Coord(4, 2)));
        assertFalse(listOfCoord.contains(new Coord(4, 3)));
        assertFalse(listOfCoord.contains(new Coord(4, 4)));
    }

    // @Test
    void chip3Manual() {
        Dimension dim;
        List<Obstacle> obstacles = new LinkedList<>();
        List<Wire> wires = new LinkedList<>();

        // Build the chip described in small_03.in
        dim = new Dimension(7, 6);
        obstacles.add(new Obstacle(1, 1, 1, 4));
        obstacles.add(new Obstacle(1, 4, 3, 4));
        obstacles.add(new Obstacle(3, 2, 3, 4));
        obstacles.add(new Obstacle(3, 2, 5, 2));
        wires.add(new Wire(1, 4, 3, 2, 3));
        Chip chip3 = new Chip(dim, obstacles, wires);

        // Test properties of chip3.
        assertEquals(7, chip3.dim.width);
        assertEquals(6, chip3.dim.height);
        assertEquals(4, chip3.obstacles.size());
        assertTrue(chip3.obstacles.getFirst().contains(new Coord(1, 1)));
        assertTrue(chip3.obstacles.getFirst().contains(new Coord(1, 2)));
        assertTrue(chip3.obstacles.getFirst().contains(new Coord(1, 3)));
        assertTrue(chip3.obstacles.getFirst().contains(new Coord(1, 4)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(0, 0)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(2, 1)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(2, 2)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(2, 3)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(2, 4)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(0, 1)));
        assertFalse(chip3.obstacles.getFirst().contains(new Coord(0, 2)));
        assertFalse(chip3.obstacles.get(0).contains(new Coord(0, 3)));
        assertFalse(chip3.obstacles.get(0).contains(new Coord(0, 4)));
        assertTrue(chip3.obstacles.get(1).contains(new Coord(1, 4)));
        assertTrue(chip3.obstacles.get(1).contains(new Coord(2, 4)));
        assertTrue(chip3.obstacles.get(1).contains(new Coord(3, 4)));
        assertTrue(chip3.obstacles.get(2).contains(new Coord(3, 2)));
        assertTrue(chip3.obstacles.get(2).contains(new Coord(3, 3)));
        assertTrue(chip3.obstacles.get(2).contains(new Coord(3, 4)));
        assertTrue(chip3.obstacles.get(3).contains(new Coord(3, 2)));
        assertTrue(chip3.obstacles.get(3).contains(new Coord(4, 2)));
        assertTrue(chip3.obstacles.get(3).contains(new Coord(5, 2)));
        assertEquals(1, chip3.wires.size());
        assertEquals(1, chip3.wires.getFirst().wireId);
        assertEquals(4, chip3.wires.getFirst().from.x);
        assertEquals(3, chip3.wires.getFirst().from.y);
        assertEquals(2, chip3.wires.getFirst().to.x);
        assertEquals(3, chip3.wires.getFirst().to.y);
        assertEquals(2, chip3.wires.getFirst().separation());
    }

    // @Test
    void chip9Manual() {
        Dimension dim;
        List<Obstacle> obstacles = new LinkedList<>();
        List<Wire> wires = new LinkedList<>();

        // Build the chip described in small_09.in
        dim = new Dimension(4, 5);
        wires.add(new Wire(1, 1, 0, 1, 4));
        wires.add(new Wire(2, 0, 1, 2, 1));
        wires.add(new Wire(3, 0, 2, 2, 2));
        wires.add(new Wire(4, 0, 3, 2, 3));
        Chip chip9 = new Chip(dim, obstacles, wires);

        // TODO: Test properties of chip9.
        assertEquals(4, chip9.dim.width);
        assertEquals(5, chip9.dim.height);
        assertEquals(0, chip9.obstacles.size());
        assertEquals(4, chip9.wires.size());
        assertEquals(1, chip9.wires.getFirst().wireId);
        assertEquals(1, chip9.wires.getFirst().from.x);
        assertEquals(0, chip9.wires.getFirst().from.y);
        assertEquals(1, chip9.wires.get(0).to.x);
        assertEquals(4, chip9.wires.get(0).to.y);
        assertEquals(2, chip9.wires.get(1).wireId);
        assertEquals(0, chip9.wires.get(1).from.x);
        assertEquals(1, chip9.wires.get(1).from.y);
        assertEquals(2, chip9.wires.get(1).to.x);
        assertEquals(1, chip9.wires.get(1).to.y);
        assertEquals(3, chip9.wires.get(2).wireId);
        assertEquals(0, chip9.wires.get(2).from.x);
        assertEquals(2, chip9.wires.get(2).from.y);
        assertEquals(2, chip9.wires.get(2).to.x);
        assertEquals(2, chip9.wires.get(2).to.y);
        assertEquals(4, chip9.wires.get(3).wireId);
        assertEquals(0, chip9.wires.get(3).from.x);
        assertEquals(3, chip9.wires.get(3).from.y);
        assertEquals(2, chip9.wires.get(3).to.x);
        assertEquals(3, chip9.wires.get(3).to.y);
    }

    // @Test
    void tinyWire() {
        Wire w1 = new Wire(1, 1, 2, 3, 4);
        assertEquals(1, w1.wireId);
        assertEquals(new Coord(1, 2), w1.from);
        assertEquals(new Coord(3, 4), w1.to);
        assertEquals(4, w1.separation());

        Wire w2 = new Wire(2, 3, 4, 1, 2);
        assertEquals(2, w2.wireId);
        assertEquals(new Coord(3, 4), w2.from);
        assertEquals(new Coord(1, 2), w2.to);
        assertEquals(4, w2.separation());
    }

    // @Test
    void tinyObstacle() {
        Obstacle obs = new Obstacle(5, 5, 5, 5);
        assertTrue(obs.contains(new Coord(5, 5)));
    }

    // @Test
    void chip3File() {
        Chip chip3 = new Chip(new File("inputs/small_03.in"));
        assertEquals(7, chip3.dim.width);
        assertEquals(6, chip3.dim.height);
        assertEquals(4, chip3.obstacles.size());
        assertEquals(1, chip3.wires.size());
        assertEquals(1, chip3.wires.getFirst().wireId);
        assertEquals(4, chip3.wires.getFirst().from.x);
        assertEquals(3, chip3.wires.getFirst().from.y);
        assertEquals(2, chip3.wires.getFirst().to.x);
        assertEquals(3, chip3.wires.getFirst().to.y);
    }

    // @Test
    void smallChip0Layout() {
        Chip chip0 = new Chip(new File("inputs/small_00.in"));
        System.out.println("before:\n" + chip0);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip0);
        chip0.markWirePaths(layout);
        System.out.println("after:\n" + chip0);
        assertNotNull(layout);
        assertEquals(1, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip1Layout() {
        Chip chip1 = new Chip(new File("inputs/small_01.in"));
        System.out.println("before:\n" + chip1);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip1);
        chip1.markWirePaths(layout);
        System.out.println("after:\n" + chip1);
        assertNotNull(layout);
        assertEquals(1, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip2Layout() {
        Chip chip2 = new Chip(new File("inputs/small_02.in"));
        System.out.println("before:\n" + chip2);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip2);
        chip2.markWirePaths(layout);
        System.out.println("after:\n" + chip2);
        assertNotNull(layout);
        assertEquals(1, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip3Layout() {
        Chip chip3 = new Chip(new File("inputs/small_03.in"));
        System.out.println("before:\n" + chip3);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip3);
        chip3.markWirePaths(layout);
        System.out.println("after:\n" + chip3);
        assertNotNull(layout);
        assertEquals(1, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip4Layout() {
        Chip chip4 = new Chip(new File("inputs/small_04.in"));
        System.out.println("before:\n" + chip4);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip4);
        chip4.markWirePaths(layout);
        System.out.println("after:\n" + chip4);
        assertNotNull(layout);
        assertEquals(2, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip5Layout() {
        Chip chip5 = new Chip(new File("inputs/small_05.in"));
        System.out.println("before:\n" + chip5);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip5);
        chip5.markWirePaths(layout);
        System.out.println("after:\n" + chip5);
        assertNotNull(layout);
        assertEquals(2, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip6Layout() {
        Chip chip6 = new Chip(new File("inputs/small_06.in"));
        System.out.println("before:\n" + chip6);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip6);
        chip6.markWirePaths(layout);
        System.out.println("after:\n" + chip6);
        assertNotNull(layout);
        assertEquals(3, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip7Layout() {
        Chip chip7 = new Chip(new File("inputs/small_07.in"));
        System.out.println("before:\n" + chip7);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip7);
        chip7.markWirePaths(layout);
        System.out.println("after:\n" + chip7);
        assertNotNull(layout);
        assertEquals(2, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChi80Layout() {
        Chip chip8 = new Chip(new File("inputs/small_08.in"));
        System.out.println("before:\n" + chip8);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip8);
        chip8.markWirePaths(layout);
        System.out.println("after:\n" + chip8);
        assertNotNull(layout);
        assertEquals(1, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip9Layout() {
        Chip chip9 = new Chip(new File("inputs/small_09.in"));
        System.out.println("before:\n" + chip9);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip9);
        chip9.markWirePaths(layout);
        System.out.println("after:\n" + chip9);
        assertNotNull(layout);
        assertEquals(4, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip10Layout() {
        Chip chip10 = new Chip(new File("inputs/small_10.in"));
        System.out.println("before:\n" + chip10);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip10);
        chip10.markWirePaths(layout);
        System.out.println("after:\n" + chip10);
        assertNotNull(layout);
        assertEquals(4, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    // @Test
    void smallChip11Layout() {
        Chip chip11 = new Chip(new File("inputs/small_11.in"));
        System.out.println("before:\n" + chip11);
        Map<Integer, Path> layout = PathFinder.connectAllWires(chip11);
        chip11.markWirePaths(layout);
        System.out.println("after:\n" + chip11);
        assertNotNull(layout);
        assertEquals(2, layout.size());

        // TODO: Test properties of layout.get(0).

    }

    /**
     * ******************************************************************* Benchmarks: Computes layouts for chips
     * described in input/wire*.in *******************************************************************
     */
    @Test
    void runBenchmarks() {
        runBenchmarksFor("small");
        runBenchmarksFor("medium");
        runBenchmarksFor("big");
        runBenchmarksFor("huge");
    }
}

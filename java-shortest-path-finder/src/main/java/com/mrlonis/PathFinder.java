package com.mrlonis;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

@SuppressWarnings("unused")

/**
 * Most of the work for this project involves implementing the connectAllWires()
 * method in this class. Feel free to create any helper methods that you deem
 * necessary.
 *
 * Your goal is to come up with an efficient algorithm that will find a layout
 * that connects all the wires (if one exists) while attempting to minimize the
 * overall wire length.
 *
 * @author Matthew Lonis (mrlonis)
 */ public class PathFinder {

    /**
     * TODO
     * <p>
     * Returns the sum of the lengths of all non-null paths in the given layout.
     */
    public static int totalWireUsage;

    /**
     * TODO
     * <p>
     * Lays out a path connecting each wire on the chip, and then returns a map that associates a wire id numbers to the paths corresponding to the connected wires on the grid. If
     * it is not possible to connect the end points of a wire, then there should be no association for the wire id# in the result.
     */
    public static Map<Integer, Path> connectAllWires(Chip chip) {
        Map<Integer, Path> ans = new HashMap<>();

        /*
         * Construct visited array.
         */
        boolean[][] visited = visitedConstruction(chip);

        /*
         * Sort Wires based on shortest Manhattan distance for processing.
         */
        PriorityQueue<Wire> wires = wiresSort(chip.wires);

        Map<Integer, Pair> pairs = new HashMap<>();

        /*
         * Process each wire.
         */
        while (!wires.isEmpty()) {
            Wire x = wires.poll();

            Pair pair = wireProcessing(x, visited, chip, "");

            if (pair != null) {
                pairs.put(x.wireId, pair);
            }

            if (pair != null && pair.path == null) {
                int wireIDToFix = Integer.parseInt(pair.wildCard);
                Wire wireToFix = chip.wires.get(wireIDToFix - 1);
                Pair wirePairToFix = pairs.get(wireIDToFix);

                for (Coord cor : wirePairToFix.path) {
                    if (!cor.equals(wireToFix.from) && !cor.equals(wireToFix.to)) {
                        visited[cor.y][cor.x] = false;
                        chip.grid.put(cor, 0);
                    }
                }

                Pair tempPair = wireProcessing(wireToFix, visited, chip, wirePairToFix.wildCard);
                pairs.put(wireIDToFix, tempPair);

                ans.put(wireToFix.wireId, tempPair.path);

                for (Coord cor : tempPair.path) {
                    chip.grid.put(cor, wireToFix.wireId);
                }

                pair = wireProcessing(x, visited, chip, "");

                if (pair != null && pair.path != null) {
                    pairs.put(x.wireId, pair);
                }
            }

            if (pair != null && pair.path != null) {
                ans.put(x.wireId, pair.path);

                for (Coord cor : pair.path) {
                    chip.grid.put(cor, x.wireId);
                }
            } else {
                System.out.println("Unable to connect Wire " + x.wireId);
            }
        }

        return ans;
    }

    /**
     * Constructs the initial visited array placing true into every coordinate that is either inside an obstacle or an
     * end point of a wire.
     *
     * @param chip
     *         The chip this visited array will reference from.
     *
     * @return A boolean matrix of true and false where true is wherever an obstacle is or wherever an end point of a
     * wire is.
     */
    public static boolean[][] visitedConstruction(Chip chip) {
        int height = chip.dim.height;
        int width = chip.dim.width;
        boolean[][] visited = new boolean[height][width];

        for (Obstacle obs : chip.obstacles) {
            for (Coord coord : obs.listOfCoords()) {
                visited[coord.y][coord.x] = true;
            }
        }

        for (Wire wir : chip.wires) {
            visited[wir.from.y][wir.from.x] = true;
            visited[wir.to.y][wir.to.x] = true;
        }

        return visited;
    }

    /**
     * This method sorts the wires into a PriorityQueue based on the separation distance of the wires. The wires with
     * the smallest separation distance will be placed at the top of the queue while the longer separations will be
     * placed at the bottom of the queue.
     *
     * @param wires
     *         The wires to be sorted into a PriorityQueue.
     *
     * @return The PrioiortyQueue of sorted wires.
     */
    public static PriorityQueue<Wire> wiresSort(List<Wire> wires) {
        PriorityQueue<Wire> yo = new PriorityQueue<>((x, y) -> x.separation() - y.separation());

        for (Wire x : wires) {
            yo.add(x);
        }

        return yo;
    }

    /**
     * This method processes a given wire, finding the optimal path to connect the wire. If the path cannot be found, a
     * null value is returned if the path wasn't able to be completed, or a pair is returned that contains a null path
     * but a string containing the wireID of the wire preventing this wire from connecting.
     *
     * @param wire
     *         The wire to connect.
     * @param visited
     *         The visited matrix corresponding to coordinates that can be added to the path without interfering with
     *         any obstacles or other wires.
     * @param chip
     *         The chip this wire exists inside of.
     * @param wildCard
     *         A wild card string. If this is anything other than "Ignore" then the algorithm will use this as a
     *         reference of a direction to avoid using on its first move.
     *
     * @return A pair containing the found path, if there is one, and a String referencing either the first direction
     * the algorithm made or the wireID of the wire preventing this wire from being able to connect. In special
     * circumstances, such as the wire being unable to connect due to obstacles, null is returned.
     */
    public static Pair wireProcessing(Wire wire, boolean[][] visited, Chip chip, String wildCard) {
        /**
         * Start of local variable declarations.
         */
        /*
         * The wire's from coordinate.
         */
        Coord from = wire.from;

        /*
         * The wire's to coordinate.
         */
        Coord to = wire.to;

        /*
         * The string representing the first direction this method chose to
         * travel down during the process of connect the given wire.
         */
        String firstDir = "";

        /*
         * The path representing the coordinates that will connect this wire.
         */
        Path path = new Path(wire);

        /*
         * An integer that represents how many times the while loop has been
         * running.
         */
        int numberOfIterations = 0;

        /*
         * The grid that represents this chip.
         */
        Map<Coord, Integer> grid = chip.grid;

        /*
         * The dimension of the given chip.
         */
        Dimension dim = chip.dim;

        /*
         * An integer representing the amount of times a wire has had to
         * backtrack to find a new path. Prevents infinite loops and is
         * especially useful for small_08.in.
         */
        int backtracks = 0;

        /**
         * Start of actual PathFinding.
         */
        while (!from.equals(to) && backtracks < 4) {
            Map<String, Coord> neigh = neighbors(from, to, visited, dim);
            int i = path.size();

            /*
             * This if-else statement is what allows a path to be able to
             * backtrack and find another path, or terminate out with a null or
             * a pair with a null path but a string referencing the wire
             * preventing the path from being able to start connecting.
             */
            if (neigh.isEmpty() && i > 1) {
                List<Coord> notAcceptables = new LinkedList<>();
                Coord curr = path.get(i - 1);

                /*
                 * While loop backtracking within the path to find a coordinate
                 * that has a neighbor the wire can connect from.
                 */
                while (neighbors(curr, to, visited, dim).isEmpty()) {
                    if (i == 1) {
                        return null;
                    }

                    notAcceptables.add(0, curr);
                    path.remove(i - 1);
                    i = path.size();
                    curr = path.get(i - 1);
                }

                from = curr;
                neigh = neighbors(from, to, visited, dim);

                /*
                 * Changes the visited matrix coordinates to false that are no
                 * longer apart of the path. I do this after finding a
                 * coordinate to travel down because otherwise the path can
                 * mistake a newly falsified visited coordinate as a coordinate
                 * with neighbors it can travel down and travel right back into
                 * a dead end.
                 */
                for (Coord coord : notAcceptables) {
                    visited[coord.y][coord.x] = false;
                }

                backtracks++;
            } else if (neigh.isEmpty() && i == 1) {
                /*
                 * This only runs if the path is of size 1, meaning only the
                 * wire's from coordinate is within the path. This means the
                 * path is unable to even start and therefore the for loop
                 * checks the surrounding neighbors on the chip's grid to find a
                 * wire ID that is blocking the wire from being able to connect
                 * with anything.
                 *
                 * If an ID is found, a Pair with a null path is returned but
                 * contains a string of the wire ID blocking it in.
                 */
                for (Coord cor : from.neighbors(dim)) {
                    if (grid.get(cor) != -1 && grid.get(cor) != 0 && grid.get(cor) != wire.wireId) {
                        firstDir = grid.get(cor).toString();

                        for (Coord coord : path) {
                            if (!cor.equals(to) && !cor.equals(from)) {
                                visited[cor.y][cor.x] = false;
                                break;
                            }
                        }

                        return new Pair(null, firstDir);
                    }
                }

                /*
                 * If no wire ID can be found, null is returned and visited
                 * resets every coordinate in the path that isn't the wire's to
                 * or from back to false.
                 */
                for (Coord cor : path) {
                    if (!cor.equals(to) && !cor.equals(from)) {
                        visited[cor.y][cor.x] = false;
                    }
                }

                return null;
            }

            /*
             * from starts out as advanced, that away later on down the road we
             * can check if advanced hasn't changed.
             */
            Coord advance = from;

            /*
             * A Map of potential advances to take under consideration. The Map
             * contains the strings of the direction the coordinate is with
             * reference to from in order to be able to prevent certain
             * directions from being considered based on the wild card.
             */
            Map<String, Coord> potentialAdvances = new HashMap<>();

            /*
             * Process neighbors for advancement. 1st Heuristic
             */
            for (Entry<String, Coord> e : neigh.entrySet()) {
                Coord cor = e.getValue();
                String dir = e.getKey();

                if ((cor.separation(to) <= from.separation(to) && !visited[cor.y][cor.x]) || cor.equals(to)) {
                    potentialAdvances.put(dir, cor);

                    if (cor.equals(to)) {
                        advance = cor;
                        break;
                    }
                }
            }

            if (!potentialAdvances.isEmpty() && advance != to) {
                if (numberOfIterations == 0) {
                    if (potentialAdvances.containsKey("Right") && !wildCard.equals("Right")) {
                        advance = potentialAdvances.get("Right");
                        firstDir = "Right";
                    } else if (potentialAdvances.containsKey("Up") && !wildCard.equals("Up")) {
                        advance = potentialAdvances.get("Up");
                        firstDir = "Up";
                    } else if (potentialAdvances.containsKey("Left") && !wildCard.equals("Left")) {
                        advance = potentialAdvances.get("Left");
                        firstDir = "Left";
                    } else if (potentialAdvances.containsKey("Down") && !wildCard.equals("Down")) {
                        advance = potentialAdvances.get("Down");
                        firstDir = "Down";
                    } else {
                        if (neigh.containsKey("Right")) {
                            advance = neigh.get("Right");
                            firstDir = "Right";
                        } else if (neigh.containsKey("Up")) {
                            advance = neigh.get("Up");
                            firstDir = "Up";
                        } else if (neigh.containsKey("Left")) {
                            advance = neigh.get("Left");
                            firstDir = "Left";
                        } else if (neigh.containsKey("Down")) {
                            advance = neigh.get("Down");
                            firstDir = "Down";
                        }
                    }
                } else {
                    if (potentialAdvances.containsKey("Right")) {
                        advance = potentialAdvances.get("Right");
                    } else if (potentialAdvances.containsKey("Up")) {
                        advance = potentialAdvances.get("Up");
                    } else if (potentialAdvances.containsKey("Left")) {
                        advance = potentialAdvances.get("Left");
                    } else if (potentialAdvances.containsKey("Down")) {
                        advance = potentialAdvances.get("Down");
                    } else {
                        if (neigh.containsKey("Right")) {
                            advance = neigh.get("Right");
                        } else if (neigh.containsKey("Up")) {
                            advance = neigh.get("Up");
                        } else if (neigh.containsKey("Left")) {
                            advance = neigh.get("Left");
                        } else if (neigh.containsKey("Down")) {
                            advance = neigh.get("Down");
                        }
                    }
                }
            } else if (potentialAdvances.isEmpty()) {
                /*
                 * Process neighbors for advancement. 2nd Heuristic
                 */
                int sep = Integer.MAX_VALUE;
                for (Entry<String, Coord> e : neigh.entrySet()) {
                    Coord cor = e.getValue();
                    String dir = e.getKey();
                    int corSep = cor.separation(to);

                    if ((corSep <= sep && !visited[cor.y][cor.x]) || cor.equals(to)) {
                        potentialAdvances.put(dir, cor);
                        sep = corSep;

                        if (cor.equals(to)) {
                            advance = cor;
                            break;
                        }
                    }
                }

                if (!potentialAdvances.isEmpty() && advance != to) {
                    if (numberOfIterations == 0 && !wildCard.equals("Ignore")) {
                        if (potentialAdvances.containsKey("Right") && !wildCard.equals("Right")) {
                            advance = potentialAdvances.get("Right");
                            firstDir = "Right";
                        } else if (potentialAdvances.containsKey("Up") && !wildCard.equals("Up")) {
                            advance = potentialAdvances.get("Up");
                            firstDir = "Up";
                        } else if (potentialAdvances.containsKey("Left") && !wildCard.equals("Left")) {
                            advance = potentialAdvances.get("Left");
                            firstDir = "Left";
                        } else if (potentialAdvances.containsKey("Down") && !wildCard.equals("Down")) {
                            advance = potentialAdvances.get("Down");
                            firstDir = "Down";
                        } else {
                            if (neigh.containsKey("Right")) {
                                advance = neigh.get("Right");
                                firstDir = "Right";
                            } else if (neigh.containsKey("Up")) {
                                advance = neigh.get("Up");
                                firstDir = "Up";
                            } else if (neigh.containsKey("Left")) {
                                advance = neigh.get("Left");
                                firstDir = "Left";
                            } else if (neigh.containsKey("Down")) {
                                advance = neigh.get("Down");
                                firstDir = "Down";
                            }
                        }
                    } else {
                        if (potentialAdvances.containsKey("Right")) {
                            advance = potentialAdvances.get("Right");
                        } else if (potentialAdvances.containsKey("Up")) {
                            advance = potentialAdvances.get("Up");
                        } else if (potentialAdvances.containsKey("Left")) {
                            advance = potentialAdvances.get("Left");
                        } else if (potentialAdvances.containsKey("Down")) {
                            advance = potentialAdvances.get("Down");
                        } else {
                            if (neigh.containsKey("Right")) {
                                advance = neigh.get("Right");
                            } else if (neigh.containsKey("Up")) {
                                advance = neigh.get("Up");
                            } else if (neigh.containsKey("Left")) {
                                advance = neigh.get("Left");
                            } else if (neigh.containsKey("Down")) {
                                advance = neigh.get("Down");
                            }
                        }
                    }
                }

            }

            path.add(advance);
            from = advance;
            visited[from.y][from.x] = true;
            numberOfIterations++;
        }

        if (path.get(path.size() - 1).equals(to)) {
            return new Pair(path, firstDir);
        } else {
            for (Coord cor : path) {
                if (!cor.equals(to) && !cor.equals(from)) {
                    visited[cor.y][cor.x] = false;
                }
            }

            return null;
        }
    }

    /**
     * @param from
     * @param to
     * @param visited
     * @param dim
     *
     * @return
     */
    public static Map<String, Coord> neighbors(Coord from, Coord to, boolean[][] visited, Dimension dim) {
        Map<String, Coord> neigh = from.neighborsMap(dim);
        Map<String, Coord> temporaryNeighbors = new HashMap<>();

        /*
         * Figure out what neighbors to not consider.
         */
        for (Entry<String, Coord> fromsNeighbor : neigh.entrySet()) {
            boolean tf = true;
            String dir = fromsNeighbor.getKey();
            Coord cor = fromsNeighbor.getValue();

            /*
             * If any neighbor has not been visited or is the .to coordinate of
             * the wire, the neighbor can be considered.
             */
            for (Coord secondNeighbor : cor.neighbors(dim)) {
                if (!visited[secondNeighbor.y][secondNeighbor.x] || secondNeighbor.equals(to)) {
                    tf = false;
                    break;
                }
            }

            if ((tf || visited[cor.y][cor.x]) && !cor.equals(to)) {
                temporaryNeighbors.put(dir, cor);
            }
        }

        for (Entry<String, Coord> e : temporaryNeighbors.entrySet()) {
            String dir = e.getKey();
            neigh.remove(dir);
        }

        return neigh;
    }

    public static int totalWireUsage(Map<Integer, Path> layout) {
        int ans = 0;

        for (Path p : layout.values()) {
            ans += p.length();
        }

        return ans;
    }
}

package com.mrlonis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Board represents the current state of the game. Boards know their dimension, the collection of tiles that are inside the current flooded region, and those tiles that are on
 * the outside.
 *
 * @author Matthew Lonis
 */
public class Board {

    /**
     * These two HashMaps will hold the Tiles for the Board. The Key for the HashMap are the Coords for a Tile and the Values for the HashMap are the Tiles themselves. The variable
     * called inside will represent the Tiles within the flooded region of the Board that will change whenever
     * <code>flood(WaterColor color)</code> is called. The variable outside
     * represents the Tiles not yet flooded on the Board.
     */
    private final Map<Coord, Tile> inside;
	private final Map<Coord, Tile> outside;
    /**
     * This represents the N x N size of the Board. Can be any Integer that is greater than or equal to 0.
     */
    private final int size;
    /**
     * This variable is used to store the tiles that make up the perimeter of the flooded area.
     */
    private final List<Tile> perimeter;
    /**
     * This is an array of the possible WaterColors to be used for convenience. The order that the colors appear in this array will directly relate to how frequencies of colors
     * will be stores in the variable below this called colorCount.
     */
    private final WaterColor[] colorArray = {WaterColor.BLUE, WaterColor.RED, WaterColor.CYAN, WaterColor.PINK, WaterColor.YELLOW};
    /**
     * This will be used during my <code>floodHelper(WaterColor color)</code> method in order to count the number of Tiles, that hold a specific color, appear next to the perimeter
     * or inside that exist in outside. The color frequencies (integers) will be stored in the same order as colorArray. This will also be used during my suggest() method in order
     * to return the best possible color to do on the next move.
     * <p>
     * By storing the colors in the same order as colorArray, lets say that 1 Blue, 2 Red, 3 Pink, 8 Cyan, and 22 Yellow Tiles exist near the perimeter or inside. Then, colorCount
     * will appear this way: [1, 2, 8, 3, 22]
     */
    private int[] colorCount;

    /**
     * Constructs a square game board of the given size, initializes the list of inside tiles to include just the tile in the upper left corner, and puts all the other tiles in the
     * outside list.
     *
     * @param size The size(N) of the N x N Board being constructed.
     */
    public Board(int size) {
        // A tile is either inside or outside the current flooded region.
        this.inside = new HashMap<>();
        this.outside = new HashMap<>();
        this.size = size;

        /*
         * This initializes my custom variable perimeter.
         */
        this.perimeter = new ArrayList<Tile>();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Coord coord = new Coord(x, y);
                outside.put(coord, new Tile(coord));
            }
        }

        /*
         * Move the corner tile into the flooded region and run flood on its
         * color.
         */
        Tile corner = outside.remove(Coord.ORIGIN);
        inside.put(Coord.ORIGIN, corner);
        perimeter.add(inside.get(Coord.ORIGIN)); // Add the origin Coord to
        // perimeter from inside.
        flood(corner.getColor());
    }

    /**
     * Constructs a square game board based of the size of waterColorArray. Makes the N x N Board contain Tiles that correspond to the N x N WaterColor matrix.
     *
     * @param waterColorArray The Matrix of WaterColors we want to be filled into the Board.
     */
    public Board(WaterColor[][] waterColorArray) {
        // A tile is either inside or outside the current flooded region.
        this.inside = new HashMap<>();
        this.outside = new HashMap<>();
        this.size = waterColorArray.length;

        /*
         * This initializes my custom variable perimeter.
         */
        this.perimeter = new ArrayList<Tile>();

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Coord coord = new Coord(x, y);
                outside.put(coord, new Tile(coord, waterColorArray[x][y]));
            }
        }

        /*
         * Move the corner tile into the flooded region and run flood on its
         * color.
         */
        Tile corner = outside.remove(Coord.ORIGIN);
        inside.put(Coord.ORIGIN, corner);
        perimeter.add(inside.get(Coord.ORIGIN)); // Adds ORIGIN Coord to
        // perimeter from inside.
        flood(corner.getColor());
    }

    /**
     * Simple testing.
     */
    public static void main(String... args) {
        // Print out boards of size 1, 2, ..., 5
        int n = 5;
        for (int size = 1; size <= n; size++) {
            System.out.println("Printing our board of size " + size + "...");
            Board someBoard = new Board(size);
            System.out.println(someBoard.toString());
            System.out.println("Running flood(BLUE) method");
            someBoard.flood(WaterColor.BLUE);
            System.out.println("Printing out updated Board...");
            System.out.println(someBoard);
            System.out.println("...Main Finished!");
        }
    }

    /**
     * Returns the tile at the specified coordinate.
     *
     * @param coord The Coordinate where the Tile I want to return is at.
     * @return The Tile at the given Coordinate.
     */
    public Tile get(Coord coord) {
		if (outside.containsKey(coord)) {
			return outside.get(coord);
		}
        return inside.get(coord);
    }

    /**
     * Returns true iff all tiles on the board have the same color.
     *
     * @return <code>true</code> if all the Tiles on the Board have the same
     * color, otherwise returns <code>false</code>.
     */
    public boolean fullyFlooded() {
        return outside.isEmpty();
    }

    /**
     * This helper function takes a List<Coord> which will be the neighbors of a specific Tile. If the neighbors exist in outside and have the desired color, they will be added to
     * tileList. Otherwise we will associate them with colorCount in order to count the number of tiles with a specific color exist near the perimeter.
     * <p>
     * This method, as you can see, is recursive in order to make sure if the Coord being added to tileList has neighbors that are of the same color, that they get added to
     * tileList as well.
     *
     * @param neighbors A list of Coord for the neighbors of a Tile.
     * @param color     The desired color for the Tiles that will be added to TileList.
     * @param tileList  A temporary ArrayList of Tiles to store Tiles from outside to be added to inside/perimeter.
     */
    public void floodHelper(List<Coord> neighbors,
                            WaterColor color,
                            List<Tile> tileList) {
        neighbors.forEach((Coord) -> {
            if (outside.containsValue(new Tile(Coord, color)) && !tileList.contains(new Tile(Coord, color))) {
                tileList.add(outside.get(Coord));
                List<Coord> temp = Coord.neighbors(this.getSize());
                floodHelper(temp, color, tileList);
            } else if (outside.containsKey(Coord) && (!tileList.contains(inside.get(Coord)))) {
                WaterColor colorToAdd = outside.get(Coord)
                                               .getColor();

                for (int i = 0; i < 5; i++) {
                    if (colorToAdd == colorArray[i]) {
                        colorCount[i] += 1;
                        break;
                    }
                }
            }
        });
    }

    /**
     * Updates this board by changing the color of the current flood region and extending its reach.
     * <p>
     * This is done by calling floodHelper on each of the Tiles in the perimeter. Once the floodHelper is done, we put all the Tiles in tileList into inside. Then, I remove all the
     * Tiles in tileList from outside. Then, i add all the Tiles in tileList to perimeter. Then, I check every Tile in perimeter to see if all of its possible neighbors exist in
     * inside and if they do, add them to a temporary ArrayList of Tiles called temp. Finally, I remove all the tiles that exist in temp from perimeter in order to have the correct
     * perimeter representation for the next time flood is called.
     *
     * @param color The color to flood the board with.
     */
    public void flood(WaterColor color) {
        List<Tile> tileList = new ArrayList<>();
        colorCount = new int[colorArray.length];

        inside.forEach((Coord, Tile) -> Tile.setColor(color));

        /*
         * This is a failsafe because for some reason when i ran batch tests,
         * flood1 would end up having a null perimeter
         */
        if (perimeter.isEmpty()) {
            inside.forEach((Coord, Tile) -> {
                perimeter.add(Tile);
            });
        }

        perimeter.forEach((Tile) -> {
            floodHelper(Tile.getCoord()
                            .neighbors(getSize()), color, tileList);
        });

        tileList.forEach((e) -> {
            inside.put(e.getCoord(), e);
        });

        tileList.forEach((e) -> {
            outside.remove(e.getCoord(), e);
        });

        perimeter.addAll(tileList);

        List<Tile> temp = new ArrayList<>();
        List<Coord> tempCoord = new ArrayList<>();

        inside.forEach((Coord, Tile) -> {
            tempCoord.add(Coord);
        });

        perimeter.forEach((Tile) -> {
            if (tempCoord.containsAll(Tile.getCoord()
                                          .neighbors(getSize()))) {
                temp.add(Tile);
            }
        });

        perimeter.removeAll(temp);
    }

    /**
     * Updates this board by changing the color of the current flood region and extending its reach.
     * <p>
     * This is done in a similar way as flood but instead of using a perimeter, it iterates over the entire inside region.
     * <p>
     * I thought this method would be faster since it removes the perimeter calculations but it instead was a slower algorithm. This is why I abandoned this algorithm.
     *
     * @param color The color to flood the board with.
     */
    public void flood1(WaterColor color) {
        List<Tile> tileList = new ArrayList<>();
        colorCount = new int[colorArray.length];

        inside.forEach((Coord, Tile) -> Tile.setColor(color));

        inside.forEach((Coord, Tile) -> {
            floodHelper(Coord.neighbors(getSize()), color, tileList);
        });

        tileList.forEach((e) -> {
            inside.put(e.getCoord(), e);
        });

        tileList.forEach((e) -> {
            outside.remove(e.getCoord(), e);
        });
    }

    /**
     * Updates this board by changing the color of the current flood region and extending its reach.
     * <p>
     * This is done in a similar way as flood but instead of using the floodHelper function, it uses a while Loop that only runs if the size of inside increases.
     * <p>
     * I thought this method would be faster since I tried to resemble the cauliflower approach Menzel described in office hours but it instead was a slower algorithm. This is why
     * I abandoned this algorithm. I'm assuming I didn't implement it as optimized as possible.
     *
     * @param color The color to flood the board with.
     */
    public void flood2(WaterColor color) {
        List<Tile> tileList = new ArrayList<>();
        colorCount = new int[colorArray.length];
        int oldSize, newSize;
        oldSize = inside.size();

        inside.forEach((Coord, Tile) -> Tile.setColor(color));

        /*
         * This is a failsafe because for some reason when i ran batch tests,
         * flood1 would end up having a null perimeter
         */

        if (perimeter.isEmpty()) {
            inside.forEach((Coord, Tile) -> {
                perimeter.add(Tile);
            });
        }

        perimeter.forEach((Tile) -> {
            Tile.getCoord()
                .neighbors(getSize())
                .forEach((Coord) -> {
                    if (outside.containsValue(new Tile(Coord, color)) && !tileList.contains(new Tile(Coord, color))) {
                        tileList.add(outside.get(Coord));
                    } else if (outside.containsKey(Coord) && (!tileList.contains(inside.get(Coord)))) {
                        WaterColor colorToAdd = outside.get(Coord)
                                                       .getColor();
                        for (int i = 0; i < 5; i++) {
                            if (colorToAdd == colorArray[i]) {
                                colorCount[i] += 1;
                                break;
                            }
                        }
                    }
                });
        });

        tileList.forEach((e) -> {
            inside.put(e.getCoord(), e);
        });

        tileList.forEach((e) -> {
            outside.remove(e.getCoord(), e);
        });

        perimeter.addAll(tileList);

        List<Tile> temp = new ArrayList<>();
        List<Coord> tempCoord = new ArrayList<>();

        perimeter.forEach((Tile) -> {
            tempCoord.add(Tile.getCoord());
        });

        perimeter.forEach((Tile) -> {
            if (tempCoord.containsAll(Tile.getCoord()
                                          .neighbors(getSize()))) {
                temp.add(Tile);
            }
        });

        perimeter.removeAll(temp);

        newSize = inside.size();

        while (newSize > oldSize) {
            tileList.removeAll(tileList);
            oldSize = inside.size();

            inside.forEach((Coord, Tile) -> Tile.setColor(color));

            /*
             * This is a failsafe because for some reason when i ran batch
             * tests, flood1 would end up having a null perimeter
             */

            if (perimeter.isEmpty()) {
                inside.forEach((Coord, Tile) -> {
                    perimeter.add(Tile);
                });
            }

            perimeter.forEach((Tile) -> {
                Tile.getCoord()
                    .neighbors(getSize())
                    .forEach((Coord) -> {
                        if (outside.containsValue(new Tile(Coord, color)) && !tileList.contains(new Tile(Coord, color))) {
                            tileList.add(outside.get(Coord));
                        } else if (outside.containsKey(Coord) && (!tileList.contains(inside.get(Coord)))) {
                            WaterColor colorToAdd = outside.get(Coord)
                                                           .getColor();
                            for (int i = 0; i < 5; i++) {
                                if (colorToAdd == colorArray[i]) {
                                    colorCount[i] += 1;
                                    break;
                                }
                            }
                        }
                    });
            });

            tileList.forEach((e) -> {
                inside.put(e.getCoord(), e);
            });

            tileList.forEach((e) -> {
                outside.remove(e.getCoord(), e);
            });

            perimeter.addAll(tileList);

            temp.removeAll(temp);
            tempCoord.removeAll(tempCoord);

            perimeter.forEach((Tile) -> {
                tempCoord.add(Tile.getCoord());
            });

            perimeter.forEach((Tile) -> {
                if (tempCoord.containsAll(Tile.getCoord()
                                              .neighbors(getSize()))) {
                    temp.add(Tile);
                }
            });

            perimeter.removeAll(temp);

            newSize = inside.size();
        }
    }

    /**
     * Returns the size of this board.
     *
     * @return The size of the Board. Since the Board is N x N, this method returns N.
     */
    public int getSize() {
        return size;
    }

    /**
     * This method detects the best possible color to return for the next move. This is done by comparing each color in colorCount to the local variable count. If it is bigger than
     * count, i change count to be equal to that color's frequency and also change the local variable suggestColor to that color in colorArray. Once the for loop ends, suggestColor
     * is returned.
     *
     * @return The best color for the next move in the game.
     */
    public WaterColor suggest() {
        WaterColor suggestColor = colorArray[0];
        int count = 0;

        for (int i = 0; i < 5; i++) {
            if (colorCount[i] > count) {
                count = colorCount[i];
                suggestColor = colorArray[i];
            }
        }

        return suggestColor;
    }

    /**
     * Returns a string representation of this board. Tiles are given as their color names, with those inside the flooded region written in uppercase.
     */
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (int y = 0; y < this.getSize(); y++) {
            for (int x = 0; x < this.getSize(); x++) {
                Coord curr = new Coord(x, y);
                WaterColor color = get(curr).getColor();
                ans.append(this.inside.containsKey(curr) ? color.toString()
                                                                .toUpperCase() : color);
                ans.append("\t");
            }
            ans.append("\n");
        }
        return ans.toString();
    }
}

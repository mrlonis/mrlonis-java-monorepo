package com.mrlonis;

import java.util.LinkedList;
import java.util.List;

/**
 * This is a simple structure to encapsulate an obstacle on a chip. An obstacle is a rectangle defined by its upper left corner and its lower right corner.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Obstacle {

    protected Coord upperLeft, lowerRight;

    /**
     * Creates a new obstacle whose upper left corner is (x1, y1) and whose lower right corner is (x2, y2).
     */
    public Obstacle(int x1,
                    int y1,
                    int x2,
                    int y2) {
        this(new Coord(x1, y1), new Coord(x2, y2));
    }

    /**
     * Creates a new obstacle covering the rectangle with the given upper left corner and lower right corner on a grid.
     */
    public Obstacle(Coord upperLeft,
                    Coord lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    /**
     * Returns true iff the given coordinate is contained in the region of cells covered by this obstacle.
     *
     * @param coord The coordinate to check if it exists within the obstacle.
     */
    public boolean contains(Coord coord) {
        return coord.x >= this.upperLeft.x && coord.y >= this.upperLeft.y && coord.x <= this.lowerRight.x && coord.y <= this.lowerRight.y;
    }

    /**
     * Returns a list of all the Coordinates contained inside this obstacle.
     *
     * @return A list of coordinates that exist inside this obstacle.
     */
    public List<Coord> listOfCoords() {
        List<Coord> ans = new LinkedList<>();

        for (int x = this.upperLeft.x; x <= this.lowerRight.x; x++) {
            for (int y = this.upperLeft.y; y <= this.lowerRight.y; y++) {
                ans.add(0, new Coord(x, y));
            }
        }

        return ans;
    }

    /**
     * Returns a textual representation of this obstacle.
     */
    public String toString() {
        return String.format("Obstacle[%s,%s]", upperLeft, lowerRight);
    }
}

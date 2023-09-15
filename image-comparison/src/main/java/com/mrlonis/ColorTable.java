package com.mrlonis;

import java.awt.Color;

/**
 * A ColorTable is a class that represents a dictionary of frequency counts, keyed on Color. It is a simplification of
 * Map<Color, Integer>. The size of the key space can be reduced by limiting each Color to a certain number of bits per
 * channel.
 *
 * @author Matthew Lonis
 */

public class ColorTable {
    /**
     * Counts the number of collisions during an operation.
     */
    private static int numCollisions = 0;
    /**
     * Represents the capacity of the colorTable array in this ColorTable.
     */
    public int capacity;
    /**
     * Represents the number of current Associations in the colorTable array in this ColorTable.
     */
    public int numOfAssoc;
    /**
     * Represents the bits per channel used for the colors in this ColorTable.
     */
    public int bitsPerChannel;
    /**
     * Represents the collision strategy used by this ColorTable.
     */
    public int collisionStrategy;
    /**
     * Represents the rehash threshold of this ColorTable.
     */
    public double rehashThreshold;
    /**
     * The internal array of a ColorTable that will hold the Associations for a color.
     */
    public Association[] colorTable;

    /**
     * Constructs a color table with a starting capacity of initialCapacity. Keys in the color key space are truncated
     * to bitsPerChannel bits. The collision resolution strategy is specified by passing either Constants.LINEAR or
     * Constants.QUADRATIC for the collisionStrategy parameter. The rehashThrehold specifies the maximum tolerable load
     * factor before triggering a rehash.
     *
     * @param initialCapacity
     *         The starting size of the Association[] colorTable.
     * @param bitsPerChannel
     *         The bits per channel that colors are truncated to for a specified ColorTable.
     * @param collisionStrategy
     *         The collision strategy for a specified ColorTable.
     * @param rehashThreshold
     *         The point at which if the load factor exceeds this number, a rehash() is automatically called.
     *
     * @throws RuntimeException
     *         if initialCapacity is not in the range [1..Constants.MAX_CAPACITY]
     * @throws RuntimeException
     *         if bitsPerChannel is not in the range [1..8]
     * @throws RuntimeException
     *         if collisionStrategy is not one of Constants.LINEAR or Constants.QUADRATIC
     * @throws RuntimeException
     *         if rehashThreshold is not in the range (0.0..1.0] for a linear strategy or (0.0..0.5) for a quadratic
     *         strategy
     */
    public ColorTable(int initialCapacity, int bitsPerChannel, int collisionStrategy, double rehashThreshold) {
        if ((initialCapacity > Constants.MAX_CAPACITY) || (initialCapacity < 1)) {
            throw new RuntimeException("initialCapacity error");
        }

        if ((bitsPerChannel > 8) || (bitsPerChannel < 1)) {
            throw new RuntimeException("bitsPerChannel Error");
        }

        if ((collisionStrategy != Constants.LINEAR) && (collisionStrategy != Constants.QUADRATIC)) {
            throw new RuntimeException("collisionStrategy Error");
        }

        if (collisionStrategy == Constants.LINEAR) {
            if (rehashThreshold > 1) {
                throw new RuntimeException(
                        "rehashThreshold Error - Cannot be greater than 1.0 for LINEAR collision strategy");
            } else if (rehashThreshold <= 0) {
                throw new RuntimeException(
                        "rehashThreshold Error - Cannot be less than or equal to zero for LINEAR collision strategy");
            }
        } else {
            if (rehashThreshold >= 0.5) {
                throw new RuntimeException(
                        "rehashThreshold Error - Cannot be greater than or equal to 0.5 for QUADRATIC collision strategy");
            } else if (rehashThreshold <= 0) {
                throw new RuntimeException(
                        "rehashThreshold Error - Cannot be less than or equal to zero for QUADRATIC collision strategy");
            }
        }

        numCollisions = 0;
        this.capacity = initialCapacity;
        this.numOfAssoc = 0;
        this.bitsPerChannel = bitsPerChannel;
        this.collisionStrategy = collisionStrategy;
        this.rehashThreshold = rehashThreshold;
        this.colorTable = new Association[this.capacity];
    }

    /**
     * Returns the number of collisions that occurred during the most recent get, put, or increment operation.
     *
     * @return Number of collisions that occurred during the most recent get, put, or increment operation.
     */
    public static int getNumCollisions() {
        return numCollisions;
    }

    /**
     * Simple testing.
     */
    public static void main(String[] args) {
        /*
         * Testing Association Class
         */
        Association a1 = new Association(1, 2);
        System.out.println("a1 key = " + a1.key + " value = " + a1.value);

        ColorTable table = new ColorTable(3, 6, Constants.QUADRATIC, .49);
        int[] data = new int[]{32960, 4293315, 99011, 296390};
        for (int datum : data) {
            table.increment(new Color(datum));
        }
        System.out.println("capacity: " + table.getCapacity()); // Expected: 7
        System.out.println("size: " + table.getSize());         // Expected: 3

 		/* The following automatically calls table.toString().
       	Notice that we only include non-zero counts in the String representation.

       	Expected: [3:2096,2, 5:67632,1, 6:6257,1]

       	This shows that there are 3 keys in the table. They are at positions 3, 5, and 6.
       	Their color codes are 2096, 67632, and 6257. The code 2096 was incremented twice.
       	You do not have to mimic this format exactly, but strive for something compact
       	and readable.
 		 */
        System.out.println(table);
    }

    /**
     * Returns the number of bits per channel used by the colors in this table.
     *
     * @return The bits per channel for this ColorTable
     */
    public int getBitsPerChannel() {
        return this.bitsPerChannel;
    }

    /**
     * This method finds an index that is either null or contains an Association who's key is equal to hashVal. The
     * method then returns the index that it found.
     * <p>
     * This method behaves differently depending on the ColorTable collision strategy and is thus named
     * collisionStrategySearch.
     *
     * @param hashVal
     *         The Integer representation of a Color based on Util.pack().
     * @param hashLocation
     *         Represents the ideal hashLocation for hashVal based on hashVal % this.capacity.
     *
     * @return The index hashVal is associated with.
     */
    private int collisionStrategySearch(int hashVal, int hashLocation) {
        int i = hashLocation;
        int h = 1;

        if (this.collisionStrategy == Constants.LINEAR) {
            do {
                if ((this.colorTable[i] == null) || (this.colorTable[i].key == hashVal)) {
                    return i;
                }
                i = (i == this.capacity - 1) ? 0 : i + 1; //Linear strategy for incrementing i
                numCollisions++;
            } while (i != hashLocation);
        } else if (this.collisionStrategy == Constants.QUADRATIC) {
            do {
                if ((this.colorTable[i] == null) || (this.colorTable[i].key == hashVal)) {
                    return i;
                }
                i = (hashLocation + (h * h++)) % this.capacity; //Quadratic strategy for incrementing i
                numCollisions++;
            } while (i != hashLocation);
        }
        return 0;
    }

    /**
     * Returns the frequency count associated with color. Colors that are not explicitly represented in the table are
     * assumed to be present with a count of zero. Uses Util.pack() as the hash function.
     *
     * @param color
     *         The Color that the method will return the frequency count of.
     *
     * @return The frequency count of color.
     */
    public long get(Color color) {
        numCollisions = 0;
        int hashVal = Util.pack(color, this.bitsPerChannel);
        int hashLocation = hashVal % this.capacity;

        int n = collisionStrategySearch(hashVal, hashLocation);

        if (this.colorTable[n] == null) {
            return 0;
        } else if (this.colorTable[n].key == hashVal) {
            return this.colorTable[n].value;
        }

        return 0;
    }

    /**
     * Associates the count with the color in this table. Will do nothing if count is less than or equal to zero. Uses
     * Util.pack() as the hash function.
     *
     * @param count
     *         The frequency count that will be associated with color in the ColorTable.
     * @param color
     *         The Color that will be put into the ColorTable.
     */
    public void put(Color color, long count) {
        if (count > 0) {
            numCollisions = 0;
            int hashVal = Util.pack(color, this.bitsPerChannel);
            int hashLocation = hashVal % this.capacity;

            int n = collisionStrategySearch(hashVal, hashLocation);

            if (this.colorTable[n] == null) {
                this.colorTable[n] = new Association(hashVal, count);

                this.numOfAssoc++;

                if (this.getLoadFactor() >= this.rehashThreshold) {
                    this.rehash();
                }
            } else if (this.colorTable[n].key == hashVal) {
                this.colorTable[n].value = count;
            }
        }
    }

    /**
     * Increments the frequency count associated with color. Colors that are not explicitly represented in the table are
     * assumed to be present with a count of zero.
     *
     * @param color
     *         The Color that will be incremented in the ColorTable.
     */
    public void increment(Color color) {
        numCollisions = 0;
        int hashVal = Util.pack(color, this.bitsPerChannel);
        int hashLocation = hashVal % this.capacity;

        int n = collisionStrategySearch(hashVal, hashLocation);

        if (this.colorTable[n] == null) {
            this.colorTable[n] = new Association(hashVal, 1);

            this.numOfAssoc++;

            if (this.getLoadFactor() >= this.rehashThreshold) {
                this.rehash();
            }
        } else if (this.colorTable[n].key == hashVal) {
            this.colorTable[n].value = this.colorTable[n].value + 1;
        }
    }

    /**
     * Returns the load factor for this table. This is done by dividing the number of Associations in the ColorTable by
     * the capacity of the ColorTable.
     *
     * @return The load factor for the ColorTable.
     */
    public double getLoadFactor() {
        return ((double) this.numOfAssoc / (double) this.capacity);
    }

    /**
     * Returns the size of the internal array representing this table. In other words, returns the capacity variable for
     * ColorTable.
     *
     * @return The size of the Association[] colorTable array in this ColorTable
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Returns the number of key/value associations in this ColorTable.
     *
     * @return The numOfAssoc variable for this ColorTable
     */
    public int getSize() {
        return this.numOfAssoc;
    }

    /**
     * Returns true iff this table is empty.
     *
     * @return <code>true</code> if numOfAssoc equals 0 and <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return this.numOfAssoc == 0;
    }

    /**
     * Increases the size of the Association[] colorTable array in this ColorTable to the smallest prime that is greater
     * than double the current size of the Association[] colorTable array that is of the form 4j + 3. Then, this method
     * moves all the key/value associations into the new array.
     *
     * @throws RuntimeException
     *         if the ColorTable is already at maximum capacity.
     */
    private void rehash() {
        if (this.capacity == Constants.MAX_CAPACITY) {
            throw new RuntimeException("rehash Error: Table already at maximum capacity");
        }

        int oldCapacity = this.capacity;
        int newCapacity = this.capacity * 2;

        if (newCapacity < 0) {
            newCapacity = Constants.MAX_CAPACITY;
        } else {
            boolean primeAnd4j3 = false;
            while (!primeAnd4j3) {
                if (Util.isPrime(newCapacity)) {
                    int temp = newCapacity - 3;
                    if ((temp % 4) == 0) {
                        primeAnd4j3 = true;
                    } else {
                        newCapacity++;
                    }
                } else {
                    newCapacity++;
                }
            }
        }

        Association[] temp = new Association[oldCapacity];

        for (int i = 0; i < oldCapacity; i++) {
            if (this.colorTable[i] != null) {
                temp[i] = this.colorTable[i];
            }
        }

        this.colorTable = new Association[newCapacity];
        this.capacity = newCapacity;
        this.numOfAssoc = 0;

        for (int i = 0; i < oldCapacity; i++) {
            if (temp[i] != null) {
                put(Util.unpack(temp[i].key, this.bitsPerChannel), temp[i].value);
            }
        }
    }

    /**
     * Returns an Iterator that marches through each color in the key color space and returns the sequence of frequency
     * counts for each color in the color key space that exists in this ColorTable.
     *
     * @return An Iterator that marches through the color key space
     *
     * @see ColorIterator
     */
    public Iterator iterator() {
        return new ColorIterator(this);
    }

    /**
     * Returns a String representation of this ColorTable.
     *
     * @return A string representation of the Association[] colorTable array in this ColorTable.
     */
    public String toString() {
        StringBuilder str = new StringBuilder("[");

        for (int i = 0; i < this.capacity; i++) {
            if (this.colorTable[i] != null) {
                str.append(i);
                str.append(":");
                str.append(this.colorTable[i].key);
                str.append(",");
                str.append(this.colorTable[i].value);
                str.append(", ");
            }
        }

        int n = str.length();
        str = new StringBuilder(str.substring(0, n - 2));
        str.append("]");

        return str.toString();
    }

    /**
     * Returns the frequency count of a color in the ColorTable at index i in the Association[] colorTable array.
     *
     * @param i
     *         The desired index where the frequency count will be returned.
     *
     * @return The frequency count of the Association at index i in the Association[] colorTable array in this
     * ColorTable.
     */
    public long getCountAt(int i) {
        if (this.colorTable[i] == null) {
            return 0;
        } else {
            return this.colorTable[i].value;
        }
    }
}

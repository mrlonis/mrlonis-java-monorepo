package com.mrlonis;

/**
 * A Pair is an object that contains a Path and a String. The string represents the direction that was first taken in
 * the construction of the associated Path.
 *
 * @author Matthew Lonis (mrlonis)
 */
public class Pair {

    public Path path;
    public String wildCard;

    public Pair(Path path, String wild) {
        this.path = path;
        this.wildCard = wild;
    }

}

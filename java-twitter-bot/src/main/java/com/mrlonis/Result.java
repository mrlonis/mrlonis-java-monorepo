package com.mrlonis;

/**
 * A basic structure to hold the result of a search (i.e., pos) and the number of comparisons performed by the algorithm
 */
public class Result {

    int pos, comps;

    public Result(int pos,
                  int comps) {
        this.pos = pos;
        this.comps = comps;
    }

}

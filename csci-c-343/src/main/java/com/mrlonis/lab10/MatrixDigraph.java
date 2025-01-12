package com.mrlonis.lab10;

import java.util.HashSet;
import java.util.Set;

interface Digraph {
    Iterable<Integer> in(int v);

    Iterable<Integer> out(int v);

    default void addEdge(int u, int v) {
        addEdge(u, v, 1);
    }

    void addEdge(int u, int v, int weight);

    default int weight(int u, int v) {
        throw new UnsupportedOperationException();
    }

    int numVertices();

    int numEdges();
}

/**
 * MatrixDigraph is a class that implements a directed graph using an adjacency matrix.
 *
 * <p>A digraph may have self-loops but no parallel edges. We assume that vertices are labeled by the integers 0, 1,
 * ..., n - 1. The edges are labeled with a positive weight.
 *
 * <p>TODO: Give the big-Oh cost for each of the following operations in terms of n (the number of vertices):
 *
 * <p>in O(n)
 *
 * <p>out O(n)
 *
 * <p>addEdge O(1)
 *
 * <p>weight O(1)
 *
 * <p>numVertices O(1)
 *
 * <p>numEdges O(1)
 *
 * <p>twoHopsAway O(n^2)
 *
 * @author Matthew Lonis (mrlonis)
 * @author Austin Mitts
 * @author Justin Keller
 */
public class MatrixDigraph implements Digraph {
    private int[][] mat; // adjacency matrix
    private int n; // number of vertices
    private int m; // number of edges

    /** Create a directed graph with no edges and n vertices, labeled 0, 1, 2, ..., n - 1. Assume n is non-negative. */
    public MatrixDigraph(int n) {
        assert n >= 0;
        this.n = n;
        mat = new int[n][n];
    }

    /** Returns the weight of the edge from u to v, if it exists. Otherwise, Integer.MAX_VALUE is returned. */
    public int weight(int u, int v) {
        if (u >= 0 && u < n && v >= 0 && v < n) {
            if (mat[u][v] != 0) return mat[u][v];
            return Integer.MAX_VALUE;
        }
        return Integer.MAX_VALUE;
    }

    /** TODO: Returns true iff the edge between u and v exists. */
    public boolean hasEdge(int u, int v) {
        if (u > n || v > n) {
            return false;
        }

        return mat[u][v] != 0;
    }

    /** Returns the number of vertices in this graph. */
    public int numVertices() {
        return n;
    }

    /** Returns the number of edges in this graph. */
    public int numEdges() {
        return m;
    }

    /**
     * TODO: Adds the edge from u to v of the given weight to this graph. If the edge already exists, then its weight is
     * replaced with the new weight.
     */
    public void addEdge(int u, int v, int weight) {
        assert u >= 0 && u < n;
        assert v >= 0 && v < n;
        assert weight > 0;

        if (this.hasEdge(u, v)) {
            mat[u][v] = weight;
            return;
        }

        mat[u][v] = weight;
        m++;
    }

    /** Returns those vertices that are incident to an outgoing edge of v. */
    public Set<Integer> out(int v) {
        Set<Integer> neighbors = new HashSet<Integer>();
        for (int x = 0; x < n; x++) if (hasEdge(v, x)) neighbors.add(x);
        return neighbors;
    }

    /** TODO: Returns those vertices that are incident to an incoming edge of v. */
    public Set<Integer> in(int v) {
        Set<Integer> neighbors = new HashSet<Integer>();
        for (int x = 0; x < n; x++) if (hasEdge(x, v)) neighbors.add(x);
        return neighbors;
    }

    /** TODO: Returns those vertices that are exactly two hops away from v. */
    public Set<Integer> twoHopsAway(int v) {
        Set<Integer> out = this.out(v);
        Set<Integer> secondOut = new HashSet<>();

        out.forEach(x -> {
            this.out(x).forEach(y -> {
                secondOut.add(y);
            });
        });

        return secondOut;
    }

    /** Returns a textual representation of the adjacency matrix. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Adjacency matrix (n = %d, m = %d)", n, m));
        for (int i = 0; i < n; i++) {
            sb.append("\n\t" + i + ": ");
            for (int j = 0; j < n - 1; j++) sb.append(mat[i][j]).append(", ");
            sb.append(mat[i][n - 1]);
        }
        return sb.toString();
    }
}

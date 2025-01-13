package com.mrlonis.hw6;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ListDigraph is a class that implements a directed graph using a Map of adjacency lists.
 *
 * <p>A digraph may have self-loops but no parallel edges. We assume that vertices are labeled by the integers 0, 1,
 * ..., n - 1. The edges are labeled with a positive weight.
 *
 * <p>Give the big-Oh cost for each of the following operations in terms of n (the number of vertices): in O(?) out O(?)
 * addEdge O(?) weight O(?) numVertices O(?) numEdges O(?) twoHopsAway O(?)
 *
 * <p>N = number of vertices
 *
 * <p>in O(?) - O(N^2)
 *
 * <p>out O(?) - O(N^2)
 *
 * <p>addEdge O(?) - O(N)
 *
 * <p>hasEdge O(?) - O(N)
 *
 * <p>weight O(?) - O(N)
 *
 * <p>numVertices O(?) - O(1)
 *
 * <p>numEdges O(?) - O(1)
 *
 * <p>twoHopsAway O(?) - O(N^2)
 *
 * @author Matthew Lonis (mrlonis)
 */
public class ListDigraph implements Digraph {

    /** An Edge encapsulates the incident vertex and a weight. */
    class Edge {
        int to, weight;

        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return String.format("(%d,%d)", to, weight);
        }
    }

    private Map<Integer, List<Edge>> adj;
    private int n; // number of vertices
    private int m; // number of edges

    /** Create a directed graph with no edges and n vertices, labeled 0, 1, 2, ..., n - 1. Assume n is non-negative. */
    public ListDigraph(int n) {
        this.adj = new HashMap<>();
        this.n = 0;
        this.m = 0;

        for (int i = 0; i < n; i++) {
            this.adj.put(i, new LinkedList<>());
            this.n++;
        }
    }

    /** Returns the weight of the edge from u to v, if it exists. Otherwise, Integer.MAX_VALUE is returned. */
    public int weight(int u, int v) {
        if (u >= this.numVertices() || this.adj.get(u).isEmpty()) {
            return Integer.MAX_VALUE;
        }

        for (Edge possibleAns : this.adj.get(u)) {
            if (possibleAns.to == v) {
                return possibleAns.weight;
            }
        }

        return Integer.MAX_VALUE;
    }

    /** Returns true iff the edge between u and v exists. */
    public boolean hasEdge(int u, int v) {
        if (u >= this.numVertices() || this.adj.get(u).isEmpty()) {
            return false;
        }

        for (Edge possibleAns : this.adj.get(u)) {
            if (possibleAns.to == v) {
                return true;
            }
        }

        return false;
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
     * Adds the edge from u to v of the given weight to this graph. If the edge already exists, then its weight is
     * replaced with the new weight.
     */
    public void addEdge(int u, int v, int weight) {
        if (this.hasEdge(u, v)) {
            for (Edge possibleAns : this.adj.get(u)) {
                if (possibleAns.to == v) {
                    possibleAns.weight = weight;
                    return;
                }
            }
        }

        this.adj.get(u).add(new Edge(v, weight));
        this.m++;
    }

    /** Returns those vertices that are incident to an outgoing edge of v. */
    public Set<Integer> out(int v) {
        Set<Integer> neighbors = new HashSet<>();

        for (int i = 0; i < this.numVertices(); i++) {
            if (this.hasEdge(v, i)) {
                neighbors.add(i);
            }
        }

        return neighbors;
    }

    /** Returns those vertices that are incident to an incoming edge of v. */
    public Set<Integer> in(int v) {
        Set<Integer> neighbors = new HashSet<>();

        for (int i = 0; i < this.numVertices(); i++) {
            if (this.hasEdge(i, v)) {
                neighbors.add(i);
            }
        }

        return neighbors;
    }

    /** Returns those vertices that are exactly two hops away from v. */
    public Set<Integer> twoHopsAway(int v) {
        Set<Integer> neighbors = new HashSet<>();
        Set<Integer> out = this.out(v);

        out.forEach(x -> {
            this.out(x).forEach(y -> {
                neighbors.add(y);
            });
        });

        return neighbors;
    }

    /** Returns a textual representation of the adjacency lists. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Adjacency lists (n = %d, m = %d)", n, m));
        for (int i = 0; i < n; i++) {
            sb.append("\n\t" + i + ": ");
            sb.append(adj.get(i));
        }
        return sb.toString();
    }
}

/**
 * Interface for a Digraph
 *
 * @author Matthew Lonis (mrlonis)
 */
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

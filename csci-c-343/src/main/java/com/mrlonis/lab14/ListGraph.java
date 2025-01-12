package com.mrlonis.lab14;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Starter code for lab14.
 *
 * <p>TODO: Complete the following task: Implement the ListGraph.hasCycle() method.
 */
interface Graph {
    Iterable<Integer> adj(int v);

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
 * ListGraph is a class that implements an undirected graph using a Map of adjacency lists.
 *
 * <p>A graph has no self-loops and no parallel edges. We assume that vertices are labeled by the integers 0, 1, ..., n
 * - 1. The edges are labeled with a positive weight.
 */
public class ListGraph implements Graph {

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
    public ListGraph(int n) {
        assert n >= 0;
        this.n = n;
        adj = new HashMap<>();
        for (int v = 0; v < n; v++) {
            adj.put(v, new LinkedList<Edge>());
        }
    }

    /** Returns the weight of the edge from u to v, if it exists. Otherwise, Integer.MAX_VALUE is returned. */
    public int weight(int u, int v) {
        for (Edge e : adj.get(u)) {
            if (e.to == v) {
                return e.weight;
            }
        }
        return Integer.MAX_VALUE;
    }

    /** Returns true iff the edge between u and v exists. */
    public boolean hasEdge(int u, int v) {
        for (Edge e : adj.get(u)) {
            if (e.to == v) {
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
        assert u >= 0 && u < n;
        assert v >= 0 && v < n;
        List<Edge> ls = adj.get(u);
        boolean found = false;
        for (Edge e : ls) {
            if (e.to == v) {
                e.weight = weight;
                found = true;
                break;
            }
        }
        if (!found) {
            ls.add(new Edge(v, weight));
            m++;
        }
        ls = adj.get(v);
        if (found) {
            for (Edge e : ls) {
                if (e.to == v) {
                    e.weight = weight;
                    return;
                }
            }
        }
        ls.add(new Edge(u, weight));
    }

    /** Returns those vertices that are incident to an outgoing edge of v. */
    public Set<Integer> adj(int v) {
        Set<Integer> neighbors = new HashSet<Integer>();
        for (Edge e : adj.get(v)) {
            neighbors.add(e.to);
        }
        return neighbors;
    }

    /** Returns those vertices that are exactly two hops away from v. */
    public Set<Integer> twoHopsAway(int v) {
        Set<Integer> neighbors = new HashSet<>();
        for (int x : adj(v)) {
            for (int y : adj(x)) {
                neighbors.add(y);
            }
        }
        return neighbors;
    }

    /**
     * TODO
     *
     * <p>Returns true if the graph is cyclic and false otherwise.
     */
    public boolean hasCycle() {
        Boolean visited[] = new Boolean[this.numVertices()];
        for (int i = 0; i < this.numVertices(); i++) {
            visited[i] = false;
        }

        for (int u = 0; u < this.numVertices(); u++) {
            if (!visited[u]) {
                if (isCyclicUtil(u, visited, -1)) {
                    return true;
                }
            }
        }
        return false;
    }

    Boolean isCyclicUtil(int v, Boolean visited[], int parent) {
        System.out.println("New Helper");
        visited[v] = true;
        int i;
        Iterator<Edge> it = adj.get(v).iterator();

        while (it.hasNext()) {
            i = it.next().to;
            System.out.println("i = " + i + " || v = " + v + " || parent = " + parent + " || visited = "
                    + Arrays.toString(visited));

            if (!visited[i] && i != parent) {
                System.out.println("!visied[i]");
                if (isCyclicUtil(i, visited, v)) {
                    return true;
                }
            }
        }
        return false;
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

    public static void main(String[] args) {
        ListGraph g0 = new ListGraph(2);
        g0.addEdge(0, 1);
        System.out.println("!g0.hasCycle()");
        assert !g0.hasCycle();
        System.out.println();
        g0.addEdge(1, 0);
        System.out.println("!g0.hasCycle()");
        assert !g0.hasCycle();
        System.out.println();

        /** 0--5 | | | | 6--1 4--7 | | | | 2--3 */
        ListGraph g1 = new ListGraph(8);
        System.out.println("!g1.hasCycle()");
        assert !g1.hasCycle();
        System.out.println();
        g1.addEdge(0, 1);
        g1.addEdge(1, 2);
        g1.addEdge(1, 6);
        g1.addEdge(2, 3);
        g1.addEdge(3, 4);
        g1.addEdge(4, 5);
        g1.addEdge(4, 7);
        System.out.println("!g1.hasCycle()");
        assert !g1.hasCycle();
        System.out.println();
        g1.addEdge(5, 0);
        System.out.println("g1.hasCycle()");
        assert g1.hasCycle();
        System.out.println();

        /**
         * 0 | | 1
         *
         * <p>2 | | 3--4
         */
        ListGraph g2 = new ListGraph(5);
        g2.addEdge(0, 1, 1);
        g2.addEdge(2, 3, 1);
        g2.addEdge(3, 4, 1);
        System.out.println("!g2.hasCycle()");
        assert !g2.hasCycle();
        System.out.println();

        g2.addEdge(2, 4, 1);
        System.out.println("g2.hasCycle()");
        assert g2.hasCycle();
        System.out.println();

        System.out.println("All tests passed!");
    }
}

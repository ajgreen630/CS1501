/*************************************************************************
 *  Compilation:  javac DepthFirstSearch.java
 *  Execution:    java DepthFirstSearch filename s
 *  Dependencies: WeightedGraph.java
 *
 *  Run depth first search on a weighted, undirected graph.
 *  Runs in O(E + V) time.
 *
 *
 *************************************************************************/
import java.util.*;
import java.io.*;
public class DepthFirstSearch {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int count;           // number of vertices connected to s

    /**
    * Constructor for this algorithm.
    *
    * @param G The weighted graph we are interacting with.
    * @param s The starting vertex.
    */
    public DepthFirstSearch(WeightedGraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    /**
    * depth first search from v 
    *
    * @param G The weighted graph we are interacting with.
    * @param v The vertex in the graph we want to search up until.
    */
    private void dfs(WeightedGraph G, int v) {
        marked[v] = true;
        count++;  // CS 1501 Summer 2018
                  // I added this line to Sedgewick's code.  Otherwise
                  // every graph would be considered to be disconnected
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }
    /**
    * is there an s-v path?
    *
    * @param v The vertex in the graph that we want to examine.
    * @return True if the vertex has been visited (or marked), false otherwise.
    */
    public boolean marked(int v) {
        return marked[v];
    }

    /**
    * number of vertices connected to s
    *
    * @return The number of vertices connected to s within count.
    */
    public int count() {
        return count;
    }
}

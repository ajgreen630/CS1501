/*************************************************************************
 *  Compilation:  javac BreadthFirstPaths.java
 *  Execution:    java BreadthFirstPaths G s
 *  Dependencies: WeightedGraph.java Queue.java Stack.java StdIn.java StdOut.java
 *
 *  Run breadth first search on an weighted, undirected graph.
 *  Runs in O(E + V) time.
 *************************************************************************
 */
import java.util.*;
public class BreadthFirstPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;   // marked[v] = is there an s-v path.
    private int[] edgeTo;       // edgeTo[v] = previous edge on shortest s-v path.
    private int[] distTo;       // distTo[v] = number of edges shortest s-v path
    private final int s;        // source vertex

    /**
    * Constructor for a Breadth First Search.
    *
    * @param G The weighted graph this algorithm interacts with.
    * @param s The starting vertex within the weighted graph.
    */
    public BreadthFirstPaths(WeightedGraph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);

        assert check(G);
    }

    /**
    * Performs a breadth first search on a weighted graph to find the shortest
    * path(s) based on number of hops.
    *
    * @param G The weighted graph this algorithm interacts with.
    * @param s The starting vertex within the weighted graph.
    */
    private void bfs(WeightedGraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = INFINITY;
        }
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);   // Add S to the queue.

        while (!q.isEmpty()) {
            int v = q.dequeue();    // Remove the vertex at the front of the queue.
            //
            for (Edge e : G.adj(v)) {   // For each edge e adjacent to vertex v in the graph.
            //
                int w = e.other(v);     // Get the 'other' vertex to v, w.
                if (!marked[w]) {   // If w is unseen.
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;   // Mark w as seen.
                    q.enqueue(w);   // Add w to the queue.
                }
            }
        }
    }
    /**
    * Indicate whether a vertex has been visited within the graph.
    *
    * @param v The vertex in the graph that has or has not been visited.
    * @return True if the vertex has been visited (or "marked"), false otherwise.
    */
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    /**
    * Returns the distance to a vertex in the graph from the starting vertex.
    *
    * @param v The vertex in the graph we want to examine.
    * @return int - The value in the array distTo at v that is the distance
    *         from the starting vertex to the vertex v.
    */
    public int distTo(int v) {
        return distTo[v];
    }

    /**
    * Return shortest path from s to v, or null is there is no such path. 
    *
    * @param v The vertex in the graph we want to path towards.
    * @return Iterable<Integer> - An iterable path from the starting vertex s to vertex v.
    */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) { 
            return null;
        }
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    /**
    * Check for optimality conditions.  This method is asserted in the constructor.
    * 
    * @param G The graph we are interacting with.
    * @return boolean - True if the graph is optimal, false otherwise.
    */
    private boolean check(WeightedGraph G) {

        // Check that the distance of s = 0.
        if (distTo[s] != 0) {
            StdOut.println("distance of source " + s + " to itself = " + distTo[s]);
            return false;
        }

        // Check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s.
        for (int v = 0; v < G.V(); v++) {
            //
            for (Edge e : G.adj(v)) {
            //
                int w = e.other(v);
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("hasPathTo(" + v + ") = " + hasPathTo(v));
                    StdOut.println("hasPathTo(" + w + ") = " + hasPathTo(w));
                    return false;
                }
                if (hasPathTo(v) && (distTo[w] > distTo[v] + 1)) {
                    StdOut.println("edge " + v + "-" + w);
                    StdOut.println("distTo[" + v + "] = " + distTo[v]);
                    StdOut.println("distTo[" + w + "] = " + distTo[w]);
                    return false;
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] + distTo[v] + 1
        // provided v is reachable from s
        for (int w = 0; w < G.V(); w++) {
            if (!hasPathTo(w) || w == s) continue;
            int v = edgeTo[w];
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge " + v + "-" + w);
                StdOut.println("distTo[" + v + "] = " + distTo[v]);
                StdOut.println("distTo[" + w + "] = " + distTo[w]);
                return false;
            }
        }

        return true;
    }
}
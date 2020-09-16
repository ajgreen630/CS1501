import java.util.*;
public class WeightedGraph {
    private final int V;    // Number of vertices in the graph (final).
    private int E;          // Number of edges in the graph.
    private LinkedList<Edge>[] adj;     // Adjacency list of edges.

    /**
    * Constructor: Create an empty graph with V vertices.
    *
    * @param V Number of vertices in the graph.
    */
    public WeightedGraph(int V) { 
        if (V < 0) {    // In case the number of edges is negative.
            throw new RuntimeException("Number of vertices must be nonnegative!");
        }
        this.V = V;     // Initial number of vertices by argument.
        this.E = 0;     // Initial number of edges is 0.
        adj = (LinkedList<Edge>[]) new LinkedList[V];   // Initalize an adjacency list.
        for (int v = 0; v < V; v++) {   // Fill the adjacency list with edges.
            adj[v] = new LinkedList<Edge>();
        }
    }
    /**
    * Return the number of vertices in the graph.
    * @return The number of verices in the graph V.
    */
    public int V() {
        return V;
    }

    /**
    * Return the number of edges in the graph.
    * @return The number of edges in the graph E.
    */
    public int E() {
        return E;
    }

    /**
    * Add an edge the graph.
    *
    * @param e The edge e to add to the graph.
    */
    public void addEdge(Edge e) {
        int v = e.either();     // Set one endpoint v in the edge.
        int w = e.other(v);     // Set the other endpoint w to the endpoint v in the edge.
        adj[v].add(e);  // Add the edge at vertex v in the adjacency list.
        adj[w].add(e);  // Add the edge at vertex w in the adjacency list.
        E++;            // Increment the number of edges.
    }

    /**
    * Indicate whether or not the graph has a specified edge.
    *
    * @param v One endpoint of the edge.
    * @param w Another enopoint of the edge.
    * @return True if the graph has this edge, false otherwise.
    */
    public boolean hasEdge(int v, int w) {
        for (Edge e : adj[v]) {
            if(e.other(v) == w) {
                return true;
            }
        }
        for (Edge e : adj[w]) {
            if(e.other(w) == v) {
                return true;
            }
        }
        return false;
    }

    /**
    * Remove a specified edge from this graph.
    *
    * @param v One endpoint of the edge.
    * @param w Another endpoint of the edge.
    */
    public void removeEdge(int v, int w) {
        for (Edge e : adj[v]) {
            if(e.other(v) == w) {
                adj[v].remove(e);
                E--;
                return;
            }
        }
        for (Edge e : adj[w]) {
            if(e.other(w) == v) {
                adj[w].remove(e);
                E--;
                return;
            }
        }
        E--;    // Decrement the number of edges.
    }

    /**
    * Return the edges adjacent to vertex v as an Iterable.
    * To iterate over the edges adjacent to vertex, use foreach notation:
    *      for (Edge e: graph.adj(v)) 
    * @param v The vertex we want to examine.
    * @return Iterator of type Edge of all edges adjacaent to v.
    */
    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    /** 
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     *      for (Edge e : graph.edges())
    * @return Iterator of type Edge of all edges adjacaent to v.
    */
    public Iterable<Edge> edges() {
        LinkedList<Edge> list = new LinkedList<Edge>();
        for(int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if(e.other(v) > v) {
                    list.add(e);
                }
                // Only add one copy of each self loop:
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) {
                        list.add(e);
                    }
                    selfLoops++;
                }
            }
        }
        return list;
    }

    /**
    * Return a String representation of this graph.
    *
    * @return A String representation of this graph. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        s.append(V + " " + E + NEWLINE);
        for(int v = 0; v < V; v++) {
            s.append(v + ": ");
            for(Edge e : adj[v]) {
                s.append(e + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
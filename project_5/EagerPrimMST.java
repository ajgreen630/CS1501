/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST V E
 *  Dependencies: WeightedGraph.java Edge.java Queue.java IndexMinPQ.java
 *                UF.java
 *
 *  Prim's algorithm (Eager Prim) to compute a minimum spanning forest.
 *
 ******************************************************************************/
import java.util.*;
public class EagerPrimMST {
    private Edge[] edgeTo;          // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;        // distTo[v] = weight of shortest such edge
    private boolean[] marked;       // marked[v] = true if v is in the tree, false otherwise
    private IndexMinPQ<Double> pq;  // Indexable priority queue.

    /**
    * Constructor for this algorithm.
    *
    * @param G The weighted graph we want this algorithm to interact with.
    */
    public EagerPrimMST(WeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        for (int v = 0; v < G.V(); v++) {   // Run through each vertex
            if (!marked[v]) {               // to find the MST.
                prim(G, v);
            }
        }
    }

    /**
    * Run Prim's algorithm in graph G, starting from vertex s.
    *
    * @param G The weighted graph we want this algorithm to interact with.
    * @param s The starting vertex.
    */
    private void prim(WeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        //showPQ(pq);
        while (!pq.isEmpty()) {
            int v = pq.delMin();    // Get the vertex at the front of the queue.
            //System.out.println("    Next Vertex (Weight): " + v + " (" + distTo[v] + ")");
            scan(G, v);
            //showPQ(pq);
        }
    }

    /**
    * Scan vertex v.
    *
    * @param G The weighted graph we want this algorithm to interact with.
    * @param v The vertex we want to examine.
    */
    private void scan(WeightedGraph G, int v) {
        marked[v] = true;   // Mark the vertex as visited.
        //System.out.println("    Checking neighbors of " + v);
        for (Edge e : G.adj(v)) {   // Iterate through ajdacent edges of v.
            //System.out.println("Edge: " + e.toString());
            int w = e.other(v);     // Get the other endpoint of this vertex.
            //System.out.print("    Neighbor " + w);
            if (marked[w]) {    // If the neighbor has already been visited.
                //System.out.println(" Already in the tree.");
                continue;   // v-w is an obsolete edge.
            }
            if (e.weight() < distTo[w]) {  // If the distance of the current edge is less than the distance to the current neighbor.
                distTo[w] = e.weight();   // Set the new shortest distance.
                edgeTo[w] = e;  // Set the new edge.
                if(pq.contains(w)) {    // If the neighbor is in the priority queue.
                    pq.change(w, distTo[w]);    // Change the distance associated with w.
                }
                else {  // (Otherwise) If the neighbor is not in the priority queue.
                    pq.insert(w, distTo[w]);    // Insert w with the associated key.
                }
            }
        }
    }

    /**
    * Display the priority queue
    *
    * @param pq An indexable minimum priority queue.
    */
    private void showPQ(IndexMinPQ<Double> pq) {
        System.out.print("PQ contents: ");
        for(Integer i : pq) {
            System.out.print("(V: " + i + ", E: " + distTo[i] + ") ");
        }
        System.out.println();
    }

    /**
    * Return an iterator of edges in the MST.
    *
    * @return An iterator of type Edge of all of the edges in the MST.
    */
    public Iterable<Edge> edges() {
        LinkedList<Edge> mst = new LinkedList<Edge>();
        for (int v = 0; v < edgeTo.length; v++) {
            Edge e = edgeTo[v];
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }

    /**
    * Return the weight of the MST.
    *
    * @return The total weight of the MST.
    */
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges()) {
            weight += e.weight();
        }
        return weight;
    }
}
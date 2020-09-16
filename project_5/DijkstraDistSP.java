/*************************************************************************
 *  Compilation:  javac DijkstraDistSP.java
 *  Execution:    java DijkstraDistSP V E
 *  Dependencies: WeightedGraph.java IndexMinPQ.java Stack.java Edge.java
 *
 *  Dijkstra's algorithm. Computes the shortest path tree on a weighted, undirected graph.
 *  Assumes all weights are nonnegative.
 *
 *************************************************************************/
import java.util.*;
public class DijkstraDistSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;            // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    /**
    * Constructor for this algorithm.
    *
    * @param G The weighted graph we want this algorithm to interact with.
    * @param s The starting vertex.
    */
    public DijkstraDistSP(WeightedGraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Double.POSITIVE_INFINITY;
        }
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v)) {
                relax(e, v);
            }
        }
        // check optimality conditions
        assert check(G, s);
    }

    /**
    * relax edge e and update pq if changed
    *
    * @param E The weighted edge we want to interact with.
    * @param v The vertex at one point of the edge.
    */
    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) {
                pq.change(w, distTo[w]);
            }
            else {
                pq.insert(w, distTo[w]);
            }
        }
    }

    /**
    * length of shortest path from s to v
    *
    * @param v The vertex in the graph that we want to examine.
    * @return The distance from the starting vertex to the vertex v.
    */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
    * is there a path from s to v?
    *
    * @param v The vertex in the graph that we want to examine.
    * @return True if there is a path to v from s (i.e. distTo[v] is less than infinity), false otherwise.
    */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
    * shortest path from s to v as an Iterable, null if no such path
    *
    * @param v The vertex in the graph we want to path towards.
    * @return An iterator of type Edge as the shortest path from s to v, null if no such path exists.
    */
    public Iterable<Edge> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Edge> path = new Stack<Edge>();
        int other = v;
        for (Edge e = edgeTo[v]; e != null; e = edgeTo[other]) {
            path.push(e);
            other = e.other(other);
        }
        return path;
    }

    /**
    * check optimality conditions:
    * (i)  for all edges e:           distTo[e.to()] <= distTo[e.from()] + e.weight()
    * (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    *
    * @param s The starting point in the graph.
    * @return True if the graph is optimal, false otherwise.
    */
    private boolean check(WeightedGraph G, int s) {

        // check that edge weights are nonnegative
        for (Edge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            int v = e.either();
            if (w != e.other(v)) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }
}

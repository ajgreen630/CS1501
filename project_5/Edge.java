/*************************************************************************
 *  Compilation:  javac Edge.java
 *  Execution:    java Edge
 *
 *  Immutable weighted edge.
 *
 *************************************************************************/
import java.util.*;
import java.io.*;

/* Weighted edge in an undirected graph. */
public class Edge {
    private final int v;    // Vertex on one end
    private final int w;    // vertex on other end
    private final double distance;  // distance of the edge, i.e. the weight
    private final double price; // price of the edge
    private boolean marked;     // whether or not the edge has been marked for other algorithm's

    /**
    * Create an edge between v and w within a given weight.
    *
    * @param v One endpoint of the edge.
    * @param w Another endpoint of the edge.
    * @param distance The weight of the edge based on distance.
    * @param price The price of the edge for trips.
    */
    public Edge(int v, int w, double distance, double price) {
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.price = price;
        marked = false;
    }

    /**
    * Return the weight (distance) of this edge.
    *
    * @return The distance, or weight, of this edge.
    */
    public double weight() {
        return distance;
    }
    
    /**
    * Return the price of this edge.
    *
    * @return The price of this edge.
    */
    public double price() {
        return price;
    }

    /**
    * Return either endpoint of this edge.
    *
    * @return The endpoint v of this edge.
    */
    public int either() {
        return v;
    }

    /**
    * Return the endpoint of this edge that is different
    * from the given vertex (unless a self-loop).
    *
    * @param vertex The vertex we want to compare to.
    * @return The other endpoint of this edge based on vertex passed.
    */
    public int other(int vertex) {
        if(vertex == v) {
            return w;
        }
        else if(vertex == w) {
            return v;
        }
        else {
            throw new RuntimeException("Illegal endpoint!");
        }
    }
    /**
    * Indicate whether the edge has been visited for other algorithm's interacting
    * on the edge.
    *
    * @return True if the edge has been marked, false otherwise.
    */
    public boolean marked() {
        return marked;
    }
    /**
    * Specify whether or not an edge has been visited.
    *
    * @param flag A boolean to indicate if the edge has been marked (true if makred, false otherwise).
    */
    public void setMark(boolean flag) {
        marked = flag;
    }

    /**
    * Return a String representation of this edge (vertices are indexed from 1 to make sense for the user).
    *
    * @return A String representation of this edge.
    */
    public String toString() {
        return String.format("%d-%d      %-7.0f %-10.2f", v, w, distance, price);   // change back to v+1, w+1
    }
    /**
    * Return a String representation of this edge (vertices are indexed from 0).
    * 
    * @return A String representation of this edge.
    */
    public String toStringIndexed() {
        return String.format("%d-%d      %-7.0f %-10.2f", v, w, distance, price);
    }
}
package Utils.Graph;

/*
 * Author: Gregory Dott
 * 20-10-2022
 * 
 * This is a simple class for storing graph data (networks of nodes) as edges
 * Each edge object describes the connection between two nodes in a graph
 */

public class Edge {
    private int source, dest;

    public Edge(int source, int dest) {
        this.source = source;
        this.dest = dest;
    }

    public int getSource() {
        return source;
    }

    public int getDest() {
        return dest;
    }
}

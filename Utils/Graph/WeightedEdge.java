package Utils.Graph;

import Utils.Pr;
/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * Just a simple extension of the Edge class that now includes a weight value
 * This was first created for implementing Kruskal's algorithm
 * 
 * TODO Just as WeightedGraph can be extended to replace Graph, we might want to do the same with this class and Edge
 */

public class WeightedEdge extends Edge {
    private int weight;

    /**
     * WeightedEdge - class constructor
     * @param source 
     * @param dest
     */
    public WeightedEdge(int source, int dest, int weight) {
        super(source, dest);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void print() {
        Pr.x("Weight: " + weight + ", source: " + super.getSource() + ", dest: " + super.getDest());
    }
}

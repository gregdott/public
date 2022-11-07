package com.bibleloops.graph;

/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * Given how the cross_references.txt file is constructed, we see that each reference in one direction
 * is not always repeated in the other direction:
 * Gen.1.1	Rev.10.6 is one example.
 * 
 * I may need to explore what this means more closely at some point.
 * I was initially assuming that this would just be an undirected graph (if there is an edge between nodes, you can travel in either direction along that edge)
 * For a lot of references this might be the case, but it clearly is not the case for all of them.
 */

public class BibleEdge {
    private BibleNode source;
    private BibleNode dest;
    private int weight;

    public BibleEdge(BibleNode source, BibleNode dest, int weight) {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }

    public BibleNode getSource() {
        return this.source;
    }

    public BibleNode getDest() {
        return this.dest;
    }

    public int getWeight() {
        return this.weight;
    }
}

package Utils.Graph;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * Just a simple version of a graph implementation for weighted graphs
 * This was first created for implementing Kruskal's Algorithm
 * 
 * For Kruskal's Algorithm we need to create a list of edges that are sorted according to their weights.
 * 
 * TODO This class should probably get extended to be able to deal with unweighted graphs too. Then I can delete Graph.java and do away with a bit of redundant code. 
 * 
 * 
 * For Dijkstra's Algorithm we will be using this class now. Are there any modifications needed?
 * 
 */

public class WeightedGraph {
    private List<WeightedEdge> edgeList; // list containing the edges in the graph

    // list containing the edges of the minimum spanning tree. This gets progressively updated, 
    // so there could be points in time when it is incomplete. Is this ok in terms of naming then?
    private List<WeightedEdge> minSpanningTree; 

    private List<Integer> nodeList; // Just a list of numbered nodes.
    private List<List<Integer>> adjList; // adjacency list
    private List<WeightedEdge> orderedByWeight; // contains the same edges as in edgeList, but it is ordered according to the weight of the edge in ascending order
    private int numNodes; // number of nodes in the graph

    /**
     * WeightedGraph: constructor
     * 
     * @param edges array of arrays containing 3 ints. The first two refer to nodes and the last int refers to the weight of the edge
     * @param numNodes number of nodes in the graph
     */
    public WeightedGraph(int[][] edges, int numNodes) {
        this.numNodes = numNodes;
        edgeList = new ArrayList<WeightedEdge>();
        minSpanningTree = new ArrayList<WeightedEdge>();
        nodeList = new ArrayList<Integer>();
        adjList = new ArrayList<List<Integer>>();

        for (int i = 0; i < edges.length; i++) {
            int source, dest, weight;
            source = edges[i][0];
            dest = edges[i][1];
            weight = edges[i][2];
            WeightedEdge newEdge = new WeightedEdge(source, dest, weight);
            edgeList.add(newEdge);
        }
        
        
        
        // Initialise each list in the adjacency list
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<Integer>());
            nodeList.add(i);
        }

        // Create adjacency list from edges 
        for (WeightedEdge edge: edgeList) {
            adjList.get(edge.getSource()).add(edge.getDest());
            adjList.get(edge.getDest()).add(edge.getSource());
        }
    }

    public List<WeightedEdge> getMinSpanningTree() {
        return minSpanningTree;
    }

    public void printMinSpanningTree() {
        for(WeightedEdge edge: minSpanningTree) {
            Pr.x("Weight: " + edge.getWeight() + ", source: " + edge.getSource() + ", dest: " + edge.getDest());
        }
    }

    public void updateMinSpanningTree(WeightedEdge edge) {
        minSpanningTree.add(edge);
    }
    
    public List<WeightedEdge> getOrderedByWeightList() {
        orderedByWeight = orderByWeight(new ArrayList<WeightedEdge>(edgeList)); // we copy the list because we destroy it when ordering.
        return orderedByWeight;
    }

    public List<List<Integer>> getAdjList() {
        return adjList;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public List<Integer> getNodeList() {
        return nodeList;
    }

    /**
     * orderByWeight - uses the basic Quicksort Algorithm (recursive)
     * 
     * @param edgeList containing WeightedEdge objects, most likely unsorted (according to edge weight)
     * @return sorted List<WeightedEdge>
     */
    private List<WeightedEdge> orderByWeight(List<WeightedEdge> edgeList) {
        if (edgeList.size() <= 1) {
            return edgeList;
        } else {
            // Select pivot, arrange numbers either side of pivot and recurse
            WeightedEdge pivotEdge = edgeList.get(edgeList.size() - 1);
            int pivot = pivotEdge.getWeight();
            List<WeightedEdge> beforePivot = new ArrayList<WeightedEdge>();
            List<WeightedEdge> afterPivot = new ArrayList<WeightedEdge>();

            // Loop through unsorted elements and place them in either beforePivot or afterPivot based on their value relative to pivot
            for (int i = 0; i < edgeList.size() - 1; i++) {
                if (edgeList.get(i).getWeight() <= pivot) {
                    beforePivot.add(edgeList.get(i));
                } else {
                    afterPivot.add(edgeList.get(i));
                }
            }

            List<WeightedEdge> beforePivotSorted = orderByWeight(beforePivot); // recurse on elements before pivot
            beforePivotSorted.add(pivotEdge); // add pivot back
            List<WeightedEdge> afterPivotSorted = orderByWeight(afterPivot); // recurse on elements after pivot

            beforePivotSorted.addAll(afterPivotSorted); // join the two lists together
            return beforePivotSorted;
        }
    }

    // debug function for checking if sorting of edgeList works... It does.
    public void printEdgeList(List<WeightedEdge> edgeList) {
        Pr.x("PRINTING EDGE LIST:");
        Pr.x("=========================================");
        Pr.x("=========================================");
        for (WeightedEdge edge: edgeList) {
            edge.print();
        }
        Pr.x("=========================================");
        Pr.x("=========================================");
    }

    // debug function for checking adjList is correctly populated
    public void printAdjList() {
        for (int i = 0; i < adjList.size(); i++) {
            List<Integer> adj = adjList.get(i);
            System.out.println("NODE: " + i);
            System.out.println("Neighbours:");
            for (Integer adjNode: adj) {
                System.out.println(adjNode);
            }
        }
    }
}

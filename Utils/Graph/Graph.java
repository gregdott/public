package Utils.Graph;

import java.util.*;

/*
 * Author: Gregory Dott
 * 20-10-2022
 * 
 * Basic data structure for storing an undirected graph. 
 * This takes a collection of edges for initialisation
 * 
 * Upon construction, we create an adjacency list to make operations (searches, shortest path etc.) on the graph easier.
 * 
 * This implementation assumes nodes are numbered from 0 upwards and fall properly in sequence (0, 1, 2, ... , 101, 102 etc.)
 * So it relies on input data being of that form.
 * 
 * It also takes single nodes (nodes without neighbours) into account. That is why we need to provide the number of nodes in the graph
 * 
 */

public class Graph {
    private List<Edge> edgeList;
    private List<Integer> nodeList; // Just a list of numbered nodes.
    private List<List<Integer>> adjList;
    private int numNodes;

    /**
     * Graph: constructor
     * @param edges array of arrays containing 2 ints that each refer to a node (edge data)
     * @param numNodes number of nodes in the graph
     */
    public Graph(int[][] edges, int numNodes) {
        this.numNodes = numNodes;
        edgeList = new ArrayList<Edge>();
        nodeList = new ArrayList<Integer>();
        adjList = new ArrayList<List<Integer>>();

        for (int i = 0; i < edges.length; i++) {
            int source, dest;
            source = edges[i][0];
            dest = edges[i][1];

            Edge newEdge = new Edge(source, dest);
            edgeList.add(newEdge);
        }
        
        // Initialise each list in the adjacency list
        for (int i = 0; i < numNodes; i++) {
            adjList.add(new ArrayList<Integer>());
            nodeList.add(i);
        }

        // Create adjacency list from edges
        for (Edge edge: edgeList) {
            adjList.get(edge.getSource()).add(edge.getDest());
            adjList.get(edge.getDest()).add(edge.getSource());
        }
        
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

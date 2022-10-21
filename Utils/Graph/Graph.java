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
 */

public class Graph {
    private List<Edge> edgeList;
    private List<Integer> nodeList; // Just a list of numbered nodes.
    private List<List<Integer>> adjList;

    /**
     * Graph: constructor
     * @param edges array of arrays containing 2 ints that each refer to a node (edge data)
     */
    public Graph(int[][] edges) {
        edgeList = new ArrayList<Edge>();
        nodeList = new ArrayList<Integer>();
        adjList = new ArrayList<List<Integer>>();

        for (int i = 0; i < edges.length; i++) {
            int source, dest;
            source = edges[i][0];
            dest = edges[i][1];

            Edge newEdge = new Edge(source, dest);
            edgeList.add(newEdge);

            if (!nodeList.contains(source)) {
                nodeList.add(source);
            }
            
            if (!nodeList.contains(dest)) {
                nodeList.add(dest);
            }
        }
        
        // Initialise each list in the adjacency list
        for (int i = 0; i < nodeList.size(); i++) {
            adjList.add(new ArrayList<Integer>());
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

    public List<Integer> getNodes() {
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

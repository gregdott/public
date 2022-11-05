package Algorithms;

import java.util.*;
import Utils.Pr;
import Utils.Graph.WeightedEdge;
import Utils.Graph.WeightedGraph;

/*
 * Author: Gregory Dott
 * 31-10-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================================
 * The Bellman–Ford algorithm is an algorithm that computes shortest paths from a single source vertex to all of the other 
 * vertices in a weighted digraph. It is slower than Dijkstra's algorithm for the same problem, but more versatile, as it 
 * is capable of handling graphs in which some of the edge weights are negative numbers. The algorithm was first proposed 
 * by Alfonso Shimbel (1955), but is instead named after Richard Bellman and Lester Ford Jr., who published it in 1958 and 
 * 1956, respectively. Edward F. Moore also published a variation of the algorithm in 1959, and for this reason it is also 
 * sometimes called the Bellman–Ford–Moore algorithm.
 * ===========================================================================================================================
 * 
 * ^^^ The Wikipedia description's second sentence was clearly written by a non-English speaker. What they are saying there is
 * that it's capability of handling graphs with negative edge weights is the reason that it is slower than Dijkstra's Algo.
 * 
 * Initialisation and basic data structures are pretty much identical to what is used in the implementation of Dijkstra's Algorithm.
 * This algorithm is in fact so similar to Dijkstra's that much of the code is shared between the two implementations.
 * 
 */

public class BellmanFord {
    public static void main(String args[]) {
        int[] dist, prev;
        //int[][] weightedEdges = {{0, 1, 10}, {0, 4, 3}, {1, 2, 2}, {1, 4, -4}, {2, 3, 9}, {3, 2, 7}, {4, 1, 1}, {4, 2, 8}, {4, 3, 2}}; // for negative cycle testing
        int[][] weightedEdges = {{0, 1, 10}, {0, 4, 3}, {1, 2, 2}, {1, 4, 4}, {2, 3, 9}, {3, 2, 7}, {4, 1, 1}, {4, 2, 8}, {4, 3, 2}};

        int numVertices = 5;
        int startVertex = 0;
        WeightedGraph wg = new WeightedGraph(weightedEdges, numVertices, true);
        dist = new int[numVertices];
        prev = new int[numVertices];

        boolean noNegativeCycles = calculateShortestPaths(dist, prev, wg, startVertex);

        if (noNegativeCycles) {
            printShortestPaths(startVertex, dist, prev);
        } else {
            Pr.x("Graph contains a negative cycle!");
        }
    }

     /**
     * calculateShortestPath - calculates the shortest path from the startVertex to all other vertices in the graph
     * using the Bellman-Ford Algorithm
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param wg weighted graph object
     * @param startVertex the vertex we begin the search from
     * @throws Exception
     */
    private static boolean calculateShortestPaths(int[] dist, int[] prev, WeightedGraph wg, int startVertex) {
        
        initDataStructures(dist, prev, startVertex); // initialise the data structures we are using
        List<WeightedEdge> edgeList = wg.getEdgeList();
        for (int i = 0; i < dist.length - 1; i++) {
            for (WeightedEdge edge: edgeList) {
                if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDest()]) {
                    dist[edge.getDest()] = dist[edge.getSource()] + edge.getWeight();
                    prev[edge.getDest()] = edge.getSource();
                }
            }
        }

        // Check for negative cycles in graph. By virtue of having executed the previous for-loop [dist.length -1] times,
        // if any of our paths end up shorter, we must have a negative cycle in the graph.
        for (WeightedEdge edge: edgeList) {
            if (dist[edge.getSource()] + edge.getWeight() < dist[edge.getDest()]) {
                return false;
            }
        }

        return true;
    }

    /**
     * initDataStructures - initialise the data structures necessary for calculating the shortest paths
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param unvisited list of unvisited vertices
     * @param startVertex the vertex we begin the search from
     */
    private static void initDataStructures(int[] dist, int[] prev, int startVertex) {
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE; // initialise to largest possible value
            prev[i] = -1;
        }
        dist[startVertex] = 0;
    }

    /**
     * printShortestPaths - after calculating the shortest paths from the source vertex to all other nodes,
     * print the distance and the reconstructed paths
     * 
     * @param startVertex the vertex our search began from
     * @param dist distances from startVertex
     * @param prev tree structure storing paths to startVertex
     */
    private static void printShortestPaths(int startVertex, int[] dist, int[] prev) {
        for (int i = 0; i < dist.length; i++) {
            if (i == startVertex) continue;
            Pr.x("Path from " + startVertex + " to " + i);
            Pr.x("Distance: " + dist[i]);

            List<Integer> path = reconstructPath(startVertex, i, prev);
            int[] pathReversed = new int[path.size()];
            int count = 0;
            for (int j = path.size() - 1; j >= 0; j--) { // this just reverses the path so we can see it in the order that the nodes were visited
                pathReversed[count] = path.get(j);
                count++;
            }
            Pr.x(Arrays.toString(pathReversed));
        }
    }

    /**
     * reconstructPath - reconstruct the shortest path from startVertex to dest vertex
     * 
     * @param startVertex the vertex our search began from
     * @param dest destination vertex
     * @param prev tree structure storing paths to startVertex
     * @return
     */
    private static List<Integer> reconstructPath(int startVertex, int dest, int[] prev) {
        int currentVertex = dest;
        List<Integer> path = new ArrayList<Integer>();        
        path.add(currentVertex);
        
        while(currentVertex != startVertex) { // while we have not found our way back to the start vertex
            currentVertex = prev[currentVertex]; // traverse up the tree to the previous vertex from the current one
            path.add(currentVertex);
        }

        return path;
    }
}

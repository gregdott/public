package Algorithms;

import java.util.*;
import Utils.Graph.WeightedGraph;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 30-10-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================================
 * Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph, which may represent, 
 * for example, road networks. It was conceived by computer scientist Edsger W. Dijkstra in 1956 and published three years later.
 * 
 * The algorithm exists in many variants. Dijkstra's original algorithm found the shortest path between two given nodes,
 * but a more common variant fixes a single node as the "source" node and finds shortest paths from the source to all other 
 * nodes in the graph, producing a shortest-path tree.
 * ===========================================================================================================================
 * 
 * This implementation will find the shortest-path tree for a given graph.
 * 
 * We need the following data structures:
 * - dist[i] which stores distances from source to vertex i. 
 * - prev[i] which stores the vertex we visited before visiting i. This is essentially our tree as we can track our way back to the source
 *   vertex by following j = prev[i], then we go to prev[j] and so on until we reach the source vertex. Path reconstruction.
 * - unvisited is a list containing vertices that have not been visited yet
 * 
 * It should be noted that it is assumed that there definitely is SOME path from arbitrary node a to arbitrary node b (the graph is connected)
 * If a disconnected graph is provided there will be some errors.
 * 
 * TODO: extend functionality to account for disconnected graphs
 * 
 */

public class DijkstrasAlgorithm {

    public static void main(String args[]) {
        int[] dist, prev;
        List<Integer> unvisited;
        int[][] weightedEdges = {{0, 1, 10}, {0, 4, 3}, {1, 2, 2}, {1, 4, 4}, {2, 3, 9}, {3, 2, 7}, {4, 1, 1}, {4, 2, 8}, {4, 3, 2}};

        int numVertices = 5;
        int startVertex = 0;
        WeightedGraph wg = new WeightedGraph(weightedEdges, numVertices, true);
        dist = new int[numVertices];
        prev = new int[numVertices];
        unvisited = new ArrayList<Integer>();
        
        calculateShortestPaths(dist, prev, unvisited, wg, startVertex);
        printShortestPaths(startVertex, dist, prev);
    }

    /**
     * calculateShortestPath - calculates the shortest path from the startVertex to all other vertices in the graph
     * using Dijkstra's Algorithm
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param unvisited list of unvisited vertices
     * @param wg weighted graph object
     * @param startVertex the vertex we begin the search from
     */
    private static void calculateShortestPaths(int[] dist, int[] prev, List<Integer> unvisited, WeightedGraph wg, int startVertex) {
        initDataStructures(dist, prev, unvisited, startVertex); // initialise the data structures we are using
        List<List<Integer>> adjList = wg.getAdjList();
        while(!unvisited.isEmpty()) { // while we have vertices in our unvisited list
            int minDist = Integer.MAX_VALUE; // initialise to largest possible value
            int currentVertex = -1;
            
            // Go through list of currently unvisited vertices and find the one that is closest to startVertex
            for (Integer uv: unvisited) { // uv = unvisited vertex
                if (dist[uv] < minDist) {
                    minDist = dist[uv];
                    currentVertex = uv;
                }
            }
            unvisited.remove(Integer.valueOf(currentVertex)); // remove currentVertex from our list of unvisited vertices

            // visit all the neighbours of currentVertex
            List<Integer> neighbours = adjList.get(currentVertex);            
            for (Integer neighbour: neighbours) {
                int newDist = dist[currentVertex] + wg.getEdgeWeight(currentVertex, neighbour);
                if (newDist < dist[neighbour]) { // if the distance to the neighbour on the current path is less than what we have before, update the distance and the path
                    dist[neighbour] = newDist;
                    prev[neighbour] = currentVertex;
                }
            }
        }
    }

    /**
     * initDataStructures - initialise the data structures necessary for calculating the shortest paths
     * 
     * @param dist distances from startVertex (while calculating this stores current min distance from start to i)
     * @param prev tree structure storing paths to startVertex
     * @param unvisited list of unvisited vertices
     * @param startVertex the vertex we begin the search from
     */
    private static void initDataStructures(int[] dist, int[] prev, List<Integer> unvisited, int startVertex) {
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE; // initialise to largest possible value
            prev[i] = -1;
            unvisited.add(i);
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



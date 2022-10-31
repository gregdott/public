package Algos;

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

        initDataStructures(dist, prev, unvisited, startVertex);
        calculateShortestPath(dist, prev, unvisited, wg);
        //printShortestPaths(startVertex, dist, prev);

        for (int i = 0; i < dist.length; i++) {
            if (i == startVertex) continue;
            Pr.x("Path from " + startVertex + " to " + i);
            Pr.x("Distance: " + dist[i]);

            List<Integer> path = reconstructPath(startVertex, i, prev);
            for (int j = path.size() - 1; j >= 0; j--) {
                Pr.x("(" + path.get(j) + ")");
            }
        }
        
    }

    private static void printShortestPaths(int startVertex, int[] dist, int[] prev) {
        for (int i = 0; i < dist.length; i++) {
            for (int j = 0; j < dist.length; j++) {
                List<Integer> path = reconstructPath(i, j, prev);
                Pr.x(path.toString());
            }
        }
        
    }

    private static List<Integer> reconstructPath(int startVertex, int dest, int[] prev) {
        int currentVertex = dest;
        List<Integer> path = new ArrayList<Integer>();        
        path.add(currentVertex);
        
        while(currentVertex != startVertex) {
            currentVertex = prev[currentVertex];
            path.add(currentVertex);
        }

        return path;
    }

    private static void calculateShortestPath(int[] dist, int[] prev, List<Integer> unvisited, WeightedGraph wg) {
        List<List<Integer>> adjList = wg.getAdjList();
        while(!unvisited.isEmpty()) { // while we have vertices in our unvisited list
            int minDist = Integer.MAX_VALUE;
            int currentVertex = -1;
            
            for (Integer uv: unvisited) { // uv = unvisited vertex
                if (dist[uv] < minDist) {
                    minDist = dist[uv];
                    currentVertex = uv;
                }
            }
            unvisited.remove(Integer.valueOf(currentVertex));

            // visit all the neighbours of currentVertex
            List<Integer> neighbours = adjList.get(currentVertex);            
            for (Integer neighbour: neighbours) {
                int newDist = dist[currentVertex] + wg.getEdgeWeight(currentVertex, neighbour);
                if (newDist < dist[neighbour]) {
                    dist[neighbour] = newDist;
                    prev[neighbour] = currentVertex;
                }
            }

        }
    }

    private static void initDataStructures(int[] dist, int[] prev, List<Integer> unvisited, int startVertex) {
        for (int i = 0; i < dist.length; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            unvisited.add(i);
        }
        dist[startVertex] = 0;
    }
}



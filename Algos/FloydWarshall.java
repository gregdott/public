package Algos;

import java.util.Arrays;
import Utils.Graph.WeightedGraph;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 29-10-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================================
 * In computer science, the Floyd–Warshall algorithm (also known as Floyd's algorithm, the Roy–Warshall algorithm, 
 * the Roy–Floyd algorithm, or the WFI algorithm) is an algorithm for finding shortest paths in a directed weighted graph 
 * with positive or negative edge weights (but with no negative cycles). A single execution of the algorithm will find 
 * the lengths (summed weights) of shortest paths between all pairs of vertices. Although it does not return details of 
 * the paths themselves, it is possible to reconstruct the paths with simple modifications to the algorithm.
 * ===========================================================================================================================
 */

public class FloydWarshall {
    public static void main(String args[]) {
        int[][] weightedEdges = {{0, 2, -2}, {2, 3, 2}, {3, 1, -1}, {1, 2, 3}, {1, 0, 4}}; // first 2 elements are vertex indices, last element is edge weight
        int numVertices = 4;
        int[][] distanceMatrix = new int[4][4]; // create a distance matrix with as many rows and columns as there are vertices

        //WeightedGraph wg = new WeightedGraph(weightedEdges, numVertices, true);
        initDistanceMatrix(distanceMatrix, weightedEdges);

        calculateShortestPaths(distanceMatrix);
    }

    private static void initDistanceMatrix(int[][] distanceMatrix, int[][] weightedEdges) {
        // Initialise all distances between vertices to be Integer.MAX_VALUE to begin - this is to approximate infinity.
        // Important: before adding anything to a value, we will need to check if it is equal to Integer.MAX_VALUE. 
        // If we don't do this, we potentially end up with a negative number instead (binary addition - when number exceeds max representable number,
        // it wraps to the max negatives which is bad in our case, so we avoid adding anything to the simulated infinite values)
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
            }    
        }


        for(int[] edge: weightedEdges) {
            distanceMatrix[edge[0]][edge[1]] = edge[2];
        }

        for(int i = 0; i < distanceMatrix.length; i++) {
            distanceMatrix[i][i] = 0;
        }
    }

    private static void calculateShortestPaths(int[][] distanceMatrix) {

        for (int k = 0; k < distanceMatrix.length; k++) {
            for (int i = 0; i < distanceMatrix.length; i++) {
                for (int j = 0; j < distanceMatrix.length; j++) {
                    if (distanceMatrix[i][k] != Integer.MAX_VALUE && distanceMatrix[k][j] != Integer.MAX_VALUE) { // This is our clunky way of simulating infinity. It's not pretty, but it does the trick.
                        if (distanceMatrix[i][j] > distanceMatrix[i][k] + distanceMatrix[k][j]) {
                            distanceMatrix[i][j] = distanceMatrix[i][k] + distanceMatrix[k][j];
                        }
                    }
                }
            }
        }

        // Print the distance matrix
        for (int i = 0; i < distanceMatrix.length; i++) {
            Pr.x(Arrays.toString(distanceMatrix[i]));
        }
    }
    
}

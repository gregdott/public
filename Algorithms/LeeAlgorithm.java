package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 01-11-2022
 * 
 * From Wikipedia:
 * =============================================================================================================
 * The Lee algorithm is one possible solution for maze routing problems based on breadth-first search. It 
 * always gives an optimal solution, if one exists, but is slow and requires considerable memory.
 * =============================================================================================================
 * 
 * In this implementation it is assumed that both the source and destination points can be visited (value of 1 in the input matrix below).
 */

public class LeeAlgorithm {
    public static void main(String args[]) {
        int[][] matrix =
        {
            { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
            { 0, 1, 1, 1, 1, 1, 0, 1, 0, 1 },
            { 0, 0, 1, 0, 1, 1, 1, 0, 0, 1 },
            { 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
            { 0, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
            { 1, 0, 1, 1, 1, 0, 0, 1, 1, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 1, 0, 1 },
            { 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
            { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
            { 0, 0, 1, 0, 0, 1, 1, 0, 0, 1 },
        };
        propagateWave(matrix, 0, 0, 7, 5); // search for the route from (0,0) to the desination (7, 5)
    }

    /**
     * propagateWave - move through the matrix using what is essentially a Breadth-First search
     * 
     * @param matrix represents the basic graph where 1 means we can travel there and 0 means we can not
     * @param sourceX the x coordinate in the matrix of our starting point
     * @param sourceY the y coordinate in the matrix of our starting point
     * @param destX  the x coordinate in the matrix of our destination point
     * @param destY the y coordinate in the matrix of our destination point
     */
    private static void propagateWave(int[][] matrix, int sourceX, int sourceY, int destX, int destY) {
        int[][] waveMatrix = initWave(matrix); // initialise the wave matrix
        waveMatrix[sourceX][sourceY] = 0; // distance from start to itself is zero
        boolean reachedDest = false;
        List<int[]> nextToVisit = new ArrayList<int[]>(); // list of next points to visit
        nextToVisit.add(new int[]{sourceX, sourceY}); // add source as first point to visit

        int count = 0; // to keep track of our wave propagation so we can backtrack later
        while (!reachedDest) {
            count++;
            List<int[]> newNextToVisit = new ArrayList<int[]>(); // the next set of points to visit after we have visited all points in nextToVisit
            
            for(int[] point: nextToVisit) { // loop through current set of next points to visit in the matrix
                reachedDest = visitNeighbours(waveMatrix, point[0], point[1], destX, destY, newNextToVisit, count);
                if (reachedDest) {
                    break;
                }
            }

            nextToVisit = newNextToVisit; // next iteration we visit those points we found in this iteration
        }

        backtrack(waveMatrix, destX, destY, sourceX, sourceY); // backtrack through our wave matrix to find the shortest path
    }

    /**
     * visitNeighbours - given a point, find all the neighbours we can visit from there and add them to the list
     * also update the waveMatrix with the current wave count value
     * 
     * @param waveMatrix keeps track of wave propagation
     * @param x current position x
     * @param y current position y
     * @param dx destination position x
     * @param dy destination position y
     * @param newNextToVisit the next set of points to visit after we have visited all points in nextToVisit
     * @param count current wave value
     * @return boolean representing whether we have reached our destination yet
     */
    private static boolean visitNeighbours(int[][] waveMatrix, int x, int y, int dx, int dy, List<int[]> newNextToVisit, int count) {
       
        List<int[]> vals = new ArrayList<int[]>();
        int length = waveMatrix.length;

        /*
         * For any point (x, y) we have potentially a max of 4 neighbours.
         * This could be as few as 2 when our point is in a corner or 3 when our point is along the edge of the matrix
         * Perpendicular adjacency in the matrix corresponds to an edge in the graph
         */
        if (x - 1 >= 0) vals.add(new int[]{x - 1, y});
        if (x + 1 < length) vals.add(new int[]{x + 1, y});
        if (y - 1 >= 0) vals.add(new int[]{x, y - 1});
        if (y + 1 < length) vals.add(new int[]{x, y + 1});

        for(int[] point: vals) {
            if (waveMatrix[point[0]][point[1]] == -1) { // not visited yet and can visit (!=-2)
                waveMatrix[point[0]][point[1]] = count;
                newNextToVisit.add(new int[]{point[0], point[1]});

                if (point[0] == dx && point[1] == dy) {
                    Pr.x("We have reached our destination! Distance from source: " + count);
                    return true; // no need to execute any further
                }
            }
        }
        return false;
    }

    /**
     * initWave - initialises another matrix that keeps track of distances from the source.
     * We also initialise points that are inaccessible to -2 so that we only need to consult
     * one data structure while executing the Lee algorithm
     * 
     * @param matrix contain 0 or 1 depending on whether it represents a traversable point
     * @return
     */
    private static int[][] initWave(int[][] matrix) {
        int length = matrix.length;
        int [][] waveMatrix = new int[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (matrix[i][j] == 1) {
                    waveMatrix[i][j] = -1; // initialise unvisited, but visitable points to -1
                } else {
                    waveMatrix[i][j] = -2; // initialise unvisitable points to -2
                }
            }
        }
        return waveMatrix;
    }

    /**
     * backtrack - tracks through our wave matrix to find the(a) shortest path from the start to the destination.
     * There is the possibility that we can have 2 shortest paths (that's why the (a) above). When this happens
     * we're not too concerned in this implementation. There may be cases where we might want to optimise a 
     * certain kind of path structure (less corners or something like that), but that has not been taken
     * into consideration here. The path returned here in this case is dependent upon the order in which
     * we visit neighbouring nodes when backtracking.
     * 
     * @param waveMatrix contains our propagated wave
     * @param destX destination point x value
     * @param destY destination point y value
     * @param sourceX starting point x value
     * @param sourceY starting point y value
     */
    private static void backtrack(int[][] waveMatrix, int destX, int destY, int sourceX, int sourceY) {
        int length = waveMatrix.length;
        int[][] pathMatrix = new int[length][length]; // matrix containing 0's and 1's to represent the shortest path
        boolean reachedSource = false;
        int count = waveMatrix[destX][destY];
        int[] nextToVisit = new int[]{destX, destY}; // starting now from our destination
        // While we have not got back to the source, explore neighbours and go to the neighbour with the lowest value in the waveMatrix
        while (!reachedSource) {
            if (nextToVisit[0] == sourceX && nextToVisit[1] == sourceY) {
                reachedSource = true;   
            }
            pathMatrix[nextToVisit[0]][nextToVisit[1]] = 1;
            nextToVisit = visitNeighboursBackTrack(waveMatrix, nextToVisit[0], nextToVisit[1], sourceX, sourceY, count);
            count--;

            
        }
        Pr.x("Path from source (" + sourceX + "," + sourceY + ") to destination (" + destX + "," + destY + "):");
        printMatrix(pathMatrix);
    }

    /**
     * visitNeighboursBackTrack - from the current point, find the next neighbour we should visit based on the 
     * wave count value of the current point's neighbours
     * 
     * @param waveMatrix
     * @param x current x coordinate in our backtracking
     * @param y current y coordinate in our backtracking
     * @param ox x coordinate of origin (source)
     * @param oy y coordinate of origin (source)
     * @param count current wave propagation value
     * @return int[2] representing the next point to visit
     */
    private static int[] visitNeighboursBackTrack(int[][] waveMatrix, int x, int y, int ox, int oy, int count) {
        List<int[]> vals = new ArrayList<int[]>();
        int length = waveMatrix.length;
        int[] nextToVisit = new int[2];

        // get coordinates of neighbours
        if (x - 1 >= 0) vals.add(new int[]{x - 1, y});
        if (x + 1 < length) vals.add(new int[]{x + 1, y});
        if (y - 1 >= 0) vals.add(new int[]{x, y - 1});
        if (y + 1 < length) vals.add(new int[]{x, y + 1});

        // visit neighbours and check wave count at each point. Choose next point to visit as the first one that has count less than or equal to current count.
        for(int[] point: vals) {
            if (waveMatrix[point[0]][point[1]] > 0 && waveMatrix[point[0]][point[1]] <= count) {
                nextToVisit = point;
                break;
            }
        }

        return nextToVisit;
    }

    /**
     * printMatrix - does what it says on the tin
     * @param matrix n x n matrix of ints
     */
    private static void printMatrix(int[][] matrix) {
        for (int[] row: matrix) {
            Pr.x(Arrays.toString(row));
        }
    }
}

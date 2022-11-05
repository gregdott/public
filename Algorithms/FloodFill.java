package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 01-11-2022
 * 
 * From Wikipedia:
 * ==============================================================================================================================================
 * Flood fill, also called seed fill, is a flooding algorithm that determines and alters the area connected to a given node in a 
 * multi-dimensional array with some matching attribute. It is used in the "bucket" fill tool of paint programs to fill connected, 
 * similarly-colored areas with a different color, and in games such as Go and Minesweeper for determining which pieces are cleared. 
 * A variant called boundary fill uses the same algorithms but is defined as the area connected to a given node that does not have a particular attribute.
 * ==============================================================================================================================================
 * 
 * 2 implementations are provided:
 * 
 * 1.) The four-way, recursive version
 * 2.) Data-structure based version
 * 
 */

public class FloodFill {

    /**
     * 
     * @param node int[2] representing the starting point of the fill (node[0] - x, node[1] - y)
     */
    private static void floodFillRecursive(char[][] matrix, int[] node, char initialColour, char newColour) {
        if (matrix[node[0]][node[1]] != initialColour) { // don't want to replace colours that are not our initial colour
            return;
        } else {
            matrix[node[0]][node[1]] = newColour;
        }

        int length = matrix.length;
        List<int[]> neighbours = new ArrayList<int[]>(); 
        if (node[0] - 1 >= 0) neighbours.add(new int[]{node[0] - 1, node[1]});
        if (node[0] + 1 < length) neighbours.add(new int[]{node[0] + 1, node[1]});
        if (node[1] - 1 >= 0) neighbours.add(new int[]{node[0], node[1] - 1});
        if (node[1] + 1 < length) neighbours.add(new int[]{node[0], node[1] + 1});

        for(int[] neighbour: neighbours) {
            floodFillRecursive(matrix, neighbour, initialColour, newColour);
        }
    }

    private static void floodFillIterative(char[][] matrix, int[] startNode, char initialColour, char newColour) {
        int length = matrix.length;
        List<int[]> toFill = new ArrayList<int[]>();
        toFill.add(startNode);

        while(!toFill.isEmpty()) {
            int[] node = toFill.get(0);
            matrix[node[0]][node[1]] = newColour;
            toFill.remove(node);
            if (node[0] - 1 >= 0 && matrix[node[0] - 1][node[1]] == initialColour) toFill.add(new int[]{node[0] - 1, node[1]});
            if (node[0] + 1 < length && matrix[node[0] + 1][node[1]] == initialColour) toFill.add(new int[]{node[0] + 1, node[1]});
            if (node[1] - 1 >= 0 && matrix[node[0]][node[1] - 1] == initialColour) toFill.add(new int[]{node[0], node[1] - 1});
            if (node[1] + 1 < length && matrix[node[0]][node[1] + 1] == initialColour) toFill.add(new int[]{node[0], node[1] + 1});
        }
    }

    public static void main(String args[]) {
        char[][] matrix = {
            "YYYGGGGGGG".toCharArray(),
            "YYYYYYGXXX".toCharArray(),
            "GGGGGGGXXX".toCharArray(),
            "WWWWWGGGGX".toCharArray(),
            "WRRRRRGXXX".toCharArray(),
            "WWWRRGGXXX".toCharArray(),
            "WBWRRRRRRX".toCharArray(),
            "WBBBBRRXXX".toCharArray(),
            "WBBXBBBBXX".toCharArray(),
            "WBBXXXXXXX".toCharArray()
        };

        //floodFillRecursive(matrix, new int[]{3, 9}, matrix[3][9], 'C');
        floodFillIterative(matrix, new int[]{3, 9}, matrix[3][9], 'C');
        printMatrix(matrix);
    }

    /**
     * printMatrix - does what it says on the tin
     * @param matrix n x n matrix of ints
     */
    private static void printMatrix(char[][] matrix) {
        for (char[] row: matrix) {
            Pr.x(Arrays.toString(row));
        }
    }
    
}

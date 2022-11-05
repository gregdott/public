package Algorithms;

import java.util.*;
import Utils.Graph.*;
import Utils.Pr;


/*
 * Author: Gregory Dott
 * 20-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * Depth-first search (DFS) is an algorithm for traversing or searching tree or graph data structures. 
 * The algorithm starts at the root node (selecting some arbitrary node as the root node in the case of a graph) 
 * and explores as far as possible along each branch before backtracking. Extra memory, usually a stack, 
 * is needed to keep track of the nodes discovered so far along a specified branch which helps in backtracking of the graph.
 * ======================================================================================================================================
 * 
 * There are 2 implentations provided:
 *  
 * Iterative
 * Recursive
 * 
 * The basic procedure is this:
 * From start node, go to first neighbour, then proceed to that neighbour's first neighbour
 * and so on until either we can go no further or until we reach a node already visited (graph is not necessarily acyclic)
 * Then, we go back to the previous node and explore other neighbours (if there are any) as far down as possible until traversing up
 * and doing the same. This will be done until all nodes have been visited.
 * 
 * In order to keep track of where we are in the graph (so that we can backtrack once we hit a dead end), we use an ArrayList
 * that keeps track of our current path. We also use an ArrayList that keeps track of which nodes we have visited. We need both
 * of these to traverse the graph (at least in this implementation).
 */

public class DepthFirstSearch {

    public static void main(String args[]) {
        int [][] edges = {{1, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};
        int startNode = 0;
        Graph g = new Graph(edges, 15);

        //-----------------------------------------------------------------------
        // Iterative:
        Pr.x("----------------------------------------------------");
        Pr.x("Depth First Search (Iterative):");

        depthFirstSearchIterative(g, startNode);

        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        Pr.x("----------------------------------------------------");
        Pr.x("Depth First Search (Recursive):");

        List<Integer> nodesVisited, currentPath, nodesNotVisited;
        nodesVisited = new ArrayList<Integer>();
        currentPath = new ArrayList<Integer>();
        nodesNotVisited = new ArrayList<Integer>(g.getNodeList());

        currentPath.add(startNode);
        depthFirstSearchRecursive(g, nodesVisited, currentPath, nodesNotVisited);

        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------
        
    }

    /**
    * depthFirstSearchIterative - iterative implementation of the Depth-First Search Algorithm
    * Prints out nodes visited in order along with the current path
    * Nodes are only considered visited (added to nodesVisited & removed from nodesNotVisited) once we have explored all of their neighbours.
    * 
    * @param g graph containing an adjacency list
    * @param startNode the node that we start exploring the graph from
    * 
    */
    public static void depthFirstSearchIterative(Graph g, int startNode) {
        int currentNode;
        List<Integer> nodesVisited, currentPath, nodesNotVisited;
        List<List<Integer>> adjList;

        nodesVisited = new ArrayList<Integer>(); // list of nodes visited so far
        currentPath = new ArrayList<Integer>(); // path describing where we started, and which nodes we have travelled along to reach the current node.
        nodesNotVisited = new ArrayList<Integer>(g.getNodeList()); //nodes that we have not visited yet.
        adjList = g.getAdjList();
        
        currentPath.add(startNode);

        // Loop until we have visited all nodes
        while (nodesVisited.size() < g.getNumNodes()) {

            if (currentPath.size() == 0) {
                // No nodes in current path, but graph has not been exhausted, so we have reached a disconnect. Proceed to the first node in nodesNotVisited
                Pr.x("Found a disconnect. Moving to next unvisited node.");
                currentNode = nodesNotVisited.get(0);
                currentPath.add(currentNode);
            } else {
                currentNode = currentPath.get(currentPath.size() - 1); // Get last element on current path. Our current node...
            }
            

            Pr.x("CURRENT NODE: " + currentNode);

            List<Integer> neighbours = adjList.get(currentNode);
            boolean allNeighboursVisited = true;

            // Loop through neighbours of current node. 
            // If we have not visited the neighbour, and the neighbour is not on our current path, add it to the current path & continue exploring that node
            for (Integer neighbour: neighbours) {
                if (!nodesVisited.contains(neighbour) && !currentPath.contains(neighbour)) {
                    allNeighboursVisited = false;
                    currentPath.add(neighbour);
                    break;
                }
            }

            // All neighbours of the current node have been visited.
            if (allNeighboursVisited) {
                if (currentPath.size() > 0) {
                    currentPath.remove(currentPath.size() - 1); // remove last element on current path so we go back up the tree.
                }
                
                nodesVisited.add(currentNode); // add current node to nodes visited (we have explored all neighbours)
                
                nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode)); // remove current node from nodes not visited (and not already removed)
            }

            Pr.x(currentPath.toString());
        }
    }

    /**
     * depthFirstSearchRecursive - recursive implementation of the Depth-First Search Algorithm
     * Prints out nodes visited in order along with the current path
     * Nodes are only considered visited (added to nodesVisited & removed from nodesNotVisited) once we have explored all of their neighbours.
     * 
     * @param g graph containing an adjacency list
     * @param nodesVisited list of nodes visited so far
     * @param currentPath path describing where we started, and which nodes we have travelled along to reach the current node.
     * @param nodesNotVisited nodes that we have not visited yet.
     * 
     */
    public static void depthFirstSearchRecursive(Graph g, List<Integer> nodesVisited, List<Integer> currentPath, List<Integer> nodesNotVisited) {
        if (nodesVisited.size() == g.getNumNodes()) { // All nodes have been visited
            return;
        }

        int currentNode;

        if (currentPath.size() == 0) {
            // No nodes in current path, but graph has not been exhausted, so we have reached a disconnect. Proceed to the first node in nodesNotVisited
            Pr.x("Found a disconnect. Moving to next unvisited node.");
            currentNode = nodesNotVisited.get(0);
            currentPath.add(currentNode);
        } else {
            currentNode = currentPath.get(currentPath.size() - 1); // Get last element on current path. Our current node...
        }

        Pr.x("CURRENT NODE: " + currentNode);

        List<Integer> neighbours = g.getAdjList().get(currentNode);
        boolean allNeighboursVisited = true;

        // Loop through neighbours of current node. 
        // If we have not visited the neighbour, and the neighbour is not on our current path, add it to the current path & continue exploring that node
        for (Integer neighbour: neighbours) {
            if (!nodesVisited.contains(neighbour) && !currentPath.contains(neighbour)) {
                allNeighboursVisited = false;
                currentPath.add(neighbour);
                break;
            }
        }

        // All neighbours of the current node have been visited.
        if (allNeighboursVisited) {
            
            if (currentPath.size() > 0) {
                currentPath.remove(currentPath.size() - 1); // Remove last element on current path so we go back up the tree.
            }
            
            nodesVisited.add(currentNode); // Add current node to nodes visited (we have explored all neighbours, so now we can say the node certainly has been visited)
                       
            nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode)); // remove current node from nodes not visited (and not already removed)
        }

        Pr.x(currentPath.toString());

        depthFirstSearchRecursive(g, nodesVisited, currentPath, nodesNotVisited);
    }
}
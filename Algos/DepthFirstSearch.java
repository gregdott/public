package Algos;

import java.util.*;
import Utils.Graph.*;

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
 * of these to traverse the graph.
 */

public class DepthFirstSearch {

    public static void main(String args[]) {
        int [][] edges = {{1, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};
        int startNode = 0;
        Graph g = new Graph(edges, 15);

        //-----------------------------------------------------------------------
        // Iterative:
        System.out.println("----------------------------------------------------");
        System.out.println("Depth First Search (Iterative):");

        //DFSIterative(g, startNode);

        System.out.println("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        System.out.println("----------------------------------------------------");
        System.out.println("Depth First Search (Recursive):");

        List<Integer> nodesVisited, currentPath, nodesNotVisited;
        nodesVisited = new ArrayList<Integer>();
        currentPath = new ArrayList<Integer>();
        nodesNotVisited = new ArrayList<Integer>(g.getNodeList());

        currentPath.add(startNode);
        DFSRecursive(g, nodesVisited, currentPath, nodesNotVisited);

        System.out.println("----------------------------------------------------");
        //-----------------------------------------------------------------------
        
    }

    public static void DFSRecursive(Graph g, List<Integer> nodesVisited, List<Integer> currentPath, List<Integer> nodesNotVisited) {
        if (nodesVisited.size() == g.getNumNodes()) {
            return;
        }

        int currentNode;

        if (currentPath.size() == 0) {
            System.out.println("Found a disconnect. Moving to next unvisited node.");
            currentNode = nodesNotVisited.get(0);
            currentPath.add(currentNode);
        } else {
            currentNode = currentPath.get(currentPath.size() - 1); // get last element on current path.
        }

        System.out.println("CURRENT NODE: " + currentNode);

        List<Integer> neighbours = g.getAdjList().get(currentNode);

        boolean allNeighboursVisited = true;
        for (Integer neighbour: neighbours) {
            if (!nodesVisited.contains(neighbour) && !currentPath.contains(neighbour) && neighbour != currentNode) {
                allNeighboursVisited = false;
                currentPath.add(neighbour);
                break;
            }
        }

        if (allNeighboursVisited) {
            // Backtrack to previous node
            if (currentPath.size() > 0) {
                currentPath.remove(currentPath.size() - 1); // remove last element on current path so we go back up the tree.
            }
            
            nodesVisited.add(currentNode); // add current node to nodes visited (we have explored all neighbours)
            
            if (nodesNotVisited.contains(currentNode)) {
                nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode)); // remove current node from nodes not visited (and not already removed)
            }
        }

        System.out.println(currentPath.toString());

        DFSRecursive(g, nodesVisited, currentPath, nodesNotVisited);
        
    }

    public static void DFSIterative(Graph g, int startNode) {
        int currentNode;
        List<Integer> nodesVisited, currentPath, nodesNotVisited;
        List<List<Integer>> adjList;

        nodesVisited = new ArrayList<Integer>();
        currentPath = new ArrayList<Integer>();
        nodesNotVisited = new ArrayList<Integer>(g.getNodeList());
        adjList = g.getAdjList();
        
        currentPath.add(startNode);
        int count = 0; // DEBUG

        while (nodesVisited.size() < g.getNumNodes() || count > 30) {
            count++;

            if (currentPath.size() == 0) {
                System.out.println("Found a disconnect. Moving to next unvisited node.");
                currentNode = nodesNotVisited.get(0);
                currentPath.add(currentNode);
            } else {
                currentNode = currentPath.get(currentPath.size() - 1); // get last element on current path.
            }
            

            System.out.println("CURRENT NODE: " + currentNode);

            List<Integer> neighbours = adjList.get(currentNode);

            boolean allNeighboursVisited = true;
            for (Integer neighbour: neighbours) {
                if (!nodesVisited.contains(neighbour) && !currentPath.contains(neighbour) && neighbour != currentNode) {
                    allNeighboursVisited = false;
                    currentPath.add(neighbour);
                    break;
                }
            }

            if (allNeighboursVisited) {
                // Backtrack to previous node
                if (currentPath.size() > 0) {
                    currentPath.remove(currentPath.size() - 1); // remove last element on current path so we go back up the tree.
                }
                
                nodesVisited.add(currentNode); // add current node to nodes visited (we have explored all neighbours)
                
                if (nodesNotVisited.contains(currentNode)) {
                    nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode)); // remove current node from nodes not visited (and not already removed)
                }
            }

            System.out.println(currentPath.toString());
            
        }
    }
}
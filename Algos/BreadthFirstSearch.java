package Algos;

import java.util.*;
import Utils.Graph.*;

/*
 * Author: Gregory Dott
 * 20-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * Breadth-first search (BFS) is an algorithm for searching a tree data structure for a node that satisfies a given property. 
 * It starts at the tree root and explores all nodes at the present depth prior to moving on to the nodes at the next depth level. 
 * Extra memory, usually a queue, is needed to keep track of the child nodes that were encountered but not yet explored.
 * ======================================================================================================================================
 * 
 * There are 2 implentations provided:
 *  
 * Iterative (using edges)
 * Recursive (using edges)
 * 
 * The initial implementation just traverses the graph from a given node and prints the nodes in the order they are traversed.
 * Once this is done, it will be elementary to add additional logic for finding a given node and terminating execution.
 */
public class BreadthFirstSearch {
    public static void main(String args[])  {
        int[][] edges = {{0, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}};

        Graph g = new Graph(edges);

        //-----------------------------------------------------------------------
        // Iterative:
        //BFSIterative(g, 0);
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        List<Integer> nodesToVisit = new ArrayList<Integer>();
        List<Integer> nodesVisited = new ArrayList<Integer>();

        nodesToVisit.add(0);

        BFSRecursive(g, nodesVisited, nodesToVisit);
        //-----------------------------------------------------------------------
    }

    public static void BFSRecursive(Graph g, List<Integer> nodesVisited, List<Integer> nodesToVisit) {
        // This will work somewhat similarly to the recursive version...
        // What is our termination condition? All nodes have been visited
        
        if (nodesVisited.size() == g.getNodes().size()) {
            return;
        }

        int currentNode = nodesToVisit.get(0); // Get next node to visit
        nodesVisited.add(currentNode);
        System.out.println(currentNode);

        nodesToVisit.remove(0); // remove node we are visiting from list of nodes to visit

        List<Integer> neighbours = g.getAdjList().get(currentNode);

        for (int neighbour: neighbours) {
            if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                nodesToVisit.add(neighbour);
            }
        }

        System.out.println("NODES TO VISIT: " + nodesToVisit.toString());

        BFSRecursive(g, nodesVisited, nodesToVisit);
        
    }

    public static void BFSIterative(Graph g, int startNode) {
        // Given Graph g (containing adjacency list which we will use) and start node, we begin traversing the graph.
        // From where we start, we visit all nodes neighbouring the current node.
        // Then, sequentially we visit all neighbours of the neighbouring nodes and so on.
        // What is our terminating condition? All nodes have been explored. So we need a list containing explored nodes? That would be one way
        // Then each iteration we check explored nodes against nodelist and terminate if they contain the same elements. size() gives quickest reflection.

        List<List<Integer>> adjList = g.getAdjList();
        List<Integer> nodesVisited = new ArrayList<Integer>();
        boolean allNodesVisited = false;
        int currentNode;
        List<Integer> nodesToVisit = new ArrayList<Integer>();
        nodesToVisit.add(startNode);

        
        while (!allNodesVisited) {

            currentNode = nodesToVisit.get(0); // get next node to visit
            nodesVisited.add(currentNode);
            System.out.println(currentNode);

            nodesToVisit.remove(0); // remove from array keeping track of which nodes we need to visit
            
            List<Integer> neighbours = adjList.get(currentNode);

            for (int neighbour: neighbours) {
                if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                    nodesToVisit.add(neighbour);
                }
            }

            System.out.println("NODES TO VISIT: " + nodesToVisit.toString());

            if (nodesVisited.size() == g.getNodes().size()) {
                allNodesVisited = true;
            }
        }
    }

    
}

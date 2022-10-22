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
 * Iterative
 * Recursive
 * 
 * The initial implementation just traverses the graph from a given node and prints the nodes in the order they are traversed.
 * Once this is done, it will be easy to add additional logic for finding a given node and terminating execution.
 * 
 * Given the edges representing a graph, the number of nodes and the start node, we begin traversing the graph.
 * We construct a Graph object and when we do the construction we create an adjacency list from the edges.
 * We use the adjacency list to easily traverse the graph.
 * From where we start, we visit all nodes neighbouring the current node.
 * Then, sequentially we visit all neighbours of the neighbouring nodes and so on.
 * Terminating condition: All nodes have been explored. So we need a list containing explored nodes, along with a list of nodes to explore.
 * Then each iteration we check explored nodes against nodelist and terminate if they contain the same elements. size() gives quickest reflection.
 * 
 * NB need to fix for exception case I just found. Incrementing is not a valid approach when graph is disconnected. Unless we have some weird assumptions but let's avoid that
 */

public class BreadthFirstSearch {
    public static void main(String args[])  {
        //int[][] edges = {{0, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}};
        //int [][] edges = {{1, 0}, {1, 3}, {1, 4}, {0, 5}, {0, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};
        int [][] edges = {{1, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};

        Graph g = new Graph(edges, 15);
        
        //-----------------------------------------------------------------------
        // Iterative:
        System.out.println("----------------------------------------------------");
        System.out.println("Breadth First Search (Iterative):");
        BFSIterative(g, 0);
        System.out.println("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        System.out.println("----------------------------------------------------");
        System.out.println("Breadth First Search (Recursive):");
        List<Integer> nodesToVisit = new ArrayList<Integer>();
        List<Integer> nodesVisited = new ArrayList<Integer>();

        nodesToVisit.add(0);

        BFSRecursive(g, nodesVisited, nodesToVisit);
        System.out.println("----------------------------------------------------");
        
        //-----------------------------------------------------------------------
    }

    /**
     * BFSIterative - implementation of Breadth First Search algorithm using an iterative approach.
     * Prints out nodes visited in order along with the current list of nodes to visit.
     * 
     * @param g graph containing an adjacency list
     * @param startNode the node we are beginning the search from
     */
    public static void BFSIterative(Graph g, int startNode) {
        List<List<Integer>> adjList = g.getAdjList();
        List<Integer> nodesVisited = new ArrayList<Integer>(); // list of nodes visited so far
        List<Integer> nodesToVisit = new ArrayList<Integer>(); // list of nodes to visit (in order)
        boolean allNodesVisited = false;
        int currentNode;
        
        nodesToVisit.add(startNode);
        
        while (!allNodesVisited) {
            if (nodesToVisit.size() == 0) { // we have not yet visited all nodes, but we can go no further from the current node
                // *********** NB FIX THIS with another list!
                currentNode = nodesVisited.get(nodesVisited.size() - 1) + 1; // go to the next node in the sequence when we can go no further.
            } else {
                currentNode = nodesToVisit.get(0); // Get next node to visit
                nodesToVisit.remove(0); // remove node we are visiting from list of nodes to visit
            }

            nodesVisited.add(currentNode);
            System.out.println(currentNode);
            
            List<Integer> neighbours = adjList.get(currentNode);

            // Loop through neighbours of current node and add them to nodesToVisit if they are not in there yet and they have not been visited
            for (int neighbour: neighbours) {
                if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                    nodesToVisit.add(neighbour);
                }
            }

            System.out.println("NODES TO VISIT: " + nodesToVisit.toString());

            // Termination condition: All nodes have been visited
            if (nodesVisited.size() == g.getNumNodes()) {
                allNodesVisited = true;
            }
        }
    }

    /**
     * BFSRecursive - implementation of Breadth First Search algorithm using a recursive approach.
     * Prints out nodes visited in order along with the current list of nodes to visit.
     *     
     * @param g graph containing an adjacency list
     * @param nodesVisited list of nodes visited so far
     * @param nodesToVisit list of nodes to visit (in order)
     */
    public static void BFSRecursive(Graph g, List<Integer> nodesVisited, List<Integer> nodesToVisit) {
        
        // Termination condition: All nodes have been visited
        if (nodesVisited.size() == g.getNumNodes()) {
            return;
        }

        int currentNode;

        if (nodesToVisit.size() == 0) { // we have not yet visited all nodes, but we can go no further from the current node
            // *********** NB FIX THIS with another list!
            currentNode = nodesVisited.get(nodesVisited.size() - 1) + 1; // go to the next node in the sequence when we can go no further.
        } else {
            currentNode = nodesToVisit.get(0); // Get next node to visit
            nodesToVisit.remove(0); // remove node we are visiting from list of nodes to visit
        }

        nodesVisited.add(currentNode);
        System.out.println(currentNode);

        List<Integer> neighbours = g.getAdjList().get(currentNode);

        // Loop through neighbours of current node and add them to nodesToVisit if they are not in there yet and they have not been visited
        for (int neighbour: neighbours) {
            if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                nodesToVisit.add(neighbour);
            }
        }

        System.out.println("NODES TO VISIT: " + nodesToVisit.toString());

        BFSRecursive(g, nodesVisited, nodesToVisit);
    }
}

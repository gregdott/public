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
 */

public class BreadthFirstSearch {
    public static void main(String args[])  {
        //int[][] edges = {{0, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 3}, {2, 5}, {3, 4}, {3, 5}, {4, 5}};
        //int [][] edges = {{1, 0}, {1, 3}, {1, 4}, {0, 5}, {0, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};
        int [][] edges = {{1, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}, {5, 9}, {5, 10}, {4, 7}, {4, 8}, {7, 11}, {7, 12}};

        Graph g = new Graph(edges, 15);
        
        //-----------------------------------------------------------------------
        // Iterative:
        Pr.x("----------------------------------------------------");
        Pr.x("Breadth First Search (Iterative):");

        breadthFirstSearchIterative(g, 0);

        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        Pr.x("----------------------------------------------------");
        Pr.x("Breadth First Search (Recursive):");

        List<Integer> nodesToVisit = new ArrayList<Integer>();
        List<Integer> nodesVisited = new ArrayList<Integer>();
        List<Integer> nodesNotVisited = new ArrayList<>(g.getNodeList()); // list of nodes not visited yet. we shallow copy so we don't edit what is on the object
        nodesToVisit.add(0);

        breadthFirstSearchRecursive(g, nodesVisited, nodesToVisit, nodesNotVisited);

        Pr.x("----------------------------------------------------");
        
        //-----------------------------------------------------------------------
    }

    /**
     * breadthFirstSearchIterative - implementation of Breadth First Search algorithm using an iterative approach.
     * Prints out nodes visited in order along with the current list of nodes to visit.
     * 
     * @param g graph containing an adjacency list
     * @param startNode the node we are beginning the search from
     */
    public static void breadthFirstSearchIterative(Graph g, int startNode) {
        List<List<Integer>> adjList = g.getAdjList(); // get adjacency list from graph
        List<Integer> nodesVisited = new ArrayList<Integer>(); // list of nodes visited so far
        List<Integer> nodesToVisit = new ArrayList<Integer>(); // list of nodes to visit (in order)
        List<Integer> nodesNotVisited = new ArrayList<>(g.getNodeList()); // list of nodes not visited yet. we shallow copy so we don't edit what is on the object
        
        int currentNode;
        
        nodesToVisit.add(startNode);
        
        while (nodesVisited.size() < g.getNumNodes()) {
            if (nodesToVisit.size() == 0) { // we have not yet visited all nodes, but we can go no further from the current node
                Pr.x("Found a disconnect. Moving to next unvisited node.");
                currentNode = nodesNotVisited.get(0);
            } else {
                currentNode = nodesToVisit.get(0); // Get next node to visit
                nodesToVisit.remove(0); // remove node we are visiting from list of nodes to visit
            }
            
            Pr.x(Integer.toString(currentNode));

            nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode));
            nodesVisited.add(currentNode);

            
            
            List<Integer> neighbours = adjList.get(currentNode);

            // Loop through neighbours of current node and add them to nodesToVisit if they are not in there yet and they have not been visited
            for (int neighbour: neighbours) {
                if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                    nodesToVisit.add(neighbour);
                }
            }

            Pr.x("NODES TO VISIT: " + nodesToVisit.toString());
        }
    }

    /**
     * breadthFirstSearchRecursive - implementation of Breadth First Search algorithm using a recursive approach.
     * Prints out nodes visited in order along with the current list of nodes to visit.
     *     
     * @param g graph containing an adjacency list
     * @param nodesVisited list of nodes visited so far
     * @param nodesToVisit list of nodes to visit (in order)
     * @param nodesNotVisited list of nodes not visited yet
     */
    public static void breadthFirstSearchRecursive(Graph g, List<Integer> nodesVisited, List<Integer> nodesToVisit, List<Integer> nodesNotVisited) {
        
        // Termination condition: All nodes have been visited
        if (nodesVisited.size() == g.getNumNodes()) {
            return;
        }

        int currentNode;

        if (nodesToVisit.size() == 0) { // we have not yet visited all nodes, but we can go no further from the current node           
            Pr.x("Found a disconnect. Moving to next unvisited node.");
            currentNode = nodesNotVisited.get(0);
        } else {
            currentNode = nodesToVisit.get(0); // Get next node to visit
            nodesToVisit.remove(0); // remove node we are visiting from list of nodes to visit
        }

        Pr.x(Integer.toString(currentNode));
        
        nodesNotVisited.remove(nodesNotVisited.indexOf(currentNode));
        nodesVisited.add(currentNode);

        

        List<Integer> neighbours = g.getAdjList().get(currentNode);

        // Loop through neighbours of current node and add them to nodesToVisit if they are not in there yet and they have not been visited
        for (int neighbour: neighbours) {
            if (!nodesVisited.contains(neighbour) && !nodesToVisit.contains(neighbour)) {
                nodesToVisit.add(neighbour);
            }
        }

        Pr.x("NODES TO VISIT: " + nodesToVisit.toString());

        breadthFirstSearchRecursive(g, nodesVisited, nodesToVisit, nodesNotVisited);
    }
}

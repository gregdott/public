package Algorithms;

import java.util.*;
//import Utils.*;
import Utils.Graph.*;


/*
 * Author: Gregory Dott
 * 26-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * Kruskal's algorithm finds a minimum spanning forest of an undirected edge-weighted graph. If the graph is connected, it finds a 
 * minimum spanning tree. (A minimum spanning tree of a connected graph is a subset of the edges that forms a tree that includes every 
 * vertex, where the sum of the weights of all the edges in the tree is minimized. For a disconnected graph, a minimum spanning 
 * forest is composed of a minimum spanning tree for each connected component.) It is a greedy algorithm in graph theory as in each 
 * step it adds the next lowest-weight edge that will not form a cycle to the minimum spanning forest.
 * ======================================================================================================================================
 * 
 * More simply (from https://www.techiedelight.com/kruskals-algorithm-for-finding-minimum-spanning-tree/)
 * A Minimum Spanning Tree is a spanning tree of a connected, undirected graph. It connects all the vertices with minimal total weighting for its edges.
 * 
 * For this implementation we are assuming that the graph is connected, so we are looking for a minimum spanning tree (MST) (only 1). Not a forest.
 * At a later stage it will be good to extend this for unconnected graphs as well. 
 * 
 * 
 * Basic algorithm:
 * (requires additional implementation of Union–Find stuff (3 operations: MAKE-SET, FIND-SET, UNION))
 * 
       KRUSKAL(graph G)
 
        MST = {}
        
        for each vertex v belonging G.V:
            MAKE-SET(v)
        
        for each (u, v) in G.E ordered by weight(u, v), increasing:
            if FIND-SET(u) != FIND-SET(v):
                add {(u, v)} to set MST
                UNION(u, v)
        
        return MST
 */

public class KruskalsAlgorithm {
    public static void main(String args[]) {
        //int[][] weightedEdges = {{0, 1, 7}, {1, 2, 8}, {0, 3, 5}, {1, 3, 9}, {1, 4, 7}, {2, 4, 5}, {3, 4, 15}, {3, 5, 6}, {4, 5, 8}, {4, 6, 9}, {5, 6, 11}};
        int[][] weightedEdges = {{0, 1, 4}, {0, 7, 8}, {1, 2, 8}, {1, 7, 11}, {2, 3, 7}, {2, 8, 2}, {2, 5, 4}, {3, 4, 9}, {3, 5, 14}, {4, 5, 10}, {5, 6, 2}, {6, 7, 1}, {6, 8, 6}, {7, 8, 7}};
        WeightedGraph wg = new WeightedGraph(weightedEdges, 9, false); // Initialise weighted graph AND create list of edges ordered according to their weight (asc)
        findMST(wg);
    }

    private static List<UFNode> initUFNodes(int numNodes) {
        List<UFNode> ufNodes = new ArrayList<UFNode>(); // List of nodes for use in UnionFind logic
        // for each node in the graph we create a UFNode. These function as linked lists for tracking the progress of finding the MST
        for (int i = 0; i < numNodes; i++) {
            UFNode newNode = new UFNode(i, i); // Instantiate a ufnode and set it as its own root. Each node is a disjoint set in the beginning
            ufNodes.add(newNode);
        }
        return ufNodes;
    }

    private static void findMST(WeightedGraph wg) {
        List<UFNode> ufNodes = initUFNodes(wg.getNumNodes()); // List of nodes for use in UnionFind logic
        List<WeightedEdge> orderedByWeight = wg.getOrderedByWeightList(); // get the list of edges ordered by weight
        
        for (WeightedEdge edge: orderedByWeight) {
            // do a find() for the nodes in this edge - find gets the root of a node
            if (UnionFind.find(edge.getSource(), ufNodes) != UnionFind.find(edge.getDest(), ufNodes)) {
                // the two nodes do not have the same origin, so we know we aren't creating a cycle and can safely add this edge to the MST
                UnionFind.union(edge, ufNodes, wg);
            }
        }
        
        wg.printMinSpanningTree(); // print out the minimum spanning tree
    }
}

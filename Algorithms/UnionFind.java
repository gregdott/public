package Algorithms;

import java.util.*;
import Utils.Graph.*;
//import Utils.Pr;

/*
 * Author: Gregory Dott
 * 27-10-2022
 * 
 * From Wikipedia:
 * =============================================================================================================================
 * In computer science, a disjoint-set data structure, also called a union–find data structure or merge–find set, is a data 
 * structure that stores a collection of disjoint (non-overlapping) sets. Equivalently, it stores a partition of a set into 
 * disjoint subsets. It provides operations for adding new sets, merging sets (replacing them by their union), and finding 
 * a representative member of a set. The last operation makes it possible to find out efficiently if any two elements are in 
 * the same or different sets.
 * =============================================================================================================================
 * 
 * This was implemented as part of implementing Kruskals Algorithm, so the implementation is particular to a weighted graph.
 */

public class UnionFind {
    
    /**
     * makeSet - makes a new set (linked list) with the node as its own parent for starters
     * This gets added to a List<UFNode> to keep track of the linked lists for all the nodes in the graph
     * 
     * @param node the node number
     * @return UFNode instance
     */
    public static UFNode makeSet(int node) {
        UFNode ufNode = new UFNode(node, node); // Initialise the node as the root of its own tree
        return ufNode;
    }

    /**
     * find - given a node and the List<UFNode> of all the other nodes, find the root of this node by traversing the linked list
     * 
     * @param node a node index
     * @param forest List<UFNode> containing a UFNode for each node in the graph
     * @return the index of the origin/root node for the given node
     */
    public static int find(int node, List<UFNode> forest) {
        UFNode currentNode = forest.get(node);
        
        while(currentNode.getParent() != currentNode.getNode()) { // while we have not found the root of the tree
            currentNode = forest.get(currentNode.getParent());
        }
        
        return currentNode.getNode();
    }

    // when doing a union, we take one node's tree and turn it upside down and connect it to the other node.
    // at this point, we know there is an edge between source and dest that does not create a cycle and that it is the optimal node to add for the
    // minimum spanning tree. It does not matter where the root is, so we just use the first node's root
    // For purposes other than ours (Kruskal's Algo for now), we might want to look at which tree we merge into which
    // However, seeing as we are constructing a MST, this does not matter here.

    /**
     * union - This union assumes that we have already checked that the 2 nodes in the edge given do not share the same root
     * So we don't check for that here
     * In the current implementation, we don't worry about the order in which we merge things together,
     * as the end result will be identical regardless (at least in terms of the total weight of the MST). Depending on edge weights provided,
     * we could have 2 or more different MST's for the same graph though interestingly. 
     * This will have an impact on performance for much larger graphs if we have to traverse very deep trees.
     * I imagine we could have the task of finding the deepest MST or the shallowest MST (most sensible and efficient) for a graph. In these cases it would be necessary
     * to store size/rank on UFNodes. This has not been done in this case, so efficiency has not been fully optimised.
     * 
     * @param edge the current edge we want to add to the MST
     * @param forest the list of UFNodes
     * @param wg our weighted graph (we alter this by updating the MST)
     */
    public static void union(WeightedEdge edge, List<UFNode> forest, WeightedGraph wg) {
        UFNode currentNode = forest.get(edge.getDest()); // get UFNode for edge dest
        UFNode previousNode = forest.get(edge.getSource()); // get UFNode for edge source

        int oldParent;
        boolean reachedRoot = false;
        
        /*
         * Below we basically take two linked lists and merge them.
         * We keep the one associated with the source node intact and then we turn the other one upside and append it to the source node
         */
        do {
            if (currentNode.getParent() == currentNode.getNode()) { // UFNode with parent as same value is a root
                reachedRoot = true;
            }
            
            oldParent = currentNode.getParent(); // get the parent of the current node (so that we can visit that node next)
            currentNode.setParent(previousNode.getNode()); // set the parent of the current node to its new parent (part of inverting the tree)

            previousNode = currentNode;
            currentNode = forest.get(oldParent); // move to old parent so we can make the node we just dealt with into its parent
            
        } while (!reachedRoot);
       
        wg.updateMinSpanningTree(edge); // update the MST on the WeightedGraph object
    }
}
package Algos;

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
    
    public static UFNode makeSet(int node) {
        UFNode ufNode = new UFNode(node, node); // Initialise the node as the root of its own tree
        return ufNode;
    }

    // get the root of the given node
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
    public static void union(WeightedEdge edge, List<UFNode> forest, WeightedGraph wg) {
        UFNode currentNode = forest.get(edge.getDest());
        UFNode previousNode = forest.get(edge.getSource());

        int oldParent;
        boolean reachedRoot = false;
        
        do {
            if (currentNode.getParent() == currentNode.getNode()) {
                reachedRoot = true;
            }
            
            oldParent = currentNode.getParent();
            currentNode.setParent(previousNode.getNode());

            previousNode = currentNode;
            currentNode = forest.get(oldParent);
            
        } while (!reachedRoot);
       
        wg.updateMinSpanningTree(edge);
    }
}
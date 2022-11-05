package Algorithms;

import java.util.*;

import Utils.Graph.WeightedEdge;
import Utils.Graph.WeightedGraph;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 02-11-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================
 * In computer science, a topological sort or topological ordering of a directed graph is a linear ordering 
 * of its vertices such that for every directed edge uv from vertex u to vertex v, u comes before v in the 
 * ordering. For instance, the vertices of the graph may represent tasks to be performed, and the edges may 
 * represent constraints that one task must be performed before another; in this application, a topological 
 * ordering is just a valid sequence for the tasks. Precisely, a topological sort is a graph traversal in 
 * which each node v is visited only after all its dependencies are visited.
 * 
 * Kahn's Algorithm:
 * One of these algorithms, first described by Kahn (1962), works by choosing vertices in the same order as 
 * the eventual topological sort. First, find a list of "start nodes" which have no incoming edges and insert 
 * them into a set S; at least one such node must exist in a non-empty acyclic graph. 
 * ===========================================================================================================
 */

public class TopologicalSort {

    public static void main(String args[]) {
        // In order to avoid having to rework the WeightedGraph class right now, just using it and setting edge weights all to 0
        int[][] edges =  {{0, 6, 0}, {1, 2, 0}, {1, 4, 0}, {1, 6, 0}, {3, 0, 0}, {3, 4, 0}, {5, 1, 0}, {7, 0, 0}, {7, 1, 0}};
        WeightedGraph wg = new WeightedGraph(edges, 8, true);
        List<Integer> sortedVertices = kahnTopologicalSort(wg);
        Pr.x(sortedVertices.toString());
    }
    
    private static List<Integer> kahnTopologicalSort(WeightedGraph wg) {
        List<Integer> sortedVertices = new ArrayList<Integer>();
        List<Integer> noIncomingEdge = new ArrayList<Integer>(); // list of vertices with no incoming edge
        getVerticesWithNoIncomingEdges(wg, noIncomingEdge, sortedVertices); // list of vertices with no incoming edge
        
        while (!noIncomingEdge.isEmpty()) {
            int vertex = noIncomingEdge.get(0);
            sortedVertices.add(vertex);
            noIncomingEdge.remove(0);
            // remove all edges coming from this vertex now...
            removeEdgesForVertex(vertex, wg);
            getVerticesWithNoIncomingEdges(wg, noIncomingEdge, sortedVertices); // update list of vertices to visit next after having removed edges from previous vertex
        }

        return sortedVertices;
    }

    /**
     * removeEdgesForVertex - removes the outgoing edges from vertex in the graph wg
     * 
     * @param vertex the vertex for which we want to remove all outgoing edges
     * @param wg WeightedGraph object
     */
    private static void removeEdgesForVertex(int vertex, WeightedGraph wg) {
        List<WeightedEdge> edgeList = wg.getEdgeList();
        List<WeightedEdge> toRemove = new ArrayList<WeightedEdge>();
        for (WeightedEdge edge: edgeList) {
            if (edge.getSource() == vertex) {
                toRemove.add(edge);
            }
        }
        // Seeing as we are using the enhanced for-loop we can't just remove the edge while looping as this will cause a concurrent modification exception
        edgeList.removeAll(toRemove);
        wg.updateAdjList();
    }

    /**
     * getVerticesWithNoIncomingEdges - there are 2 ways we can do this as far as I can tell. This is one.
     * The other can be done when creating the graph and making the adjacency list. We can basically create a reverse
     * adjacency list in that case. Probably less processing, but makes graph class a touch more confusing, so doing
     * it this way.
     * 
     * @param wg WeightedGraph object containing our graph data
     * @return
     */
    private static void getVerticesWithNoIncomingEdges(WeightedGraph wg, List<Integer> noIncomingEdge, List<Integer> sortedVertices) {
        List<List<Integer>> adjList = wg.getAdjList();
        List<Integer> nodeList = new ArrayList<Integer>(wg.getNodeList());

        for (List<Integer> neighbours: adjList) {
            for (Integer neighbour: neighbours) {
                if (nodeList.contains(neighbour)) {
                    nodeList.remove(nodeList.indexOf(neighbour));
                }
            }
        }

        for (Integer node: nodeList) {
            if (!noIncomingEdge.contains(node) && !sortedVertices.contains(node)) {
                noIncomingEdge.add(node);
            }
        }
    }
}

package com.bibleloops.graph;

import java.util.*;

import org.json.JSONArray;

import com.bibleloops.Pr;


/*
 * Author: Gregory Dott
 * 05-11-2022
 */

public class BibleNode {
    private String book; // the book the verse is contained in. "Ge", "Ex" etc.
    private int chapterNumber; // verse chapter number
    private int verseNumber; // verse number
    private String text; // the text of the verse
    //private int weight; // not sure how weights of nodes will be determined, but I believe something like this will be required

    // neighbours and futureNeighbours should be complements of one another: together they make up all possible neighbours. They must be disjoint sets.
    private List<BibleNode> neighbours; // contains list of instantiated neighbours (neighbours for which we have created a BibleNode object)
    private List<FutureNeighbour> futureNeighbours; // contains list of uninstantiated neighbours (objects representing edges to nodes that we have not created yet)

    public BibleNode(String book, int chapterNumber, int verseNumber, String text, JSONArray narr) {
        futureNeighbours = new ArrayList<FutureNeighbour>();
        neighbours = new ArrayList<BibleNode>();
        this.book = book;
        this.chapterNumber = chapterNumber;
        this.verseNumber = verseNumber;
        this.text = text;
        addFutureNeighbours(narr);
    }

    public void addNeighbour(BibleNode newNeighbour) {
        neighbours.add(newNeighbour);
    }

    // add future neighbours to node. all those neighbours for which we have not (potentially at least) instantiated nodes
    private void addFutureNeighbours(JSONArray narr) {
        // we may at some point (VERY LIKELY) want to order this by weight desc. Then we can add edges in order of high weight first
        for (int i = 0; i < narr.length(); i++) {
            String destString = narr.getJSONObject(i).getString("dest");
            //Pr.x("FUTURE NEIGHBOUR: " + destString);
            int weight = narr.getJSONObject(i).getInt("weight");
            FutureNeighbour fn = new FutureNeighbour(destString, weight);
            futureNeighbours.add(fn);
        }
    }

    public List<FutureNeighbour> getFutureNeighbours() {
        return futureNeighbours;
    }

    public List<BibleNode> getNeighbours() {
        return neighbours;
    }

    public String getText() {
        return this.text;
    }

    // public int getWeight() {
    //     return this.weight;
    // }

    public void print() {
        Pr.x("Book: " + book);
        Pr.x("Chapter: " + chapterNumber);
        Pr.x("Verse: " + verseNumber);
        Pr.x("Text: " + text);
    }

}

// Just a little placeholder for before we create an actual bible node.
class FutureNeighbour {
    String nodeId; // book, verse num, chapter num
    int weight; // weight of the edge

    public FutureNeighbour(String nodeId, int weight) {
        this.nodeId = nodeId;
        this.weight = weight;
    }
}


/*
    * At this point, another consideration needs to be made:
    * We have an edgelist stored for a vertex in the database. At a particular point in the creation of a graph, we might not have instantiated
    * all of these edges (have not created nodes for their destination and stored a reference to the destination on the source)
    * 
    * So I think what needs to be done is we keep a list of edges on the node in addition to the actual edges that we store.
    * So we have potential edges and instantiated edges or something to that effect. It would make sense if these 2 sets were complements of each other:
    * in other words potential edges will contain all the edges if none are instantiated. If some are instantiated it will only contain those edges
    * that are not instantiated. Then the union of these 2 sets will give us all of the edges leaving the current node.
    */
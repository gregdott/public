package com.bibleloops.graph;

import java.util.*;
import java.util.concurrent.Future;

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

    public String getVerseId() {
        return book + "." + chapterNumber + "." + verseNumber;
    }

    public void addNeighbour(BibleNode newNeighbour) {
        neighbours.add(newNeighbour);
    }

    /**
     * addFutureNeighbours -  We load the neighbours and then sort them in descending order of edge weight
     * These are neighbours for which we have not (potentially at least) instantiated nodes. We could have instantiated
     * some of these nodes previously as a result of them being adjacent to some other already-existing node, but we 
     * have not attempted that yet from the perspective of the current node.
     * 
     * @param narr jsonarray of adj list from db for the current node.
     */
    private void addFutureNeighbours(JSONArray narr) {
        for (int i = 0; i < narr.length(); i++) {
            String destString = narr.getJSONObject(i).getString("dest");
            int weight = narr.getJSONObject(i).getInt("weight");
            FutureNeighbour fn = new FutureNeighbour(destString, weight);
            futureNeighbours.add(fn);
        }

        futureNeighbours = sortFutureNeighbours(futureNeighbours); // sort them!
    }

    // just a debug function to check that everything is good.
    private void printFutureNeighbours() {
        for (int i = 0; i < futureNeighbours.size(); i++) {
            FutureNeighbour fn = futureNeighbours.get(i);
            fn.print();
        }
    }

    /**
     * Quicksort for our FutureNeighbour objects. sorts in descending order so we can always 
     * choose the one with the greatest edge weight from the front of the list
     * 
     * @param fns list of future neighbours, most likely unsorted in terms of weight
     * @return list of future neighbours sorted in terms of weight
     */
    private static List<FutureNeighbour> sortFutureNeighbours(List<FutureNeighbour> fns) {
        if (fns.size() <= 1) {
            return fns;
        } else {
            FutureNeighbour pivot = fns.get(fns.size() - 1); // get last element of array
            
            List<FutureNeighbour> beforePivot = new ArrayList<FutureNeighbour>();
            List<FutureNeighbour> afterPivot = new ArrayList<FutureNeighbour>();

            // Loop through unsorted elements and place them in either beforePivot or afterPivot based on their value relative to pivot
            for (int i = 0; i < fns.size() - 1; i++) {
                if (fns.get(i).weight <= pivot.weight) {
                    afterPivot.add(fns.get(i));
                } else {
                    beforePivot.add(fns.get(i));
                }
            }
            
            List<FutureNeighbour> sortedList = sortFutureNeighbours(beforePivot); // recurse on elements before pivot
            sortedList.add(pivot); // add pivot back
            sortedList.addAll(sortFutureNeighbours(afterPivot)); // recurse on elements after pivot
            
            return sortedList;
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
        Pr.x("**********************************************************");
        Pr.x(book + "." + chapterNumber + "." + verseNumber + ": " + text);
        Pr.x("Neighbours:");
        for (int i = 0; i < neighbours.size(); i++) {
            BibleNode bn = neighbours.get(i);
            Pr.x(bn.getVerseId());
        }
        Pr.x("**********************************************************");
        // Pr.x("Chapter: " + chapterNumber);
        // Pr.x("Verse: " + verseNumber);
        // Pr.x("Text: " + text);
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

    public void print() {
        Pr.x("NODE ID: " + nodeId);
        Pr.x("WEIGHT: " + weight);
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
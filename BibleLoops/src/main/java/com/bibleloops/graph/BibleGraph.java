package com.bibleloops.graph;

import com.bibleloops.Pr;
import java.util.*;
import java.io.IOException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.*;

//-----------------------------------------------------------------------------
// for removing annoying mongodb log messages
import java.util.logging.Logger;
import java.util.logging.Level;
//-----------------------------------------------------------------------------

/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * Class for creating graphs from Biblical data in various ways and interacting with them
 * 
 * Job.30.2 - no edges leaving it!
 */

public class BibleGraph {
    static Hashtable<String, BibleNode> bibleNodes = new Hashtable<String, BibleNode>();

    public static void main(String args[]) throws IOException {

        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------

        BibleGraph bg = new BibleGraph("Ge", 1, 1, 1);
       
    }

    /**
     * BibleGraph - create a BibleGraph object
     * 
     * @param startVerse the verse to begin graph construction from. we need some start node otherwise where do we begin???
     * @param limit the number of vertices allowed in this graph. depending on how the graph is constructed, this limit could have various implications.
     * 
     */
    public BibleGraph(String book, int chapterNum, int verseNum, int limit) {
        Document verseDoc = getVerse(book, chapterNum, verseNum);

        String verseJSON = verseDoc.toJson();
        JSONObject vjo = new JSONObject(verseJSON);
        JSONArray narr = vjo.getJSONArray("adj"); // neighbours

        BibleNode firstNode = new BibleNode(book, chapterNum, verseNum, verseDoc.get("text").toString(), narr);

        String verseId = book + "." + chapterNum + "." + verseNum;
        bibleNodes.put(verseId, firstNode);

        List<BibleNode> nodesToExplore = new ArrayList<BibleNode>(); // keeps track of which nodes we have not explored fully yet
        nodesToExplore.add(firstNode);

        int nodeCount = 1;

        BibleNode currentNode;
        
        while(nodeCount < limit && !nodesToExplore.isEmpty()) { // nodesToExplore will probably never be empty at the level we will be working at... unless we place conditions on edge weights...
            currentNode = nodesToExplore.get(0);
            List<FutureNeighbour> fns = currentNode.getFutureNeighbours();
            Pr.x("NODE COUNT: " + nodeCount);
            while (nodeCount < limit && !fns.isEmpty()) { // loop through future neighbours for current node
                FutureNeighbour cfn = fns.get(0);
                
                // ignoring hyphenated entries for now (some verse to another). need to figure out how to deal with these. Multiple edges?
                if (!cfn.nodeId.contains("-") && !bibleNodes.containsKey(cfn.nodeId)) { // also don't want to create nodes that already exist
                    BibleNode newNode = getNodeFromString(cfn.nodeId);
                    bibleNodes.put(cfn.nodeId, newNode);
                    nodesToExplore.add(newNode);
                    currentNode.addNeighbour(newNode);
                    nodeCount++;
                }
                fns.remove(0); // when we remove from here, we are also removing the entry on the node which we want
            }
            nodesToExplore.remove(0); // remove this node after having explored it
        }

        //printGraph();
    }

    /*
     * In terms of graph creation, I am going to explore several possible ways of creating graphs:
     * 
     * Unconcsious Breadth-First (just expand all edges etc. from source node breadth-first until we reach our limit)
     * Concsious Breadth-First (instead of expanding every possible edge, we only expand edges with certain weights - most sensibly greater than some value)
     * Conscious Depth-First (Here we need to have some means of choosing which edge to follow, edge with greatest weight etc.)
     * Unconscious Depth-First (Randomise the edge we follow)
     * Depth-First Multiphase? (Depth-First construction up until some limit, then proceed again from source or perhaps a child... Needs thought)
     * Randomised (randomly choose depth or breadth first approach for each node or something. some way of randomising how we move through)
     * Alternating dfs & bfs?
     * 
     * So, the first thing that is needed is to sort the edges belonging to a node in descending order of weight.
     * Then create separate graph building function here to build them 
     */

    private static void printGraph() {
        Enumeration<String> iterator = bibleNodes.keys();
        while(iterator.hasMoreElements()) {
            String key = iterator.nextElement();
            BibleNode bn = bibleNodes.get(key);
            Pr.x("===========================================");
            Pr.x("KEY: " + key);
            bn.print();
        }
    }

    // given a string (usually from an edge representing a destination node), get the BibleNode for it. so parse the string and find the verse.
    private static BibleNode getNodeFromString(String vString) {
        String[] destBits = vString.split("\\.");
        String book = destBits[0];
        int chapter = Integer.parseInt(destBits[1]);
        int verse = Integer.parseInt(destBits[2]);

        Document verseDoc = getVerse(book, chapter, verse);
        
        String verseJSON = verseDoc.toJson();
        JSONObject vjo = new JSONObject(verseJSON);
        JSONArray narr = new JSONArray();
        //vjo.getJSONArray("adj"); // neighbours

        if (vjo.has("adj")) {
            narr = vjo.getJSONArray("adj"); // neighbours
        }

        BibleNode newNode = new BibleNode(book, chapter, verse, verseDoc.get("text").toString(), narr);
        return newNode;
    }

    // will use somewhere at some point. just storing this here for now
    public static Document getVerse(String book, int chapterNum, int verseNum) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("bibleloops");
            MongoCollection<Document> bCol = db.getCollection(book);

            Map<String, String> selector = new HashMap<String, String>();
            selector.put("chapter", Integer.toString(chapterNum));
            selector.put("verse", Integer.toString(verseNum));

            Document doc = bCol.find(new Document(selector)).first();
            
            return doc;
        }
    }
    
}

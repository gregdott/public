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
 * 
 */

public class BibleGraph {
    static Hashtable<String, BibleNode> bibleNodes = new Hashtable<String, BibleNode>();

    /**
     * BibleGraph - create a BibleGraph object
     * 
     * @param startVerse the verse to begin graph construction from. we need some start node otherwise where do we begin???
     * @param limit the number of vertices allowed in this graph. depending on how the graph is constructed, this limit could have various implications.
     * 
     * eg. do we just create as many nodes as possible (up to the limit) by exploring all edges of our startVerse and outwards?
     * or do we only consider those edges with a weight greater than some number?
     * or do we go depth first and just explore one edge from each vertex?
     * etc. etc. etc.
     * Many ways these things can be done...
     * 
     * For starting experiments, I'm just going to go breadth first: explore all edges of startNode, continue with edges of neighbours
     * and so on until we have reached our vertex limit.
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

        printGraph();
    }

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

    public static void main(String args[]) throws IOException {

        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------

        BibleGraph bg = new BibleGraph("Ge", 1, 1, 100);
       
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
        JSONArray narr = vjo.getJSONArray("adj"); // neighbours
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

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
    public BibleGraph(String book, String chapterNum, String verseNum, int limit) {
        Document verseDoc = getVerse(book, chapterNum, verseNum);
        //Document adj = (Document)verseDoc.get("adj");

        String verseJSON = verseDoc.toJson();
        JSONObject vjo = new JSONObject(verseJSON);
        JSONArray narr = vjo.getJSONArray("adj"); // neighbours

       

        BibleNode firstNode = new BibleNode(book, chapterNum, verseNum, verseDoc.get("text").toString(), 0);

        String verseId = book + "." + chapterNum + "." + verseNum;
        bibleNodes.put(verseId, firstNode);

        List<BibleNode> nodesToExplore = new ArrayList<BibleNode>(); // keeps track of which nodes we have not explored fully yet
        nodesToExplore.add(firstNode);

        int nodeCount = 1;

        BibleNode currentNode = nodesToExplore.get(0);
        
        while(nodeCount < limit && !nodesToExplore.isEmpty()) { // nodesToExplore will probably never be empty at the level we will be working at... unless we place conditions on edge weights...
            for (int i = 0; i < narr.length(); i++) {
                if (nodeCount >= limit) break;
                //String post_id = arr.getJSONObject(i).getString("post_id");
                //Pr.x(narr.getJSONObject(i).getString("dest").toString());
                String destString = narr.getJSONObject(i).getString("dest");

                if (!destString.contains("-")) { // ignoring hyphenated entries for now (some verse to another). need to figure out how to deal with these. Multiple edges?
                    //Pr.x(destString);
                    if (!bibleNodes.containsKey(destString)) { // don't want to create nodes that already exist
                        BibleNode newNode = getNodeFromString(destString);
                        bibleNodes.put(destString, newNode);
                        nodesToExplore.add(newNode);
                        nodeCount++;
                    }
                    
                }
            }
        }

        Pr.x(bibleNodes.toString());
        
        //Pr.x(verseDoc.get("adj").toString());
    }

    public static void main(String args[]) throws IOException {

        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------

        BibleGraph bg = new BibleGraph("Ge", "1", "1", 10);
       
    }

    // given a string (usually from an edge representing a destination node), get the BibleNode for it. so parse the string and find the verse.
    private static BibleNode getNodeFromString(String vString) {
        String[] destBits = vString.split("\\.");
        String book = destBits[0];
        String chapter = destBits[1];
        String verse = destBits[2];

        Document verseDoc = getVerse(book, chapter, verse);
        BibleNode newNode = new BibleNode(book, chapter, verse, verseDoc.get("text").toString(), 0);
        return newNode;
    }

    // will use somewhere at some point. just storing this here for now
    public static Document getVerse(String book, String chapterNum, String verseNum) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("bibleloops");
            MongoCollection<Document> bCol = db.getCollection(book);

            Map<String, String> selector = new HashMap<String, String>();
            selector.put("chapter", chapterNum);
            selector.put("verse", verseNum);

            Document doc = bCol.find(new Document(selector)).first();
            
            return doc;
        }
    }
    
}

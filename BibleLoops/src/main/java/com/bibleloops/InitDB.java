package com.bibleloops;


import java.io.BufferedReader;
//import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

//-----------------------------------------------------------------------------
// for removing annoying mongodb log messages
import java.util.logging.Logger;
import java.util.logging.Level;
//-----------------------------------------------------------------------------

/*
 * Author: Gregory Dott
 * 06-11-2022
 * 
 * This class is for initialising our mongodb database with 2 separate files:
 * kjv.txt - contains the 31 102 verses from the Old and New Testament combined
 * cross_references.txt - contains 344 790 cross references between the Biblical verses.
 * 
 * First, we load the data from kjv.txt. We create a db table for each book in the Bible (66 books = 66 tables). One record for each verse.
 * Then we load the cross_references.txt file and we store the references for each verse on that verse's record in the 'adj' section. 
 * 
 * Once the db has been loaded with the data, we don't need this anymore. Unless we have to reload for some reason.
 * So this logic should only be needed once to set everything up.
 * 
 * After that, we have the data in a nice form that allows us to construct and explore graphs made from the Bible verses.
 * 
 * ===================================================================================================================================
 * With our two files: kjv.txt & cross_references.txt we have an issue.
 * They do not use the same way of referring to passages. Initially I was going to rewrite one of the files, but that was 
 * before I decided to eventually store everything in a db. So now that it's all going in a db, we just need a mapping.
 * 
 * Eg. In cross_references.txt we have "Gen.1.1" and in kjv.txt we have "Ge 1:1"
 * Mapping the difference in numbers is trivial. They yucky part is mapping the book names. The differences
 * between the two do not have a consistent pattern, so there is no quick rule to use there. So I have just had to write each
 * mapping out manually. This is stored in the map1 Hashtable
 * ===================================================================================================================================
 * 
 */

public class InitDB {
    private static Hashtable<String, String> map1; // maps from cross_references.txt to kjv.txt
    //private static Hashtable<String, String> map2; // maps from kjv.txt to cross_references.txt
    
    // public static void main(String[] args) throws IOException {
    //     readFilesAndInitDB();
    // }

    public static void readFilesAndInitDB() throws IOException {
        initMap1(); // maps book names from cross_references.txt to kjv.txt. we name our db tables according to kjv.txt

        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------
        Pr.x("Starting import...");
        createTablesFromVerses(); // read kjv.txt and create database tables. 1 table per book. 1 record per verse.
        addReferencesToVerses(); // read cross_references.txt and save the refs for each verse on its record in the db
        Pr.x("DONE!");
    }

    /**
     * createTablesFromVerses - read the kjv.txt file and create a table for each book, with a record for each verse
     * Each verse in the Bible is stored as a line in kjv.txt
     * @throws IOException
     */
    private static void createTablesFromVerses() throws IOException  {
        // File directory = new File(""); // this is here to help you find where your ide is looking for files from in case you need it... :)
        // System.out.println(directory.getAbsolutePath());
        
        // depending on your IDE setup you may need to adjust the path below slightly. This assumes your setup is looking from the bibleloops folder as the root
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/kjv.txt"))) {
            String line = br.readLine();

            while (line != null) {
                String text = line.toString();
                insertVerse(text); // insert the verse into the db
                line = br.readLine();
            }
        }
    }

    /**
     * addReferencesToVerses - read cross_references.txt file and store each reference with its weight (votes) on each verse
     * @throws IOException
     */
    private static void addReferencesToVerses() throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/cross_references.txt"))) {
            
            String line = br.readLine();
            
            while (line != null) {
                String text = line.toString();
                addReference(text); // find the verse and add the reference in the db
                line = br.readLine();
            }
        }
    }

    /**
     * insertVerse - insert verse into relevant db table. 
     * @param verse
     */
    private static void insertVerse(String verse) {        
        //--------------------------------------------------------------------------------------------------------------------
        // Process verse string. Get book, chapter and verse number. Also get the text of the verse by itself.
        // eg. " Ge 1:1 In the beginning God created the heaven and the earth."
        String[] bits1 = verse.trim().split(" "); // trim first because all lines in kjv.txt have a leading space
        String[] bits2 = bits1[1].split(":");

        String book = bits1[0];
        String chapterNum = bits2[0];
        String verseNum = bits2[1];

        String verseText = "";
        for (int i = 2; i < bits1.length; i++) {
            verseText = verseText + bits1[i] + " ";
        }
        verseText = verseText.trim(); // remove last space we added...
        //--------------------------------------------------------------------------------------------------------------------

        //--------------------------------------------------------------------------------------------------------------------
        // Put the verse into the correct table in the DB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("bibleloops");
            MongoCollection<Document> bCol = db.getCollection(book);

            Document verseDocument = new Document("_id", new ObjectId());
            verseDocument.append("chapter", chapterNum);
            verseDocument.append("verse", verseNum);
            verseDocument.append("text", verseText);

            bCol.insertOne(verseDocument);
        }
        //--------------------------------------------------------------------------------------------------------------------
    }

    /**
     * addReference - given a line from cross_references.txt, process it as necessary and store the reference on the relevant verse.
     * @param reference reference string line from cross_references.txt file
     */
    private static void addReference(String reference) {
        // Gen.1.1	John.1.1-John.1.3	217
        // Rev.22.18	Eph.4.17	3
        //-----------------------------------------------------------------------------------------------------------------------------
        // Process reference string. Map book name from cross_references.txt book name to what is used in kjv.txt using map1 Hashtable
        String[] bits = reference.split("\\s+"); // split by all possible whitespace
        
        String source = bits[0];
        String dest = bits[1];
        String weight = bits[2];
        
        String[] sourceBits = source.split("\\.");
        
        String sourceBook = map1.get(sourceBits[0]); // get book name and map it to the one we are using eg. Gen->Ge
        String sourceChapter = sourceBits[1];
        String sourceVerse = sourceBits[2];
        String destString = "";

        // for now, just mapping the book name properly for the edge
        if (dest.contains("-")) { // we have a from and a to verse
            String[] destBits = dest.split("-");
            String[] destBitsStart = destBits[0].split("\\.");
            String[] destBitsEnd = destBits[1].split("\\.");

            String destBookStart = map1.get(destBitsStart[0]);
            String destChapterStart = destBitsStart[1];
            String destVerseStart = destBitsStart[2];

            String destBookEnd = map1.get(destBitsEnd[0]);
            String destChapterEnd = destBitsEnd[1];
            String destVerseEnd = destBitsEnd[2];

            destString = destBookStart + "." + destChapterStart + "." + destVerseStart;
            destString = destString + "-" + destBookEnd + "." + destChapterEnd + "." + destVerseEnd;
        } else { // just a single verse reference
            String[] destBits = dest.split("\\.");
            String destBook = map1.get(destBits[0]);
            String destChapter = destBits[1];
            String destVerse = destBits[2];

            destString = destBook + "." + destChapter + "." + destVerse;
        }
        //-----------------------------------------------------------------------------------------------------------------------------
        
        //-----------------------------------------------------------------------------------------------------------------------------
        // Find the source verse and record the destination verse and its weight in the adj section of the source verse
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            MongoDatabase db = mongoClient.getDatabase("bibleloops");
            MongoCollection<Document> bCol = db.getCollection(sourceBook);

            Map<String, String> selector = new HashMap<String, String>();
            selector.put("chapter", sourceChapter);
            selector.put("verse", sourceVerse);

            Bson filter1 = eq("chapter", sourceChapter);
            Bson filter2 = eq("verse", sourceVerse);
            
            Document adjDoc = new Document();
            adjDoc.append("dest", destString);
            adjDoc.append("weight", weight);

            Bson update = addToSet("adj", adjDoc);           
            Bson updates = combine(update);

            bCol.findOneAndUpdate(and(filter1, filter2), updates);
        }
        //-----------------------------------------------------------------------------------------------------------------------------
    }

    

    

    // reverse mapping of map1 (should we ever need it...) probably won't...
   /* private static void initMap2() {
        map2 = new Hashtable<String, String>();
        Set<String> keySet = map1.keySet();
        for (String key: keySet) {
            map2.put(map1.get(key), key);
        }
    } */

    /**
     * initMap1 - initialise map1 Hashtable to map book names from cross_references.txt to kjv.txt
     * First string is the book name in the cross_references.txt file
     * Second string is the book name in the kjv.txt file
     */
    private static void initMap1() {
        map1 = new Hashtable<String, String>();
        map1.put("Gen", "Ge");
        map1.put("Exod", "Ex");
        map1.put("Lev", "Le");
        map1.put("Num", "Nu");
        map1.put("Deut", "De");
        map1.put("Josh", "Jos");
        map1.put("Judg", "Jg");
        map1.put("Ruth", "Ru");
        map1.put("1Sam", "1Sa");
        map1.put("2Sam", "2Sa");
        map1.put("1Kgs", "1Ki");
        map1.put("2Kgs", "2Ki");
        map1.put("1Chr", "1Ch");
        map1.put("2Chr", "2Ch");
        map1.put("Ezra", "Ezr");
        map1.put("Neh", "Ne");
        map1.put("Esth", "Es");
        map1.put("Job", "Job");
        map1.put("Ps", "Ps");
        map1.put("Prov", "Pr");
        map1.put("Eccl", "Ec");
        map1.put("Song", "So");
        map1.put("Isa", "Isa");
        map1.put("Jer", "Jer");
        map1.put("Lam", "La");
        map1.put("Ezek", "Eze");
        map1.put("Dan", "Da");
        map1.put("Hos", "Ho");
        map1.put("Joel", "Joe");
        map1.put("Amos", "Am");
        map1.put("Obad", "Ob");
        map1.put("Jonah", "Jon");
        map1.put("Mic", "Mic");
        map1.put("Nah", "Na");
        map1.put("Hab", "Hab");
        map1.put("Zeph", "Zep");
        map1.put("Hag", "Hag");
        map1.put("Zech", "Zec");
        map1.put("Mal", "Mal");
        map1.put("Matt", "Mt");
        map1.put("Mark", "Mr");
        map1.put("Luke", "Lu");
        map1.put("John", "Joh");
        map1.put("Acts", "Ac");
        map1.put("Rom", "Ro");
        map1.put("1Cor", "1Co");
        map1.put("2Cor", "2Co");
        map1.put("Gal", "Ga");
        map1.put("Eph", "Eph");
        map1.put("Phil", "Php");
        map1.put("Col", "Col");
        map1.put("1Thess", "1Th");
        map1.put("2Thess", "2Th");
        map1.put("1Tim", "1Ti");
        map1.put("2Tim", "2Ti");
        map1.put("Titus", "Tit");
        map1.put("Phlm", "Phm");
        map1.put("Heb", "Heb");
        map1.put("Jas", "Jas");
        map1.put("1Pet", "1Pe");
        map1.put("2Pet", "2Pe");
        map1.put("1John", "1Jo");
        map1.put("2John", "2Jo");
        map1.put("3John", "3Jo");
        map1.put("Jude", "Jude");
        map1.put("Rev", "Re");
    }
}

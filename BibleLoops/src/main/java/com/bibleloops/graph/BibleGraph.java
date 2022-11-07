package com.bibleloops.graph;

import com.bibleloops.Pr;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * At some point we will want to explore creating nodes on the fly, based on the verse we are looking for.
 * It won't be reasonable to create the entire graph every time I believe. We can probably traverse the 
 * graph while creating it and while unused bits fall away...
 * 
 * We have 2 different references to the verses in each file. Either I need to standardise these for ease of use, or create a hashtable.
 * Standardising them makes more sense...
 * 
 * There is also the thought of different types of nodes...
 * eg. each verse is just its own node, OR we have a mega node for each book. To get a a verse we go there through a mega node...
 */

public class BibleGraph {
    static Hashtable<String, BibleNode> bibleNodes = new Hashtable<String, BibleNode>();
    public static void main(String args[]) throws IOException {
        
        try(BufferedReader br = new BufferedReader(new FileReader("public/BibleLoops/kjv.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
        
            int count = 0;
            while (line != null && count < 100) {
                count++;

                String text = line.toString();
                String[] elements = text.split(" ");
                //Pr.x(Arrays.toString(elements));
                String verse = elements[1] + elements[2];
                BibleNode newNode = new BibleNode(text, 0);
                bibleNodes.put(verse, newNode);

                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            //Pr.x(everything);
           
        }
        //Pr.x(bibleNodes.keySet().toString());
        BibleNode t = bibleNodes.get("Ge1:14");
        Pr.x(t.getText());

       
    }
    
}

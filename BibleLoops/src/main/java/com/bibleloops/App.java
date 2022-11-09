package com.bibleloops;

import java.io.IOException;


import com.bibleloops.graph.BibleGraph;
import com.bibleloops.vis.*;
import javax.swing.JFrame;

//-----------------------------------------------------------------------------
// for removing annoying mongodb log messages
import java.util.logging.Logger;
import java.util.logging.Level;
//-----------------------------------------------------------------------------

/*
 * Author: Gregory Dott
 * 06-11-2022
 * 
 * This is the start of the Maven project for Bible Loops.
 */


public final class App {

    private App() {
    }

    public static void main(String[] args) throws IOException{
        
        //*****************************************************************************************************************************************************
        //InitDB.readFilesAndInitDB(); // for initialising the database - once done, we don't need to do this again unless data gets corrupted or changed
        //*****************************************************************************************************************************************************
        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages (and makes annoying code that has little purpose other than that appear. dubious)
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------


        //BibleGraph bg = new BibleGraph("Ge", 1, 1, 10, "cbfs");
        
        // GraphVis m=new GraphVis(29);  
        // JFrame f=new JFrame();  
        // f.add(m);  
        // f.setSize(1600,900);
        // //f.setLayout(null);  
        // f.setVisible(true);
        

    }

    
}





  
 
package com.bibleloops;

import java.io.IOException;


import javax.swing.*;

import com.bibleloops.graph.BibleGraph;

import java.awt.*;  

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
        //-----------------------------------------------------------------------------
        // removes annoying mongo log messages
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE); // e.g. or Log.WARNING, etc.
        //-----------------------------------------------------------------------------


        //InitDB.readFilesAndInitDB(); // for initialising the database - once done, we don't need to do this again unless data gets corrupted or changed
        BibleGraph bg = new BibleGraph("Ge", 1, 1, 10, "cbfs");
        

        

    }

    
}

/* for when I start on the interface

DisplayGraphics m=new DisplayGraphics();  
        JFrame f=new JFrame();  
        f.add(m);  
        f.setSize(800,600);
        //f.setLayout(null);  
        f.setVisible(true);

class DisplayGraphics extends Canvas{  
      
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawString("Hello",40,40);
        setBackground(Color.WHITE);
        g2.fillRect(130, 30,100, 80);
        
        g2.drawOval(30,130,50, 60);
        g2.drawOval(300, 300, 90, 90);
        g2.fillOval(300, 300, 90, 90);
        
        g2.fillOval(130,130,50, 60);
        g2.drawArc(30, 200, 40,50,90,60);
        setForeground(Color.BLUE);
        //g.fillArc(30, 130, 40,50,180,40);  
          
    }  

  
}  
 */
package com.bibleloops.vis;

import com.bibleloops.Pr;
import com.bibleloops.graph.BibleNode;
import com.bibleloops.graph.BibleGraph;

import javax.swing.*;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.*;


import java.awt.Point;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseMotionAdapter;  

/*
 * Author: Gregory Dott
 * 09-11-2022
 * 
 * This will initially be for visualising Bible graphs. I want to make it more useful than that and adapt it for
 * visualing all sorts of graph data. In the current case we could just construct the visuals from the BibleNode
 * data, but that will restrict it only to this usage.
 * 
 * However we eventually extract a lib from this, it will need to use the linked list type setup of a graph 
 * anyway... So maybe at that point, we have an intermediary graph structure that we can map graphs to. This
 * should be simple.
 * 
 * So... for now, we are just going to use the graph directly. We can construct everything using a BFS approach.
 * Whatever approach we use (bfs or dfs) really just affects the order in which nodes are created. We will always
 * create the same nodes regardless...
 * 
 */

class DisplayNode {
    Ellipse2D.Float shape;
    BibleNode bn;

    public DisplayNode(Ellipse2D.Float shape, BibleNode bn) {
        this.shape = shape;
        this.bn = bn;
    }
}

public class GraphVis extends JPanel {
    List<Color> nodeColours;
    // Here we want an object that contains the shape data along with the node data...
    //List<Ellipse2D.Float> nodes = new ArrayList<Ellipse2D.Float>();
    List<DisplayNode> displayNodes = new ArrayList<DisplayNode>();

    MovementAdapter ma = new MovementAdapter();

    public static void main(String args[]) {
        BibleGraph bg = new BibleGraph("Ge", 1, 1, 10, "cbfs"); // init Bible Graph

        JFrame frame = new JFrame("TESTING!");
        GraphVis m = new GraphVis(bg);
        m.setDoubleBuffered(true);
        frame.add(m);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public GraphVis(BibleGraph bg) {
        initNodeColours();
        addMouseMotionListener(ma);
        addMouseListener(ma);

        BibleNode root = bg.getRoot();
        traverseFromRootBFS(root); // traverse our graph and draw the shapes
    }

    void traverseFromRootBFS(BibleNode root) {
        List<BibleNode> nodesToVisit = new ArrayList<BibleNode>();
        nodesToVisit.add(root);
        int count = 0;

        while(!nodesToVisit.isEmpty()) {
            BibleNode cNode = nodesToVisit.get(0);
            int[] coords = getNextCoodrinates(1600, 900, count, 100); 
            Ellipse2D.Float shape = new Ellipse2D.Float(coords[0], coords[1], 100, 100);

            DisplayNode dn = new DisplayNode(shape, cNode);
            displayNodes.add(dn);
            count++;

            List<BibleNode> neighbours = cNode.getNeighbours();
            for (int i = 0; i < neighbours.size(); i++) {
                BibleNode cNeigh = neighbours.get(i);
                nodesToVisit.add(cNeigh);
            }
            nodesToVisit.remove(0);
        }
    }

    // For now just a basic thing to get the xy coords of the next graph node. Very simple and sequential
    // will do something more elaborate at a later stage
    private int[] getNextCoodrinates(int frameWidth, int frameHeight, int i, int width) {
        int[] coords = new int[2];
        int numPerLine = frameWidth/(width + 5);
        int line = i/numPerLine;
        coords[0] = (i%numPerLine)*(width + 5);
        coords[1] = line*(width + 5);
        return coords;
    }

    public void paint(Graphics g) {  
        
        super.paint(g);
        for (int i = 0; i < displayNodes.size(); i++) {
            Ellipse2D.Float shape = displayNodes.get(i).shape;
            BibleNode bn = displayNodes.get(i).bn;
            Graphics2D circle = (Graphics2D) g;
            circle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            circle.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            circle.setColor(nodeColours.get(i));
            circle.fill(shape);
            circle.setColor(Color.RED);
            circle.drawString(bn.getVerseId(), shape.x + 30, shape.y + 50);
        }
        
    }

    private void initNodeColours() {
        nodeColours = new ArrayList<Color>();
        for (int r=0; r<100; r = r + 10) nodeColours.add(new Color(r*255/100,       255,         0));
        for (int g=100; g>0; g = g - 10) nodeColours.add(new Color(      255, g*255/100,         0));
        for (int b=0; b<100; b = b + 10) nodeColours.add(new Color(      255,         0, b*255/100));
        for (int r=100; r>0; r = r - 10) nodeColours.add(new Color(r*255/100,         0,       255));
        for (int g=0; g<100; g = g + 10) nodeColours.add(new Color(        0, g*255/100,       255));
        for (int b=100; b>0; b = b - 10) nodeColours.add(new Color(        0,       255, b*255/100));
        
        nodeColours.remove(0); // just because it looks identical to the next one in my setup. Not sure why...
    }
    // private void initNodeColours() {
    //     nodeColours = new ArrayList<String>();
    //     nodeColours.add("#5DADE2");
    //     nodeColours.add("#76D7C4");
    //     nodeColours.add("#73C6B6");
    //     nodeColours.add("#BB8FCE");
    //     nodeColours.add("#C39BD3");
    //     nodeColours.add("#E74C3C");
    //     nodeColours.add("#C0392B");
    //     nodeColours.add("#DC7633");
    //     nodeColours.add("#E67E22");
    // }

    class MovementAdapter extends MouseAdapter {

        private int x;
        private int y;
        private int dragging = -1;

        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
        }

        public void mouseReleased(MouseEvent e) {
            dragging = -1;
        }

        public void mouseDragged(MouseEvent e) {

            int dx = e.getX() - x;
            int dy = e.getY() - y;
            
            for (int i = 0; i < displayNodes.size(); i++) {
                Ellipse2D.Float shape = displayNodes.get(i).shape;
                
                if (shape.getBounds2D().contains(x, y) && (dragging == i || dragging == -1)) {
                    dragging = i;
                    shape.x += dx;
                    shape.y += dy;
                    repaint();
                }
            }

            x += dx;
            y += dy;
        }
    }
}

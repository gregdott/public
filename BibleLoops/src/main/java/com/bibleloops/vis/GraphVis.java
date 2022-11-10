package com.bibleloops.vis;

import com.bibleloops.Pr;
import com.bibleloops.graph.BibleNode;
import com.bibleloops.graph.BibleGraph;

import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.BasicStroke;
import java.util.*;

import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;

/*
 * Author: Gregory Dott
 * 09-11-2022
 * 
 * This will initially be for visualising Bible graphs. I want to make it more useful than that and adapt it for
 * visualing all sorts of graph data. In the current case it has been constructed for visualising BibleNode
 * data. I want to create a more generalised type of node that we can construct from various different
 * datasets. Then we can use these nodes across different applications. That is a TODO. 2 things will become
 * external: nodes & graph visualisation stuff. Those two could actually be packed together even.
 * 
 * So... for now, we are just going to use the graph directly. We can construct everything using a BFS approach.
 * Whatever approach we use (bfs or dfs) really just affects the order in which nodes are created. We will always
 * create the same nodes regardless...
 * 
 */

/*
 * Simple class to contain node data along with the part used for displaying
 * the node and its edges
 */


class DisplayNode {
    //Ellipse2D.Float shape;
    Rectangle shape;
    BibleNode bn;

    public DisplayNode(Rectangle shape, BibleNode bn) {
        this.shape = shape;
        this.bn = bn;
    }
}

/**
 * Simple class to contain edge data. Literally just the Display node objects for the source and dest 
 * of the edge
 */
class DisplayEdge {
    DisplayNode source;
    DisplayNode dest;

    public DisplayEdge(DisplayNode source, DisplayNode dest) {
        this.source = source;
        this.dest = dest;
    }
}

public class GraphVis extends JPanel {
    int nodeSize = 60; // the width and height of each node
    List<Color> nodeColours; // List of colours for the nodes
    List<DisplayNode> displayNodes = new ArrayList<DisplayNode>();
    List<DisplayEdge> edges = new ArrayList<DisplayEdge>();
    Hashtable<String, DisplayNode> nodeMap = new Hashtable<String, DisplayNode>(); // used to find a DisplayNode object given the verseId ("Ge.1.1" etc.)

    MovementAdapter ma = new MovementAdapter();

    public static void main(String args[]) {

        //Pr.x(BookMap.getBook("Re"));

        BibleGraph bg = new BibleGraph("Ge", 1, 1, 40, "cbfs"); // init Bible Graph

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
        createEdges();
    }

    /**
     * Gets called in the beginning and then each time repaint() is called, so when relevant mouse events
     * happen. For now it just goes through our list of edges, paints them and then goes through the nodes
     * and paints them.
     */
    public void paint(Graphics g) {  
        
        super.paint(g);

        // Paint edges first so they go behind
        for (int i = 0; i < edges.size(); i++) {
            DisplayEdge ce = edges.get(i);
            
            // need to update coords of line in case nodes have moved:
            DisplayNode source = ce.source;
            DisplayNode dest = ce.dest;
            int sx = Math.round(source.shape.x + source.shape.width/2);
            int sy = Math.round(source.shape.y + source.shape.height/2);
            int dx = Math.round(dest.shape.x + dest.shape.width/2);
            int dy = Math.round(dest.shape.y + dest.shape.height/2);
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawLine(sx, sy, dx, dy);
        }

        // Then paint the nodes
        for (int i = 0; i < displayNodes.size(); i++) {
            //Ellipse2D.Float shape = displayNodes.get(i).shape;
            Rectangle shape = displayNodes.get(i).shape;
            BibleNode bn = displayNodes.get(i).bn;
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(nodeColours.get(i));            
            g2.fillRoundRect(shape.x, shape.y, shape.width, shape.height, 10, 10);

            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.DARK_GRAY);
            g2.drawRoundRect(shape.x, shape.y, shape.width, shape.height, 10, 10);
            g2.setColor(Color.BLACK);
            g2.drawString(bn.getVerseId(), shape.x + 5, shape.y + 15);
        }
    }
    
    /**
     * traverseFromRootBFS - starting at the root, visit all of its neighbours, create the necessary DisplayNode
     * objects for each node found.
     * 
     * @param root BibleNode object of the first node in the graph. All other nodes can be found through this
     */
    void traverseFromRootBFS(BibleNode root) {
        List<BibleNode> nodesToVisit = new ArrayList<BibleNode>();
        nodesToVisit.add(root);
        int count = 0;

        while(!nodesToVisit.isEmpty()) {
            BibleNode cNode = nodesToVisit.get(0);
            int[] coords = getNextCoodrinates(1600, 900, count, nodeSize); 
            //Ellipse2D.Float shape = new Ellipse2D.Float(coords[0], coords[1], nodeSize, nodeSize);
            Rectangle shape = new Rectangle(coords[0], coords[1], nodeSize, nodeSize);
            
            DisplayNode dn = new DisplayNode(shape, cNode);
            displayNodes.add(dn);
            nodeMap.put(cNode.getVerseId(), dn); // associate DisplayNode with verseId so that when we create edges we can find the correct graphics object node location
            count++;

            List<BibleNode> neighbours = cNode.getNeighbours();
            for (int i = 0; i < neighbours.size(); i++) {
                //Line2D.Float edge = new Line2D.Float() // can't do this here because we don't have destination yet... wait we can...
                BibleNode cNeigh = neighbours.get(i);
                nodesToVisit.add(cNeigh);
            }
            nodesToVisit.remove(0);
        }
    }

    /**
     * Go through the list of nodes and create a DisplayEdge object for each edge.
     * DisplayEdge contains source and dest DisplayNode objects. We use the coordinates
     * from them to draw the line for the edge.
     */
    void createEdges() {
        for(DisplayNode dn: displayNodes) {
            BibleNode bn = dn.bn;
            List<BibleNode> neighbours = bn.getNeighbours();
            for (BibleNode neigh: neighbours) {
                DisplayNode ndn = nodeMap.get(neigh.getVerseId()); // get the DisplayNode object for the neighbour so we know where to send the line to.
                DisplayEdge newEdge = new DisplayEdge(dn, ndn);
                edges.add(newEdge);
            }
        }
    }

    

    // For now just a basic thing to get the xy coords of the next graph node. Very simple and sequential
    // will do something more elaborate at a later stage
    private int[] getNextCoodrinates(int frameWidth, int frameHeight, int i, int width) {
        int[] coords = new int[2];
        int numPerLine = frameWidth/(width + 5);
        int line = i/numPerLine;
        coords[0] = (i%numPerLine)*(width + 5) + 5;
        coords[1] = line*(width + 5) + 5;
        return coords;
    }

    // temporary...
    private void initNodeColours() {
        nodeColours = new ArrayList<Color>();
        for (int r=0; r<100; r = r + 5) nodeColours.add(new Color(r*255/100,       255,         0));
        for (int g=100; g>0; g = g - 5) nodeColours.add(new Color(      255, g*255/100,         0));
        for (int b=0; b<100; b = b + 5) nodeColours.add(new Color(      255,         0, b*255/100));
        for (int r=100; r>0; r = r - 5) nodeColours.add(new Color(r*255/100,         0,       255));
        for (int g=0; g<100; g = g + 5) nodeColours.add(new Color(        0, g*255/100,       255));
        for (int b=100; b>0; b = b - 5) nodeColours.add(new Color(        0,       255, b*255/100));
        
        nodeColours.remove(0); // just because it looks identical to the next one in my setup. Not sure why...
    }
    
    // I prefer using actual hex values of colours like below. They are more pleasing to the eye. Need to compile a BIG list of pleasing hex colours
    /*private void initNodeColours() {
        nodeColours = new ArrayList<String>();
        nodeColours.add("#5DADE2");
        nodeColours.add("#76D7C4");
        nodeColours.add("#73C6B6");
        nodeColours.add("#BB8FCE");
        nodeColours.add("#C39BD3");
        nodeColours.add("#E74C3C");
        nodeColours.add("#C0392B");
        nodeColours.add("#DC7633");
        nodeColours.add("#E67E22");
    }*/

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
                //Ellipse2D.Float shape = displayNodes.get(i).shape;
                Rectangle shape = displayNodes.get(i).shape;
                
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

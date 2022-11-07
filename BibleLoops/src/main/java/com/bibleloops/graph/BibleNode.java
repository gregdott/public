package com.bibleloops.graph;

import java.util.*;


/*
 * Author: Gregory Dott
 * 05-11-2022
 */

public class BibleNode {
    private String text; // the text of the verse
    private int weight; // not sure how weights of nodes will be determined, but I believe something like this will be required
    private List<BibleNode> neighbours;

    public BibleNode(String text, int weight) {
        this.text = text;
        this.weight = weight;
    }

    public String getText() {
        return this.text;
    }

    public int getWeight() {
        return this.weight;
    }

}
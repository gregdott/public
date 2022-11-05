package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================
 * In computer science and information theory, a Huffman code is a particular type of optimal prefix code that is 
 * commonly used for lossless data compression. The process of finding or using such a code proceeds by means of 
 * Huffman coding, an algorithm developed by David A. Huffman while he was a Sc.D. student at MIT, and published in the 
 * 1952 paper "A Method for the Construction of Minimum-Redundancy Codes".
 * ======================================================================================================================
 * 
 * After spending a while trying to understand how to build and use a Huffman Tree on Wikipedia and elsewhere,
 * and getting somewhat confused by a lot of text, I found a video by Tom Scott that explains pretty much all you
 * need to know in order to understand the basics of Huffman Coding and the implementation below. Here is the
 * video: https://www.youtube.com/watch?v=JsTptu56GM8
 * 
 * Thanks Tom...
 * 
 * To be honest, Tom's video was more useful in understanding Huffman Encoding than anything else. This implementation
 * was based pretty much entirely on what I learnt from his video.
 * --------------------------------------------------------------------------------------------------------------------------
 * The current implementation creates a tree as a linked list. Because it is a binary tree however, we could store it in an array.
 * Perhaps I will alter the implementation to work that way at some point...
 */

public class HuffmanCodingCompression {
    private static HuffmanNode treeRoot; // once the Huffman Tree has been constructed this will be the root
    private static Hashtable<Character, HuffmanNode> leaves; // maps characters onto Huffman Nodes - this is for encoding so we can find the leaf to go up the tree from

    public static void main(String args[]) {
        String s = "The technique works by creating a binary tree of nodes. These can be stored in a regular array, the size of which depends on the number of symbols.";
        buildHuffmanTree(s);

        Pr.x("------------------------------------------------------------------");
        Pr.x("Original string:");
        Pr.x(s);
        Pr.x("------------------------------------------------------------------");
        
        String encoded = encodeString(s, leaves);

        Pr.x("------------------------------------------------------------------");
        Pr.x("Huffman encoded string:");
        Pr.x(encoded);
        Pr.x("Huffman encoded length: " + encoded.length());
        Pr.x("------------------------------------------------------------------");
        
        String ascii = getAsciiEncoding(s);

        Pr.x("------------------------------------------------------------------");
        Pr.x("ASCII encoded string:");
        Pr.x(ascii);
        Pr.x("ASCII encoded length: " + ascii.length());
        Pr.x("------------------------------------------------------------------");
        
        String decoded = decodeString(encoded, treeRoot);

        Pr.x("------------------------------------------------------------------");
        Pr.x("Decoded string:");
        Pr.x(decoded);
        Pr.x("------------------------------------------------------------------");
    }

    /**
     * buildHuffmanTree - create a Huffman Tree for a given string
     * @param s the string to create the Huffman Tree from
     */
    private static void buildHuffmanTree(String s) {
        Hashtable<Character, Integer> freq = getCharFrequency(s); // calculate the frequency of each character in the given string
        List<Character> chars = getSortedChars(freq); // sort the characters in ascending order, based on their frequency

        // Now with list of sorted chars, we can build the Huffman tree
        List<HuffmanNode> nodes = new ArrayList<HuffmanNode>();
        leaves = new Hashtable<Character, HuffmanNode>();

        for (Character ch: chars) { // create a leaf note for each character in the string
            HuffmanNode newNode = new HuffmanNode(ch.toString(), freq.get(ch));
            nodes.add(newNode);
            leaves.put(ch, newNode);
        }
        
        // Our List is essentially a queue, that we will use to build the Huffman tree.
        // We proceed by popping items off the front of the tree
        List<HuffmanNode> set = new ArrayList<HuffmanNode>(); // array representing current set

        while (!nodes.isEmpty()) {
            
            if (!nodes.isEmpty()) {
                set.add(nodes.get(0));
                nodes.remove(0);
            }
            
            if (set.size() == 2 ) {
                HuffmanNode leftChild = set.get(0);
                HuffmanNode rightChild = set.get(1);

                leftChild.bit = "0"; // maybe shouldn't use string here, but doing this for coding quickly for now. Even though I had to comment now. hmmmm
                rightChild.bit = "1";

                HuffmanNode newNode = new HuffmanNode(leftChild.str + rightChild.str, leftChild.weight + rightChild.weight, leftChild, rightChild);

                nodes.add(newNode);
                sortNodes(nodes); // new node on the end needs to be put in the right position so that it is processed in order of its weight

                leftChild.parent = newNode;
                rightChild.parent = newNode;
                // once done creating new node, destroy current set
                set = new ArrayList<HuffmanNode>(); // is it better to create new, or to remove each object??
            }
        }

        // onces nodes is empty from while loop above, there should be only 1 element in our set array and that will be the root of our tree
        treeRoot = set.get(0);
    }
    
    /**
     * sortNodes - This assumes that only the most recently added node is out of place. When we begin, we start with a list
     * of HuffmanNode objects that represent leaves and are ordered according to their associated char frequency.
     * When adding more nodes, it is simple to keep the list in order, because the only one that will be out of order, 
     * will be the most recently added node. So we just ensure that it sits at the correct place in the list as we
     * know everything else is already in order.
     * 
     * This helps us maintain a primitive priority queue. We could have used a class built for that...
     * 
     * @param nodes List of HuffmanNode objects
     */
    private static void sortNodes(List<HuffmanNode> nodes) {
        int index = nodes.size() - 1;
        for (int i = nodes.size() - 2; i >= 0; i--) {
            // if our new node's weight is less than the one before it, swap them.
            if (nodes.get(index).weight < nodes.get(i).weight) { 
                HuffmanNode tmp = nodes.get(index);
                nodes.set(index, nodes.get(i));
                nodes.set(i, tmp);
                index = i;
            }
        }
    }

    /**
     * decodeString - decode a binary string using the Huffman Tree.
     * @param es encoded string (presumably encoded using Huffman Tree)
     * @param root the root of the Huffman Tree
     * @return decoded string
     */
    private static String decodeString(String es, HuffmanNode root) {
        char[] encChars = es.toCharArray();
        String decoded = "";

        HuffmanNode searcher = root;

        for(char bit: encChars) {
            if (bit == '0') {
                searcher = searcher.leftChild;
            } else { // bit == '1'
                searcher = searcher.rightChild;
            }

            if (searcher.leftChild == null) { // at the leaf
                decoded = decoded + searcher.str;
                searcher = root;
            }
        }

        return decoded;
    }

    
    /**
     * encodeString - encode string using Huffman Tree
     * @param s the string to encode. This will be the same string that we created the Huffman tree from.
     * @param leaves Hashtable mapping characters onto Huffman Nodes.
     * @return binary representation of entire string constructed from the Huffman Tree we constructed
     */
    private static String encodeString(String s, Hashtable<Character, HuffmanNode> leaves) {
        String encoded = "";
        char[] chars = s.toCharArray();

        for (char ch: chars) {
            encoded = encoded + generateCode(ch, leaves);
        }

        return encoded;
    }

    /**
     * generateCode - generate the Huffman Code for the given character based on the Huffman Tree we have constructed
     * @param ch the character
     * @param leaves Hashtable mapping characters onto Huffman Nodes. This is so we can start traversing up the tree from the correct place
     * @return binary string for representing the character based on the Huffman Tree
     */
    private static String generateCode(char ch, Hashtable<Character, HuffmanNode> leaves) {
        String code = "";
        HuffmanNode node = leaves.get(ch);
        
        while (node.parent != null) {
            code = node.bit + code; // need to reverse as we get the bits because we are going up the tree
            node = node.parent;
        }

        return code;
    }

    /**
     * getSortedChars- uses the Quicksort algorithm implemented in sortChars
     * @param freq Hashtable containing characters from string mapped onto their frequency in the string
     * @return array of chars sorted in order of their frequency in the string
     */
    private static List<Character> getSortedChars(Hashtable<Character, Integer> freq) {
        List<Character> chars = new ArrayList<Character>();
        chars.addAll(freq.keySet());
        return sortChars(chars, freq);
    }

    /**
     * sortChars - implementation of the Quicksort algorithm for sorting an array of characters based on the value
     * associated with them in the freq Hashtable
     * @param chars List of characters
     * @param freq Hashtable mapping chararcters to their frequency in the given string
     * @return List of chars sorted according to their frequency value from freq Hashtable
     */
    private static List<Character> sortChars(List<Character> chars, Hashtable<Character, Integer> freq) {
        if (chars.size() <= 1) {
            return chars;
        } else {
            char pivot = chars.get(chars.size() - 1); // get last element of array
            
            List<Character> beforePivot = new ArrayList<Character>();
            List<Character> afterPivot = new ArrayList<Character>();

            // Loop through unsorted elements and place them in either beforePivot or afterPivot based on their value relative to pivot
            for (int i = 0; i < chars.size() - 1; i++) {
                if (freq.get(chars.get(i)) <= freq.get(pivot)) { // compare the corresponding frequency of the chars from our freq Hashtable
                    beforePivot.add(chars.get(i));
                } else {
                    afterPivot.add(chars.get(i));
                }
            }
            
            List<Character> sortedList = sortChars(beforePivot, freq); // recurse on elements before pivot
            sortedList.add(pivot); // add pivot back
            sortedList.addAll(sortChars(afterPivot, freq)); // recurse on elements after pivot
            
            return sortedList;
        }
    }

    /**
     * getCharFrequency - returns a Hashtable mapping characters onto their frequency in the given string
     * 
     * @param s the string
     * @return Hashtable with a frequency value for each character key
     */
    private static Hashtable<Character, Integer> getCharFrequency(String s) {
        char[] chars = s.toCharArray();
        Hashtable<Character, Integer> freq = new Hashtable<Character, Integer>();

        for (char ch: chars) {
            if (freq.containsKey(ch)) {
                freq.put(ch, freq.get(ch) + 1);
            } else {
                freq.put(ch, 1);
            }
        }

        return freq;
    }

    /**
     * getAsciiEncoding - this is used to see how long the standard encoding is compared to the Huffman encoding
     * @param s string for which we want to see the standard 8 bit ascii encoding in binary
     * @return string of binary digits (1's & 0's) representing the 8 bit ascii encoding of s
     */
    private static String getAsciiEncoding(String s) {
        String ascii = "";
        char[] chars = s.toCharArray();

        for (char ch: chars) {
            int ascNum = (int)ch; // get int value of char
            String binary = String.format("%08d", Integer.parseInt(Integer.toBinaryString(ascNum))); // get binary string & ensure 8 bit representation
            ascii = ascii + binary;
        }
        return ascii;
    }
}

class HuffmanNode {
    String str; // for the leaves, this is a single character, but it becomes a string as we start to combine nodes
    int weight; // the weight of the node. for a leaf this will be the frequency with which it occurs
    String bit; // for any child node. 0 if it is the left child of the parent. 1 if it is the right child
    HuffmanNode parent, leftChild, rightChild; // references to relatives. Some might be null (eg. leaves have no children and root has no parent)

    // for constructing leaf nodes
    public HuffmanNode(String str, int weight) { 
        this.str = str;
        this.weight = weight;
    }
    
    // for constructing nodes further up the tree from the leaves
    public HuffmanNode(String str, int weight, HuffmanNode leftChild, HuffmanNode rightChild) { 
        this.str = str;
        this.weight = weight;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void print() {
        Pr.x("Str: " + str);
        Pr.x("Weight: " + weight);
        Pr.x("Bit: " + bit);
    }
}

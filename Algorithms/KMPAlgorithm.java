package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 02-11-2022
 * 
 * From Wikipedia:
 * ===============================================================================================================
 * In computer science, the Knuth–Morris–Pratt string-searching algorithm (or KMP algorithm) searches for 
 * occurrences of a "word" W within a main "text string" S by employing the observation that when a mismatch 
 * occurs, the word itself embodies sufficient information to determine where the next match could begin, thus 
 * bypassing re-examination of previously matched characters.
 * ===============================================================================================================
 * 
 * An additional explanation of the algorithm is found here: http://jakeboxer.com/blog/2009/12/13/the-knuth-morris-pratt-algorithm-in-my-own-words/
 * 
 * ===============================================================================================================
 * 
 * We want to output each index in s where w occurs 
 * 
 * TODO: hi res description of how and why the partial match table works. I believe I can even improve on Jake Boxer's explanation from above
 */

public class KMPAlgorithm {
    public static void main(String args[]) {
        String s = "ABCABAABCABAC";
        String w = "CAB";

        // String s = "ABC ABCDAB ABCDABCDABDE";
        // String w = "ABCDABD";
        kmpSearch(w, s);
    }

    /**
     * kmpSearch - search for a string inside another string using the KMP Algorithm
     * 
     * @param w the word to search for
     * @param s the string in which to search for w
     */
    private static void kmpSearch(String w, String s) {
        int m, i; // m = index in s where prospective match begins. i = index in w as we compare
        i = m = 0;

        //partial_match_length - table[partial_match_length - 1]
        int[] pmTable = generatePartialMatchTable(w);
        
        while (m <= s.length() - w.length()) { // while we have not exceeded the boundaries of searchable string with respect to w
            // Pr.x("m: " + m);
            // Pr.x("i: " + i);
            if (s.charAt(m + i) == w.charAt(i)) {
                if (i + 1 == w.length()) { // we have matched the full word
                    Pr.x("Found match at index: " + m);
                    // increment m to carry on searching and reset i
                    m = m + (i - pmTable[i-1]);
                    i = 0;
                } else {
                    i++;
                }
            } else {
                if (i > 0) {
                    // use partial match table to see how far ahead we can jump
                    m = m + (i - pmTable[i-1]);
                    Pr.x("m: " + m);
                } else {
                    m++;
                }
                
                i = 0;
            }
        }
    }

    /**
     * generatePartialMatchTable - generates a partial match table to help us determine how far we can move along 
     * the string we are searching in after a potential match fails. It's actually a really simple concept that seems 
     * to be explained online in strangely complicated ways. It's a simple concept, but tricky to explain.
     *      
     * To explain very broadly, the partial match table stores data about substrings of the search string that are repeated
     * This helps us to determine how far along the searchable string we can jump when a match fails after n characters.
     * This is a very low res description. Hi res description to come.
     * 
     * @param w the string that we are searching for
     * @return partial match table (array) that reflects how far we can move along the searchable string based on how many characters matched previously
     */
    private static int[] generatePartialMatchTable(String w) {
        char[] chars = w.toCharArray();
        int[] pmTable = new int[w.length()];
        pmTable[0] = 0;
        for (int i = 1; i < chars.length; i++) {
            String[] properPrefixes = getPropers(w, i, true);
            String[] properSuffixes = getPropers(w, i, false);

            // find longest matching prefixes and suffixes
            int longest = 0;
            for (String prefix: properPrefixes) {
                for (String suffix: properSuffixes) {
                    if (prefix.equals(suffix) && prefix.length() > longest) {
                        longest = prefix.length();
                    }
                }
            }
            pmTable[i] = longest;
        }

        return pmTable;
    }

    /**
     * getPropers - get either the proper prefixes or suffixes for a given string
     * Not sure about this naming
     * @param w
     * @param length
     * @param isPrefix
     * @return
     */
    private static String[] getPropers(String w, int length, boolean isPrefix) {
        String[] propers = new String[length];

        if (isPrefix) {
            for (int i = 0; i < length; i++) {
                propers[i] = w.substring(0, i + 1);
            }
        } else {
            for (int i = 0; i < length; i++) {
                propers[i] = w.substring(i+1, length+1);
            }
        }

        return propers;
        
    }
}

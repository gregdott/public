package Algos;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 24-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * In computer science, merge sort (also commonly spelled as mergesort) is an efficient, general-purpose, and comparison-based sorting algorithm. 
 * Most implementations produce a stable sort, which means that the order of equal elements is the same in the input and output. 
 * Merge sort is a divide-and-conquer algorithm that was invented by John von Neumann in 1945.
 * A detailed description and analysis of bottom-up merge sort appeared in a report by Goldstine and von Neumann as early as 1948.
 * 
 * Conceptually, a merge sort works as follows:

 * Divide the unsorted list into n sublists, each containing one element (a list of one element is considered sorted).
 * Repeatedly merge sublists to produce new sorted sublists until there is only one sublist remaining. This will be the sorted list.
 * ======================================================================================================================================
 * 
 * There are 2 implementations given below:
 * 
 * Top Down (recursive)
 * Bottom Up (iterative)
 */

public class MergeSort {
    public static void main(String args[]) {
        List<Integer> unsorted = new ArrayList<Integer>(Arrays.asList(1, 9, 3, 7, 33, 42, 2, 3, 4));
        Pr.x("Unsorted: " + unsorted.toString());
       

        //-----------------------------------------------------------------------
        // Iterative:
        Pr.x("----------------------------------------------------");
        Pr.x("Merge Sort (Iterative - Bottom Up):");
        List<Integer> sortedIterative = mergeSortBottomUp(unsorted);
        Pr.x("SORTED: " + sortedIterative.toString());
        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Recursive:
        Pr.x("----------------------------------------------------");
        Pr.x("Merge Sort (Recursive - Top Down):");
        List<Integer> sortedRecursive = mergeSortTopDown(unsorted);
        Pr.x("Sorted: " + sortedRecursive.toString());
        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------
    }

    public static List<Integer> mergeSortBottomUp(List<Integer> unsorted) {
        List<Integer> sorting = new ArrayList<Integer>(unsorted);
        

        /*
         * To do this bottom up (iteratively), my first thought is to do it like this:
         * Loop through list and compare successive sets of neighbours (0 & 1, 2 & 3 etc.) and reorganise pairs in order.
         * Then on next loop compare sets of sets so [0, 1] with [2, 3] and reorganise. Then [0, 1, 2, 3] with [4, 5, 6, 7]
         * We do this until the last iteration where the size of our first set added to the size of our second set is equal to the length of the list.
         * 
         * 
         */

        int bracketSize = 2;

        while (bracketSize*2 < unsorted.size()) {
            List<Integer> sortStep = new ArrayList<Integer>();
            for (int i = 0; i < unsorted.size()/bracketSize; i = i + 2) {
                int minEnd = (i*bracketSize) + bracketSize*2;

                if (minEnd > unsorted.size()) {
                    minEnd = unsorted.size();
                }

                List<Integer> arr1 = new ArrayList<Integer>(sorting.subList(i*bracketSize, (i*bracketSize) + bracketSize));
                List<Integer> arr2 = new ArrayList<Integer>(sorting.subList((i*bracketSize) + bracketSize, minEnd));
                List<Integer> merged = merge(arr1, arr2);

                sortStep.addAll(merged);
            }
            
            // Well, this works now. Not sure if it is unnecessarily complicated though? It almost certainly does extra work compared to the recursive version.
            // Nevertheless, when doing comments I need to put a good explanation of what is being done here.
            if (sorting.size()%bracketSize > 0) {
                int startNeglected = (sorting.size()/bracketSize)*bracketSize;
                int endNeglected = startNeglected + (sorting.size()%bracketSize);

                sortStep.addAll(sorting.subList(startNeglected, endNeglected));

                List<Integer> arr1 = new ArrayList<Integer>(sortStep.subList(startNeglected - bracketSize, startNeglected));
                List<Integer> arr2 = new ArrayList<Integer>(sortStep.subList(startNeglected, endNeglected));
                List<Integer> merged = merge(arr1, arr2);

                // remove last bracket plus neglected from sortStep and add merged back
                sortStep = sortStep.subList(0, startNeglected - bracketSize);
                sortStep.addAll(merged);
            }

            // not sure if we have to copy here as sortStep gets reinstantiated each iteration. Must test to see what happens if we don't copy and just use a reference.
            sorting = new ArrayList<Integer>(sortStep);
            bracketSize = bracketSize*2;
        }
        
        // sorting ArrayList is now sorted (or at least it should be!)
        return sorting;
    }

    public static List<Integer> mergeSortTopDown(List<Integer> unsorted) {
        List<Integer> sorted = new ArrayList<Integer>();
        
        if (unsorted.size() <= 1) { // we could also have empty array here that's why '<'
            return unsorted;
        }
        
        // need to instantiate new because we alter the arrays in the merge function
        List<Integer> arr1 = new ArrayList<Integer>(mergeSortTopDown(new ArrayList<Integer>(unsorted.subList(0, (unsorted.size()/2))))); 
        List<Integer> arr2 = new ArrayList<Integer>(mergeSortTopDown(new ArrayList<Integer>(unsorted.subList(unsorted.size()/2, unsorted.size()))));
        
        sorted = merge(arr1, arr2);

        return sorted;
    }

    /**
     * merge takes 2 sorted lists and merges them in order.
     * 
     * @param arr1 first list
     * @param arr2 second list
     * @return list containing the contents of arr1 first and arr2 second
     */
    private static List<Integer> merge(List<Integer> arr1, List<Integer> arr2) {
        
        List<Integer> merged = new ArrayList<Integer>();
        int totalSize = arr1.size() + arr2.size();

        for (int i = 0; i < totalSize; i++) {
            if (arr1.size() > 0 && arr2.size() > 0) { // both arrays still have values
                if (arr1.get(0) <= arr2.get(0)) {
                    merged.add(arr1.get(0));
                    arr1.remove(0);
                } else {
                    merged.add(arr2.get(0));
                    arr2.remove(0);
                }
            } else if (arr1.size() == 0 && arr2.size() > 0) {
                merged.add(arr2.get(0));
                arr2.remove(0);
            } else if (arr2.size() == 0 && arr1.size() > 0) {
                merged.add(arr1.get(0));
                arr1.remove(0);
            }
        }

        return merged;
    }
}

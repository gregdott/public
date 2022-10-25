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
        List<Integer> sorted = mergeSortTopDown(unsorted);
        Pr.x("Unsorted: " + unsorted.toString());
        Pr.x("Sorted: " + sorted.toString());
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

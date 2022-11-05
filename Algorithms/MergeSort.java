package Algorithms;

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
        List<Integer> unsorted = new ArrayList<Integer>(Arrays.asList(1, 9, 3, 7, 33, 42, 2, 3, 4, 99, 1001, 32, 34, 78, 43));
        Pr.x("Unsorted: " + unsorted.toString());

        //-----------------------------------------------------------------------
        // Recursive:
        Pr.x("----------------------------------------------------");
        Pr.x("Merge Sort (Recursive - Top Down):");
        List<Integer> sortedRecursive = mergeSortTopDown(unsorted);
        Pr.x("Sorted: " + sortedRecursive.toString());
        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Iterative:
        Pr.x("----------------------------------------------------");
        Pr.x("Merge Sort (Iterative - Bottom Up):");
        List<Integer> sortedIterative = mergeSortBottomUp(unsorted);
        Pr.x("SORTED: " + sortedIterative.toString());
        Pr.x("----------------------------------------------------");
        //-----------------------------------------------------------------------
    }

    /**
     * mergeSortTopDown - recursive implementation of merge sort. Definitely preferable to my current iterative approach.
     * 
     * @param unsorted the unsorted list
     * @return List<Integer> of sorted elements
     */
    public static List<Integer> mergeSortTopDown(List<Integer> unsorted) {
        List<Integer> sorting = new ArrayList<Integer>(); // ArrayList to manage the sorting process
        
        if (unsorted.size() <= 1) { // we could also have empty array here that's why '<'
            return unsorted;
        }
        
        // need to instantiate new because we alter the arrays in the merge function
        List<Integer> arr1 = new ArrayList<Integer>(mergeSortTopDown(new ArrayList<Integer>(unsorted.subList(0, (unsorted.size()/2))))); 
        List<Integer> arr2 = new ArrayList<Integer>(mergeSortTopDown(new ArrayList<Integer>(unsorted.subList(unsorted.size()/2, unsorted.size()))));
        
        sorting = merge(arr1, arr2);

        // sorting ArrayList is now sorted
        return sorting;
    }

    /**
     * mergeSortBottomUp - iterative implementation of merge sort. Not 100% happy with this implementation. It's very cumbersome
     * in comparison to the recursive implementation. I have not looked at other iterative implementations yet to see where I might 
     * be able to improve this (as I'm sure is possible at least to some degree). 
     * The amount of commenting required to make this intelligible reveals my discomfort with this implementation.
     * That's something to do at a later stage.
     * For now, I present my little monster:
     * 
     * @param unsorted the unsorted list
     * @return List<Integer> of sorted elements
     */
    public static List<Integer> mergeSortBottomUp(List<Integer> unsorted) {
        List<Integer> sorting = new ArrayList<Integer>(unsorted); // ArrayList to manage the sorting process
        int bracketSize = 2; // Initial bracket size
        // The idea with using a bracket size that keeps doubling is to represent the successively bigger sub-arrays we will be merging

        while (bracketSize*2 < unsorted.size()) { // While our sub arrays are no bigger than half the total unsorted list size
            List<Integer> sortStep = new ArrayList<Integer>(); // Temp ArrayList to store the current state of the partially sorted list
            for (int i = 0; i < unsorted.size()/bracketSize; i = i + 2) {
                // i is the start index of the first of our current brackets (we are looking at 2 brackets at once each iteration - arr1 & arr2)
                // This is the reason we increment i by 2 each time, because each iteration, we are looking at 2 brackets defined by the current bracket size
                int minEnd = (i*bracketSize) + bracketSize*2;

                if (minEnd > unsorted.size()) { // there are cases where `(i*bracketSize) + bracketSize*2` is greater than the size of the array, so we set it to the size
                    minEnd = unsorted.size();
                }

                List<Integer> arr1 = new ArrayList<Integer>(sorting.subList(i*bracketSize, (i*bracketSize) + bracketSize)); // first bracket
                List<Integer> arr2 = new ArrayList<Integer>(sorting.subList((i*bracketSize) + bracketSize, minEnd)); // second bracket
                List<Integer> merged = merge(arr1, arr2);

                sortStep.addAll(merged); // merge our sorted arrays
            }
            
            /*
             * A crucial aspect of my little monster
             * Due to the doubling of the bracket size each iteration of the while loop, we see bracket size following the pattern
             * 2, 4, 8, 16, 32 etc. So we are increasing the bracket size by powers of 2. This means that we are quite likely to encounter
             * multiple iterations (unless our arrays are all of a size that is a power of 2) where after we have divided our array into brackets,
             * there will be some number of elements left on the end, unattended to. This part takes care of those neglected elements.
             * 
             * We basically determine where the set of neglected elements starts and ends. We know from previous iterations that this set will be in relative
             * order. We can then take it and merge it with the last bracketed set that was dealt with in the loop above (as that will also be in order)
             * 
             * For this to then go back properly into the main list sortStep, we must take a sublist of that so that we now ignore the last bracketed section 
             * as it has now been merged with the neglected elements, before then appending that sorted set back into sortStep.
             * 
             * This implementation truly is a monster.
             */
            if (sorting.size()%bracketSize > 0) {
                int startNeglected = (sorting.size()/bracketSize)*bracketSize;
                int endNeglected = startNeglected + (sorting.size()%bracketSize); // this could actually just be sorting.size() to be fair...

                sortStep.addAll(sorting.subList(startNeglected, endNeglected));

                List<Integer> arr1 = new ArrayList<Integer>(sortStep.subList(startNeglected - bracketSize, startNeglected)); // get elements from last sorted bracket
                List<Integer> arr2 = new ArrayList<Integer>(sortStep.subList(startNeglected, endNeglected)); // get neglected elements
                List<Integer> merged = merge(arr1, arr2); // merge the sorted lists into a new sorted list

                sortStep = sortStep.subList(0, startNeglected - bracketSize); // remove last bracket of sorted elements from sortStep
                sortStep.addAll(merged); // append newly sorted list containing elements from last bracket and neglected elements
            }

            // not sure if we have to copy here as sortStep gets reinstantiated each iteration. Must test to see what happens if we don't copy and just use a reference.
            sorting = new ArrayList<Integer>(sortStep);
            bracketSize = bracketSize*2; // double the bracket size
        }
        
        // sorting ArrayList is now sorted
        return sorting;
    }

    /**
     * merge takes 2 sorted lists and merges them in order.
     * Used by both iterative and recursive implementations of the merge sort algorithm
     * 
     * @param arr1 first list
     * @param arr2 second list
     * @return list containing the contents of both arr1 and arr2 in ascending order
     */
    private static List<Integer> merge(List<Integer> arr1, List<Integer> arr2) {
        List<Integer> merged = new ArrayList<Integer>();
        int totalSize = arr1.size() + arr2.size();

        for (int i = 0; i < totalSize; i++) {
            if (arr1.size() > 0 && arr2.size() > 0) { // both arrays still have values
                /*
                 * Compare the values at the beginning of arr1 & arr2
                 * Since arr1 & arr2 are each in ascending order already, we can create a new sorted array with all values 
                 * by successively comparing only the first items in each array, using the smallest value in our new array
                 * and then discarding it from its original array. We do this repeatedly until both arrays have been exhausted.
                 */
                if (arr1.get(0) <= arr2.get(0)) {
                    merged.add(arr1.get(0));
                    arr1.remove(0);
                } else {
                    merged.add(arr2.get(0));
                    arr2.remove(0);
                }
            } else if (arr1.size() == 0 && arr2.size() > 0) { // only arr2 has values so ignore arr1 and just use the first value from arr2
                merged.add(arr2.get(0));
                arr2.remove(0);
            } else if (arr2.size() == 0 && arr1.size() > 0) { // only arr1 has values so ignore arr2 and just use the first value from arr1
                merged.add(arr1.get(0));
                arr1.remove(0);
            }
        }
        return merged;
    }
}
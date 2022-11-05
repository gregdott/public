package Algorithms;

import java.util.*;
import Utils.*;
/**
 * Author: Gregory Dott
 * 18-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * Quicksort is an in-place sorting algorithm. Developed by British computer scientist Tony Hoare in 1959 and published in 1961,
 * it is still a commonly used algorithm for sorting. When implemented well, it can be somewhat faster than merge sort and about 
 * two or three times faster than heapsort. Quicksort is a divide-and-conquer algorithm. It works by selecting a 'pivot' element 
 * from the array and partitioning the other elements into two sub-arrays, according to whether they are less than or greater than the pivot. 
 * For this reason, it is sometimes called partition-exchange sort. The sub-arrays are then sorted recursively. This can be done in-place, 
 * requiring small additional amounts of memory to perform the sorting.
 * Quicksort is a comparison sort, meaning that it can sort items of any type for which a "less-than" relation (formally, a total order) is defined. 
 * Efficient implementations of Quicksort are not a stable sort, meaning that the relative order of equal sort items is not preserved.
 * ======================================================================================================================================
 * 
 * Basic implementation of Quick Sort algorithm for integers
 * 
 * There are 2 implementations given. 
 * The first, sortInts() uses ArrayLists to store the data (Neater)
 * The second, sortIntsArray() uses default Java Arrays (Uglier, clunkier, but theoretically faster. Have not tested that thought.)
 * 
 */

public class QuickSort {

    public static void main (String[] args) {
        //-----------------------------------------------------------------------
        // ArrayList<Integer> implementation
        Pr.x("=================================================================");
        Pr.x("ArrayList<Integer>:");
        List<Integer> unsorted = new ArrayList<Integer>(Arrays.asList(3, 6, -4, 5, 33, 7, 11, 21, 19, -90));
        List<Integer> sorted = sortInts(unsorted);
        Pr.x("Unsorted: " + unsorted.toString());
        Pr.x("Sorted: " + sorted.toString());
        Pr.x("=================================================================");
        //-----------------------------------------------------------------------

        //-----------------------------------------------------------------------
        // Array implementation
        Pr.x("=================================================================");
        Pr.x("Array (int[]):");
        int[] unsortedArray = {3, 6, -4, 5, 33, 7, 11, 21, 19, -90};
        int[] sortedArray = sortIntsArray(unsortedArray);
        Pr.x("Unsorted: " + Arrays.toString(unsortedArray));
        Pr.x("Sorted: " + Arrays.toString(sortedArray));
        Pr.x("=================================================================");
        //-----------------------------------------------------------------------
    }

    /**
     * sortInts: List<Integer> based implementation of  Quicksort
     * 
     * @param unsorted List<Integer> of unsorted ints
     * @return List<Integer> of sorted ints
     */
    public static List<Integer> sortInts(List<Integer> unsorted) {
        // Base case - we have reached the smallest array size - we could have size of 0 or 1 depending on how pivot relates to other array elements
        if (unsorted.size() <= 1) {
            return unsorted;
        } else {
            // Select pivot, arrange numbers either side of pivot and recurse
            int pivot = unsorted.get(unsorted.size() - 1);
            List<Integer> beforePivot = new ArrayList<Integer>();
            List<Integer> afterPivot = new ArrayList<Integer>();

            // Loop through unsorted elements and place them in either beforePivot or afterPivot based on their value relative to pivot
            for (int i = 0; i < unsorted.size() - 1; i++) {
                if (unsorted.get(i) <= pivot) {
                    beforePivot.add(unsorted.get(i));
                } else {
                    afterPivot.add(unsorted.get(i));
                }
            }

            List<Integer> beforePivotSorted = sortInts(beforePivot); // recurse on elements before pivot
            beforePivotSorted.add(pivot); // add pivot back
            List<Integer> afterPivotSorted = sortInts(afterPivot); // recurse on elements after pivot

            beforePivotSorted.addAll(afterPivotSorted); // join the two lists together
            return beforePivotSorted;
        }
    }
    
    /**
     * sortIntsArray: array based implementation of Quicksort. 
     * Uses default Java arrays. Super clunky. This has been replaced in sortInts() with the use of List<Integer>
     * Just keeping this here for now for the sake of interest. It's a good example of an ugly implementation ;)
     * 
     * @param unsorted array of ints
     * @return sorted array of ints
     */
    public static int[] sortIntsArray(int[] unsorted) {
        // Base case - we have reached the smallest array size - we could have size of 0 depending on how pivot relates to other array elements
        if (unsorted.length <= 1) {
            return unsorted;
        } else {
            // Select pivot, arrange numbers either side of pivot and recurse
            int pivot = unsorted[unsorted.length - 1];
            int beforePivotSize, afterPivotSize;
            beforePivotSize = afterPivotSize = 0;

            // Loop through unsorted and determine how many elements come before and after pivot (this is necessary because of fixed array lengths in Java)
            // Could use ArrayList or something similar, but want to minimise on that complexity, so this complexity is necessary (should have a look to see which is more efficient)
            for (int i = 0; i < unsorted.length - 1; i++) {
                if (unsorted[i] <= pivot) {
                    beforePivotSize++;
                } else {
                    afterPivotSize++;
                }
            }

            // Initialise arrays containing elements before pivot and after pivot
            int[] beforePivot = new int[beforePivotSize];
            int[] afterPivot = new int[afterPivotSize];
            int beforePivotIndex, afterPivotIndex;
            beforePivotIndex = afterPivotIndex = 0;

            for (int i = 0; i < unsorted.length - 1; i++) {
                if (unsorted[i] <= pivot) {
                    beforePivot[beforePivotIndex] = unsorted[i];
                    beforePivotIndex++;
                } else {
                    afterPivot[afterPivotIndex] = unsorted[i];
                    afterPivotIndex++;
                }
            }

            // Call the sort function on the elements before and after pivot
            beforePivot = sortIntsArray(beforePivot);
            afterPivot = sortIntsArray(afterPivot);

            // Putting everything together
            int[] sorted = new int[unsorted.length];
            int sortedIndex = 0;

            for (int i = 0; i < beforePivot.length; i++) {
                sorted[sortedIndex] = beforePivot[i];
                sortedIndex++;
            }

            sorted[sortedIndex] = pivot;
            sortedIndex++;

            for (int i = 0; i < afterPivot.length; i++) {
                sorted[sortedIndex] = afterPivot[i];
                sortedIndex++;
            }

            return sorted;
        }
    }
}

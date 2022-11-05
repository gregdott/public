package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * ========================================================================================================
 * Insertion sort is a simple sorting algorithm that builds the final sorted array (or list) one item 
 * at a time by comparisons. It is much less efficient on large lists than more advanced algorithms such 
 * as quicksort, heapsort, or merge sort.
 * ========================================================================================================
 * 
 * There are 2 implementations provided below:
 * Iterative
 * Recursive
 */

public class InsertionSort {
    public static void main(String args[]) {
        int[] nums = {3, 8, 5, 4, 1, 9, -2};
        Pr.x("Unsorted: " + Arrays.toString(nums));
        int[] sorted1 = insertionSortIterative(nums);
        int[] sorted2 = insertionSortRecursive(nums, 1);

        Pr.x("Sorted iterative: " + Arrays.toString(sorted1));
        Pr.x("Sorted recursive: " + Arrays.toString(sorted2));
    }

    /**
     * insertionSortIterative - iterative implementation of the insertion sort algorithm
     * @param nums array of unsorted numbers
     * @return array of sorted numbers
     */
    private static int[] insertionSortIterative(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            for (int j = i; j > 0; j--) {
                if (nums[j] < nums[j-1]) { // if next element is less than current element in sorted list, swap them
                    int tmp = nums[j-1];
                    nums[j-1] = nums[j];
                    nums[j] = tmp;
                } else {
                    break; // element is in place. no need to go further
                }
            }
        }

        return nums;
    }

    /**
     * insertionSortRecursive - recursive implementation of the insertion sort algorithm
     * @param nums array of unsorted numbers
     * @param index int index of current position in array
     * @return array of sorted numbers
     */
    private static int[] insertionSortRecursive(int[] nums, int index) {
        if (index >= nums.length) {
            return nums;
        }

        for (int j = index; j > 0; j--) {
            if (nums[j] < nums[j-1]) { // if next element is less than current element in sorted list, swap them
                int tmp = nums[j-1];
                nums[j-1] = nums[j];
                nums[j] = tmp;
            } else {
                break; // element is in place. no need to go further
            }
        }

        index++;
        return insertionSortRecursive(nums, index);
    }
}

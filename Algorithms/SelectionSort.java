package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * ======================================================================================================
 * In computer science, selection sort is an in-place comparison sorting algorithm. It has an O(n2) time 
 * complexity, which makes it inefficient on large lists, and generally performs worse than the similar 
 * insertion sort. Selection sort is noted for its simplicity and has performance advantages over more 
 * complicated algorithms in certain situations, particularly where auxiliary memory is limited.
 * ======================================================================================================
 * 
 * Explained as simply as possible:
 * 
 * Find first smallest element in the array. Swap this with the first element in the array.
 * Then find next smallest element in the array. Swap this with the second element in the array.
 * Continue until we have considered all elements.
 * 
 * Two implementations are provided below:
 * Iterative
 * Recursive
 */

public class SelectionSort {
    public static void main(String args[]) {
        int[] nums = {3, 5, 8, 4, 1, 9, -2};
        Pr.x("Unsorted: " + Arrays.toString(nums));
        int[] sorted1 = selectionSortIterative(nums);
        Pr.x("Sorted iterative: " + Arrays.toString(sorted1));

        int[] sorted2 = selectionSortRecursive(nums, 0);
        Pr.x("Sorted recursive: " + Arrays.toString(sorted2));
    }

    /**
     * selectionSortIterative - iterative implementation of Selection Sort Algorithm
     * 
     * @param nums array of unsorted ints
     * @return array of sorted ints
     */
    private static int[] selectionSortIterative(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            int minIndex = findMinIndex(nums, i); // get the index of the min value in unsorted section of array
            // swap the current min value with the value sitting at i (the next expansion of the sorted array)
            int tmp = nums[i];
            nums[i] = nums[minIndex];
            nums[minIndex] = tmp;
        }

        return nums;
    }

    private static int[] selectionSortRecursive(int[] nums, int index) {
        if (index >= nums.length) { // we have explored all numbers in array, so it must be sorted now.
            return nums;    
        }

        int minIndex = findMinIndex(nums, index); // get the index of the min value in unsorted section of array
        // swap the current min value with the value sitting at index (the next expansion of the sorted array)
        int tmp = nums[index];
        nums[index] = nums[minIndex];
        nums[minIndex] = tmp;

        index++;
        return selectionSortRecursive(nums, index);
    }

    /**
     * findMinIndex - get the index of the min value in the portion of the array from given start index to the end
     * 
     * @param nums array of numbers
     * @param start index to search from
     * @return int representing the index of the item with the min value between given start and the end of the array
     */
    private static int findMinIndex(int[] nums, int start) {
        int minIndex = 0;
        int min = Integer.MAX_VALUE;

        for (int i = start; i < nums.length; i++) {
            if (nums[i] < min) {
                min = nums[i];
                minIndex = i;
            }
        }

        return minIndex;
    }
}

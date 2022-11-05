package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * From Wikipedia:
 * ===========================================================================================================
 * In computer science, quickselect is a selection algorithm to find the kth smallest element in an unordered 
 * list. It is related to the quicksort sorting algorithm. Like quicksort, it was developed by Tony Hoare, 
 * and thus is also known as Hoare's selection algorithm. Like quicksort, it is efficient in practice and 
 * has good average-case performance, but has poor worst-case performance. Quickselect and its variants are 
 * the selection algorithms most often used in efficient real-world implementations.
 * ===========================================================================================================
 */

public class QuickSelect {
    
    public static void main(String args[]) {
        int[] nums = { 7, 4, 6, 3, 9, 1 };
        int k = 2;

        int kth = select(nums, 0, nums.length - 1, k - 1);
        Pr.x("kth element: " + kth);
    }

    private static int select(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        int pivotIndex = left + ((right-left)/2); // just need a value between left and right. we choose halfway here.
        pivotIndex = partition(arr, left, right, pivotIndex);

        if (k == pivotIndex) {
            return arr[k];
        } else if (k < pivotIndex) {
            return select(arr, left, pivotIndex - 1, k);
        } else {
            return select(arr, pivotIndex + 1, right, k);
        }
    }

    private static int partition(int[] arr, int left, int right, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, right); // move pivot element to the end
        int storeIndex = left;

        for (int i = left; i <= right; i++) {
            if (arr[i] < pivotValue) {
                swap(arr, storeIndex, i);
                storeIndex++;
            }
        }
        
        swap(arr, right, storeIndex); // move pivot to its final place

        return storeIndex;
    }

    /**
     * swap - swaps the items at i1 and i2 in the array arr
     * @param arr array of ints
     * @param i1 index of first element
     * @param i2 index of second element
     */
    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }
}

package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * =========================================================================================================================
 * In computer science, counting sort is an algorithm for sorting a collection of objects according to keys 
 * that are small positive integers; that is, it is an integer sorting algorithm. It operates by counting the 
 * number of objects that possess distinct key values, and applying prefix sum on those counts to determine the
 * positions of each key value in the output sequence. Its running time is linear in the number of items and the 
 * difference between the maximum key value and the minimum key value, so it is only suitable for direct 
 * use in situations where the variation in keys is not significantly greater than the number of items. It is 
 * often used as a subroutine in radix sort, another sorting algorithm, which can handle larger keys more efficiently. 
 * =========================================================================================================================
 * 
 * Below is a very simple implementation of counting sort. It can easily be adapted for use with more advanced objects.
 * In fact... 
 * TODO: implement this with a list of objects instead of just an array.
 * TODO: Also understand properly how the prefix sums stuff works in this. A tiny bit confused...
 */
public class CountingSort {
    
    public static void main(String args[]) {
        int[] nums = { 4, 2, 10, 10, 1, 4, 2, 1, 10 }; // nums[n] always non-negative
        int k = 10; // max value in nums
        Pr.x("Unsorted: " + Arrays.toString(nums));
        int[] sorted = countingSort(nums, k);

        Pr.x("Sorted: " + Arrays.toString(sorted));
    }

    private static int[] countingSort(int[] nums, int k) {
        int[] count = new int[k+1]; // records the number of times each element appears in the sequence (we use k+1 so that we have index from 0 to k)
        int[] sorted = new int[nums.length];

        // Here we count and store the occurences of each value in the count array
        for (int i = 0; i < nums.length; i++) {
            //j = key(nums[i]) -- this will be what we use when we use objects. just using an array now, so j = nums[i] as these are our keys.
            int j = nums[i];
            count[j] = count[j] + 1;
        }

        // Now replace count values with calculated prefix sums (need to understand what this is about :/ )
        for (int i = 1; i <= k; i++) {
            count[i] = count[i] + count[i-1];
        }

        for (int i = nums.length - 1; i >= 0; i--) {
            //j = key(input[i])! for object implementation
            int j = nums[i];
            count[j] = count[j] - 1;
            sorted[count[j]] = nums[i]; // output[count[j]] = input[i]
        }

        return sorted;
    }
}

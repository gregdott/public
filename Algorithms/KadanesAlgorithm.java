package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 31-10-2022
 * 
 * 
 * From Wikipedia (slight alteration due to images being used in wiki descr):
 * ===========================================================================================================================
 * In computer science, the maximum sum subarray problem, also known as the maximum segment sum problem, is the task of 
 * finding a contiguous subarray with the largest sum, within a given one-dimensional array A[1...n] of numbers. Formally, 
 * the task is to find indices i and j with 1 <= i <= j <= n, such that the sum from i to j is as large as possible.
 * Each number in the input array A could be positive, negative, or zero.
 * 
 * Kadane's original algorithm solves the problem version when empty subarrays are admitted. It scans the given array A[1...n] 
 * from left to right. In the jth step, it computes the subarray with the largest sum ending at j; this sum is maintained in 
 * variable current_sum. Moreover, it computes the subarray with the largest sum anywhere in A[1...j], maintained in variable 
 * best_sum, and easily obtained as the maximum of all values of current_sum seen so far.
 * ===========================================================================================================================
 */

public class KadanesAlgorithm {
    public static void main(String args[]) {
        int[] nums = { -2, 1, -3, 4, -1, 2, 1, -5, 4 };
        maxSubArray(nums);
    }

    private static void maxSubArray(int[] nums) {
        int currentSum = 0;
        int maxSum = 0;
        int bestStart, bestEnd, currentStart;
        currentStart = bestStart = bestEnd = 0;

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];

            if (currentSum <= 0) {
                currentStart = i;
                currentSum = num;
            } else {
                currentSum += num;
            }

            if (currentSum > maxSum) {
                maxSum = currentSum;
                bestStart = currentStart;
                bestEnd = i;
            }
        }

        Pr.x("Max Sum: " + maxSum);
        Pr.x("Best Start: " + bestStart + ", Best End: " + bestEnd);
    }
}

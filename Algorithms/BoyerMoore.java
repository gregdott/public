package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * From Wikipedia:
 * ================================================================================================================================================
 * The Boyer–Moore majority vote algorithm is an algorithm for finding the majority of a sequence of elements using linear time and constant space. 
 * It is named after Robert S. Boyer and J Strother Moore, who published it in 1981, and is a prototypical example of a streaming algorithm.
 * ================================================================================================================================================
 */

public class BoyerMoore {
    
    public static void main(String args[]) {
        int[] nums = {1, 8, 7, 4, 1, 2, 2, 2, 2, 2, 2,};
        int m = findMajorityElement(nums);
        Pr.x("M: " + m);
    }

    private static int findMajorityElement(int[] nums) {
        int i, m;
        m = i = 0;

        // find majority element
        for (int num: nums) {
            if (i == 0) {
                m = num;
            } else if (m == num) {
                i++;
            } else {
                i--;
            }
        }

        // confirm majority element (if there is no majority element, m = some arbitrary element, so need to confirm)
        int count = 0;
        for (int num: nums) {
            if (num == m) {
                count++;
            }
        }

        if (count < nums.length/2) {
            m = -1;
        }

        return m;
    }
}

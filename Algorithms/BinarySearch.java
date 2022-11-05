package Algorithms;

/**
 * Author: Gregory Dott
 * 18-10-2022
 * 
 * From Wikipedia:
 * ======================================================================================================================================
 * In computer science, binary search, also known as half-interval search, logarithmic search, or binary chop, 
 * is a search algorithm that finds the position of a target value within a SORTED array.
 * Binary search compares the target value to the middle element of the array. 
 * If they are not equal, the half in which the target cannot lie is eliminated and the search continues on the remaining half, 
 * again taking the middle element to compare to the target value, and repeating this until the target value is found. 
 * If the search ends with the remaining half being empty, the target is not in the array.
 * ======================================================================================================================================
 * 
 * Makes O(log n) comparisons (by taking advantage of the fact that the array is sorted) which is better than linear complexity (O(n))
 * 
 * There are 2 implementations of this algorithm below. A recursive one and a loopy (iterative) one.
 * Correct behaviour is not guaranteed (obviously) if the array provided is not in ascending order
 * That is why we call the checkArrayOrdering() function before doing anything and admonish the user for reprehensible behaviour
 * 
 * On the basic test array used below, the recursive implementation appears to be ~3x more efficient. Why?
 * Even after a bit of clean up in the iterative version, the recursive solution still outperforms it by around 3x... Very interesting.
 * Lol, the time testing is not foolproof. Switching which one is called first around, the one that is called first takes ~3x longer.
 * So there must be some memory operations (possibly to do with the array?) that take place when the first one is called but do not
 * need to happen when the second one is called. Need a more robust way of testing execution times...
 */

public class BinarySearch {

    public static void main(String[] args) {
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8};

        if (checkArrayAscending(arr)) {
            long startTime1 = System.nanoTime();
            System.out.println(Integer.toString(searchLoopy(arr, 5)));
            long stopTime1 = System.nanoTime();
            long timeTaken1 = stopTime1 - startTime1;

            System.out.println("TIME TAKEN (loop): " + Long.toString(timeTaken1));

            long startTime2 = System.nanoTime();
            System.out.println(Integer.toString(searchRecursive(arr, 5, 0, arr.length - 1)));
            long stopTime2 = System.nanoTime();
            long timeTaken2 = stopTime2 - startTime2;

            System.out.println("TIME TAKEN (recursion): " + Long.toString(timeTaken2));
            
        } else {
            System.out.println("The Binary Search algorithm requires the provided array is arranged in ascending order. Please correct this!");
        }
        
    }

    /**
     * searchRecursive: implementation of the Binary Search algorithm, implemented using recursion
     * 
     * @param arr array of integers which should be sorted in ascending order
     * @param toFind value of element to search for
     * @return -1 if toFind is not in arr, otherwise it returns the index of the first instance found
     */
    public static int searchRecursive(int[] arr, int toFind, int start, int end) {
        
        if (start > end) {
            return -1;
        } else {
            int middle = start + (end - start)/2;

            if (toFind > arr[middle]) {
                start = middle + 1; // shift new start to one element right of the middle
            } else if (toFind < arr[middle]) {
                end = middle - 1; // shift new end to one element left of the middle
            } else { // toFind == arr[middle] => we have found our element
                return middle;
            }

            return searchRecursive(arr, toFind, start, end);
        }
    }

    /**
     * searchLoopy: implementation of the Binary Search algorithm, implemented using a while loop (and not recursion)
     * 
     * @param arr array of integers which should be sorted in ascending order
     * @param toFind value of element to search for
     * @return -1 if toFind is not in arr, otherwise it returns the index of the first instance found
     */
    public static int searchLoopy(int[] arr, int toFind) {
        
        int middle, start = 0, end = arr.length - 1;
        middle = start + (end - start)/2;
        
        // While !found & !exhausted, check middle element of smaller array
        while (start <= end) {
            if (toFind < arr[middle]) {
                end = middle - 1;  // shift new end to one element left of the middle
            } else if (toFind > arr[middle]) {
                start = middle + 1;  // shift new start to one element right of the middle
            } else { // toFind == arr[middle] => we have found our element
                return middle;
            }
            middle = start + (end - start)/2;
        }
        return -1;
    }

    /**
     * checkArrayAscending - does what it says on the tin. Checks if array is in ascending order
     * 
     * @param arr array of values that we want to check is in ascending order
     * @return boolean indicating whether array is in ascending order or not
     */
    private static boolean checkArrayAscending(int[] arr) {
        int prevVal = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (prevVal > arr[i]) {
                return false; // As soon as we find one case where we see the array is not in ascending order, we abort.
            }
            prevVal = arr[i];
        }
        return true;
    }
}

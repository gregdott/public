package Algorithms;

import java.util.*;
import Utils.Pr;

/*
 * Author: Gregory Dott
 * 03-11-2022
 * 
 * From Wikipedia:
 * ================================================================================================================
 * In computer science, heapsort is a comparison-based sorting algorithm. Heapsort can be thought of as an 
 * improved selection sort: like selection sort, heapsort divides its input into a sorted and an unsorted region, 
 * and it iteratively shrinks the unsorted region by extracting the largest element from it and inserting it into 
 * the sorted region. Unlike selection sort, heapsort does not waste time with a linear-time scan of the unsorted 
 * region; rather, heap sort maintains the unsorted region in a heap data structure to more quickly find the 
 * largest element in each step.
 * ================================================================================================================
 * 
 * There are several important steps in implementing this algorithm:
 * 
 * 1. Build Max Heap from the list of numbers. This guarantees that the largest element in the array is at the top of the heap.
 *    This is done by buildMaxHeap()
 * 
 * 2. Swap the top of the heap with the last element in the considered range, and shrink considered range by 1.
 * 
 * 3. Fix the heap. Having replaced the root of the heap with an element that most likely isn't the largest in the range,
 *    we need to ensure that the max heap's integrity is fixed. This is down by 'sifting down'. Once heap is fixed, max element
 *    in considered range is now at the root. Go back to step 2 and continue until considered range is 0.
 * 
 * By following the steps above, we progressively create a sorted section of the array starting at the end with the 
 * largest element and going down until we have sorted the whole range.
 */

public class HeapSort {
    public static void main(String args[]) {
        //int[] nums = {6, 4, 7, 1, 9, -2, 99};
        int[] nums = {6, 4, 7, 999, 1, 9, -2, 99, 31, 2, 400, 19, 54, 361, 20, 22, 27, 33, 36, 45};
        Pr.x("Unsorted: " + Arrays.toString(nums));

        int[] sorted = heapsort(nums);
        Pr.x("Sorted: " + Arrays.toString(sorted));
    }

    /**
     * heapsort - implementation of the Heap Sort Algorithm
     * 
     * @param nums array of unsorted ints
     * @return array of sorted ints
     */
    private static int[] heapsort(int[] nums) {
        int[] maxHeap = buildMaxHeap(nums); // build max heap data structure so that max element is at root
        int end = nums.length - 1; // initialise considered range

        while(end >= 1) { // while we have not reached the end of the considered range
            swap(maxHeap, 0, end); // Swap the element at the end of the range with the top of the heap
            end--; // decrease the considered range by 1
            siftDown(maxHeap, end); // fix the heap after swapping the root with the end
            //siftDownRecursive(maxHeap, 0, end); // works equally well as siftDown().
        }

        return maxHeap;
    }

    

    /**
     * siftDown - repair the tree after having modified it. Make sure that it retains the max heap property
     * 
     * @param heap array containing max heap data structure
     * @param end the end of the heap essentially (everything after end will have been sorted and is no longer part of the 'heap')
     */
    private static void siftDown(int[] heap, int end) {
        
        int index = 0; // start at the root
        int left = (index*2) + 1; // index of the left child of where the item we are sifting down is located
        int right = (index*2) + 2; // index of the right child of where the item we are sifting down is located
        int largest = index;

        boolean treeHealthy = false;
        
        while(!treeHealthy) {

            if (left <= end && heap[index] < heap[left]) { // is the left child bigger than its parent?
                largest = left;
            }

            if (right <= end && heap[largest] < heap[right]) { // is the right child bigger than its parent & the left?
                largest = right;
            }

            if (largest == index) {
                treeHealthy = true;
                break;
            }

            swap(heap, index, largest);

            index = largest; // set index to where we swapped the element to
            left = (index*2) + 1; // get the new left child
            right = (index*2) + 2; // get the new right child
        }
    }

    /**
     * buildMaxHeap - build a max heap from the given array of nums
     * 
     * From Wikipedia:
     * ===================================================================================================================
     * Follow this basic algorithm for building a heap:
     * 
     * 
     * 1. Add the element to the bottom level of the heap at the leftmost open space.
     * 2. Compare the added element with its parent; if they are in the correct order, stop.
     * 3. If not, swap the element with its parent and return to the previous step.
     * ===================================================================================================================
     * 
     * Start with heapSize = 0. New elements get inserted at heapSize value (we are expanding the heap by one element each time)
     * First element in array goes there. Then heapSize = 1; Next element becomes leftmost child of first. If greater than,
     * we swap them.
     * We know based on the element's index who its parent is. We can swap easily in this way.
     * 
     * @param nums array of unsorted ints
     * @return array of ints organised in a max heap (largest int in array will be at position 0)
     */
    private static int[] buildMaxHeap(int[] nums) {
        int[] heap = new int[nums.length];
        heap[0] = nums[0]; // we do this first so that we don't have to deal with the case where heapSize is 0 and will cause comparison issues with parent
        int heapSize = 1;

        for (int i = 1; i < nums.length; i++) {
            heap[heapSize] = nums[i];
            compareWithAncestors(heap, heapSize);
            heapSize++;
        }

        return heap;
    }

    /**
     * compareWithAncestors - compares new element in heap with parent. If greater than parent, swaps element with parent
     * and then compares to new parent. It does this comparing and swapping with ancestors until the new heap element
     * is in the correct place in the tree.
     * 
     * This is essentially a sift up I believe
     * 
     * @param heap array representing our heap data structure
     * @param index the index of the new heap element (initially). Depending on what happens during execution, it may end up 
     *              tracking the index of the element as it is moved up the tree.
     */
    private static void compareWithAncestors(int[] heap, int index) {
        int parentIndex = (index-1)/2; // due to how we represent a binary tree in an array, finding the parent is simple
        
        // While our child is greater than its parent and we have not reached the root
        while (heap[index] > heap[parentIndex] && parentIndex >= 0) {
            swap(heap, index, parentIndex);
            index = parentIndex;
            parentIndex = (index-1)/2;
        }
    }

    /**
     * swap - swaps arr[i1] with arr[i2]
     * 
     * @param arr array in which we are swapping 2 elements
     * @param i1 index of first element
     * @param i2 index of second element
     */
    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i2];
        arr[i2] = arr[i1];
        arr[i1] = tmp;
    }

    /**
     * siftDownRecursive - a recursive sift-down
     * siftDownRecursive(maxHeap, 0, end)
     * Not currently used, but keeping here out of interest sake.
     * 
     * @param heap the heap that needs repairing
     * @param i the index to start from. Generally 0 (root) to start
     * @param end the end of the heap currently (where the sorted numbers start from)
     */
    private static void siftDownRecursive(int[] heap, int index, int end) {
        int left = (2*index) + 1;
        int right = (2*index) + 2;
        int largest = index;

        if (left <= end && heap[left] > heap[largest]) {
            largest = left;
        }

        if (right <= end && heap[right] > heap[largest]) {
            largest = right;
        }

        if (largest != index) {
            swap(heap, index, largest);
            siftDownRecursive(heap, largest, end);
        }
    }
}

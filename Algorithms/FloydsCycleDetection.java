package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 01-11-2022
 * 
 * From Wikipedia:
 * ================================================================================================================
 * Floyd's cycle-finding algorithm is a pointer algorithm that uses only two pointers, which move through the 
 * sequence at different speeds. It is also called the "tortoise and the hare algorithm", alluding to Aesop's fable 
 * of The Tortoise and the Hare.
 * The algorithm is named after Robert W. Floyd, who was credited with its invention by Donald Knuth. However, 
 * the algorithm does not appear in Floyd's published work, and this may be a misattribution: Floyd describes 
 * algorithms for listing all simple cycles in a directed graph in a 1967 paper, but this paper does not 
 * describe the cycle-finding problem in functional graphs that is the subject of this article. In fact, 
 * Knuth's statement (in 1969), attributing it to Floyd, without citation, is the first known appearance in 
 * print, and it thus may be a folk theorem, not attributable to a single individual.
 * ================================================================================================================
 * 
 */

public class FloydsCycleDetection {
    public static void main(String args[]) {
        int[] vals = {1, 2, 3, 4, 5};
        ListNode node = null;

        // Initialise a new node for each value and link it to the previous node (first node is linked to null)
        for (int val: vals) {
            node = new ListNode(val, node);
        }

        node.next.next.next.next.next = node.next.next; // create a cycle
        detectCycle(node);
    }

    /**
     * detectCycle - using Floyd's cycle detection algorithm
     * @param node represents a node in a linked list
     */
    private static void detectCycle(ListNode node) {
        ListNode tortoise, hare;

        // Start both tortoise and hare at the same point in the list
        tortoise = node;
        hare = node;

        // While we have not reached the end of the list
        while (hare != null && hare.next != null) {
            Pr.x("Tortoise: " + tortoise.val);
            Pr.x("Hare: " + hare.val);
            
            tortoise = tortoise.next; // move tortoise to next node
            hare = hare.next.next; // move hare to the node after the next node

            if (hare.val == tortoise.val) {
                Pr.x("CYCLE DETECTED!");
                Pr.x("Tortoise: " + tortoise.val);
                Pr.x("Hare: " + hare.val);
                return;
            }

           
        }
        Pr.x("No cycles detected...");
    }
}

/*
 * For storing a linked list...
 */
class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}

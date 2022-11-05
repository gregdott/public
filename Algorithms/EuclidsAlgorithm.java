package Algorithms;

import Utils.Pr;

/*
 * Author: Gregory Dott
 * 05-11-2022
 * 
 * From Wikipedia:
 * ================================================================================================================================================
 * In mathematics, the Euclidean algorithm, or Euclid's algorithm, is an efficient method for computing the greatest common divisor (GCD) of two 
 * integers (numbers), the largest number that divides them both without a remainder. It is named after the ancient Greek mathematician Euclid, 
 * who first described it in his Elements (c. 300 BC). It is an example of an algorithm, a step-by-step procedure for performing a calculation 
 * according to well-defined rules, and is one of the oldest algorithms in common use. It can be used to reduce fractions to their simplest form, 
 * and is a part of many other number-theoretic and cryptographic calculations.
 * ================================================================================================================================================
 * 
 */

public class EuclidsAlgorithm {
    public static void main(String args[]) {
        int a = 30;
        int b = 50;

        Pr.x("GCD sub: " + gcdSub(a, b));
        Pr.x("GCD div: " + gcdDiv(a, b));
        Pr.x("GCD rec: " + gcdRec(a, b));
    }

    private static int gcdSub(int a, int b) {
        while (a != b) {
            if (a > b) {
                a = a - b;
            } else {
                b = b - a;
            }
        }

        return a;
    }

    private static int gcdDiv(int a, int b) {
        while (b != 0) {
            int tmp = b;
            b = a%b;
            a = tmp;
        }
        return a;
    }

    private static int gcdRec(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcdRec(b, a%b);
        }
    }
}

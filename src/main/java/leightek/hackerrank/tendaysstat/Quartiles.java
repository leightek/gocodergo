/**
 * 10 Days of Statistics
 * Day 1: Quartiles
 *
 * Objective
 * In this challenge, we practice calculating quartiles. Check out the Tutorial tab for learning materials and an instructional video!
 *
 * Task
 * Given an array, X, of n integers, calculate the respective first quartile (Q1), second quartile (Q2), and third quartile (Q3). It is guaranteed that Q1, Q2, and Q3 are integers.
 *
 * Input Format
 *
 * The first line contains an integer, n, denoting the number of elements in the array.
 * The second line contains n space-separated integers describing the array's elements.
 *
 * Constraints
 * . 5<=n<=50
 * . 0<x[i]<=100, where x[i] is the ith element of the array.
 *
 * Output Format
 *
 * Print 3 lines of output in the following order:
 *
 * 1. The first line should be the value of Q1.
 * 2. The second line should be the value of Q2.
 * 3. The third line should be the value of Q3.
 *
 * Sample Input
 *
 * 9
 * 3 7 8 5 12 14 21 13 18
 *
 * Sample Output
 *
 * 6
 * 12
 * 16
 */

package leightek.hackerrank.tendaysstat;

import java.util.Arrays;
import java.util.Scanner;

public class Quartiles {
    public static int getMedian(int[] numbers) {
        int len = numbers.length;
        return len % 2 != 0 ? numbers[(len - 1) / 2] :  (numbers[len / 2 -1] + numbers[len / 2]) / 2;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String numberString = sc.nextLine();

        int[] numberArray = Arrays.stream(numberString.split(" "))
                .mapToInt(Integer::valueOf)
                .sorted()
                .toArray();

        int subLen = n % 2 != 0 ? (n - 1) / 2 : n / 2;
        int[] leftArr = Arrays.copyOfRange(numberArray, 0, subLen);
        int[] rightArr = Arrays.copyOfRange(numberArray, n - subLen, n);

        int q1 = getMedian(leftArr);
        int q2 = getMedian(numberArray);
        int q3 = getMedian(rightArr);
        System.out.println(q1);
        System.out.println(q2);
        System.out.println(q3);
    }
}

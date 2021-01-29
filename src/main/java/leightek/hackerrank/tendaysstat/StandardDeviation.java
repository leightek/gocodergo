/**
 * 10 Days of Statistics
 * Day 1: Standard Deviation
 *
 * Objective
 * In this challenge, we practice calculating standard deviation. Check out the Tutorial tab for learning materials and an instructional video!
 *
 * Task
 * Given an array, X, of N integers, calculate and print the standard deviation. Your answer should be in decimal form, rounded to a scale of 1 decimal place (i.e., 12.3 format). An error margin of +-0.1 will be tolerated for the standard deviation.
 *
 * Input Format
 *
 * The first line contains an integer, N, denoting the number of elements in the array.
 * The second line contains N space-separated integers describing the respective elements of the array.
 *
 * Constraints
 *
 * . 5<=N<=100
 * . 0<x[i]<=10^5, where x[i] is the element of array X.
 *
 * Output Format
 *
 * Print the standard deviation on a new line, rounded to a scale of 1 decimal place (i.e., 12.3 format).
 *
 * Sample Input
 *
 * 5
 * 10 40 30 50 20
 *
 * Sample Output
 *
 * 14.1
 */

package leightek.hackerrank.tendaysstat;

import java.util.Arrays;
import java.util.Scanner;

public class StandardDeviation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String numberString = sc.nextLine();

        int[] numberArr = Arrays.stream(numberString.split(" "))
                .mapToInt(Integer::valueOf)
                .toArray();
        double mean = Arrays.stream(numberArr)
                .average().orElse(0.0);

        double avgDistSquare = Arrays.stream(numberArr)
                .mapToDouble(i -> (i - mean) * (i - mean))
                .average().orElse(0.0);
        double standardDeviation = Math.sqrt(avgDistSquare);
        System.out.printf("%.1f%n", standardDeviation);
    }
}

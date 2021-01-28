/**
 * 10 Days of Statistics
 * Day 1: Interquartine Range
 *
 * Objective
 * In this challenge, we practice calculating the interquartile range. We recommend you complete the Quartiles challenge before attempting this problem.
 *
 * Task
 * The interquartile range of an array is the difference between its first (Q1) and third (Q3) quartiles (i.e., Q3-Q1).
 *
 * Given an array, X, of integers and an array, F, representing the respective frequencies of X's elements, construct a data set, S, where each occurs at frequency. Then calculate and print S's interquartile range, rounded to a scale of decimal place (i.e., 12.3 format).
 *
 * Tip: Be careful to not use integer division when averaging the middle two elements for a data set with an even number of elements, and be sure to not include the median in your upper and lower data sets.
 *
 * Input Format
 *
 * The first line contains an integer, n, denoting the number of elements in arrays X and F.
 * The second line contains n space-separated integers describing the respective elements of array X.
 * The third line contains n space-separated integers describing the respective elements of array F.
 *
 * Constraints
 * . 5<=n<=50
 * . 0<x[i]<=100, where x[i] is the ith element of array X.
 * . 0<Sum(f[i])<=1000, where f[i] is the element of array F.
 * . The number of elements in S is equal to Sum(F).
 *
 * Output Format
 *
 * Print the interquartile range for the expanded data set on a new line. Round your answer to a scale of 1 decimal place (i.e., 12.3 format).
 *
 * Sample Input
 *
 * 6
 * 6 12 8 10 20 16
 * 5 4 3 2 1 5
 *
 * Sample Output
 *
 * 9.0
 */

package leightek.hackerrank.tendaysstat;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class InterquartileRange {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String numberString = sc.nextLine();
        String frequencyString = sc.nextLine();

        int[] numberArr = Arrays.stream(numberString.split(" "))
                .mapToInt(Integer::valueOf)
                .toArray();
        int[] frequencyArr = Arrays.stream(frequencyString.split(" "))
                .mapToInt(Integer::valueOf)
                .toArray();
        int len = Arrays.stream(frequencyArr)
                .sum();

        int[] expendedArr = new int[len];
        final int[] position =new int[] {0};
        IntStream.range(0, n)
                .forEach(i -> {
                    Arrays.fill(expendedArr, position[0], position[0] + frequencyArr[i], numberArr[i]);
                    position[0] += frequencyArr[i];
                });
        Arrays.sort(expendedArr);

        int subLen = len % 2 != 0 ? (len - 1) / 2 : len / 2;
        int[] leftArr = Arrays.copyOfRange(expendedArr, 0, subLen);
        int[] rightArr = Arrays.copyOfRange(expendedArr, len - subLen, len);

        double q1 = getMedian(leftArr);
        double q3 = getMedian(rightArr);
        double interquartileRange = q3 - q1;

        System.out.printf("%.1f%n", interquartileRange);

    }

    public static double getMedian(int[] numbers) {
        int len = numbers.length;
        return len % 2 != 0 ? (double) numbers[(len - 1) / 2] : (double) (numbers[len / 2 -1] + numbers[len / 2]) / 2;
    }
}


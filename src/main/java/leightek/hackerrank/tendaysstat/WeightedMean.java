/**
 * 10 Days of Statistics
 * Day 0: Weighted Mean
 *
 * Objective
 * In the previous challenge, we calculated a mean. In this challenge, we practice calculating a weighted mean. Check out the Tutorial tab for learning materials and an instructional video!
 *
 * Task
 * Given an array, X, of N integers and an array, W, representing the respective weights of X's elements, calculate and print the weighted mean of X's elements. Your answer should be rounded to a scale of decimal place (i.e., 12.3 format).
 *
 * Input Format
 *
 * The first line contains an integer, N, denoting the number of elements in arrays X and W.
 * The second line contains N space-separated integers describing the respective elements of array X.
 * The third line contains N space-separated integers describing the respective elements of array W.
 *
 * Constraints
 * . 5 <= N <= 50
 * . 0 < x[i] <= 100, where x[i] is the ith element of array X.
 * . 0 < w[i] <= 100, where w[i] is the element of array W.
 *
 * Output Format
 *
 * Print the weighted mean on a new line. Your answer should be rounded to a scale of 1 decimal place (i.e., 12.3 format).
 *
 * Sample Input
 *
 * 5
 * 10 40 30 50 20
 * 1 2 3 4 5
 *
 * Sample Output
 *
 * 32.0
 */

package leightek.hackerrank.tendaysstat;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

public class WeightedMean {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int len = scanner.nextInt();
        scanner.nextLine();
        String numberString = scanner.nextLine();
        String weightString = scanner.nextLine();

        int[] numberArr = Arrays.stream(numberString.split(" "))
                .mapToInt(Integer::valueOf)
                .toArray();
        int[] weightArr = Arrays.stream(weightString.split(" "))
                .mapToInt(Integer::valueOf)
                .toArray();

        int weightedNumbersSum = IntStream.range(0, len)
                .map(i -> numberArr[i] * weightArr[i])
                .sum();
        int weightSum = IntStream.range(0, len)
                .map(i -> weightArr[i])
                .sum();

        double weightedMean = (double) weightedNumbersSum / weightSum;
        System.out.printf("%.1f%n", weightedMean);
    }
}

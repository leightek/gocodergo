/**
 * 10 Days of Statistics
 * Day 0: Mean, Median, and Mode
 *
 * Objective
 * In this challenge, we practice calculating the mean, median, and mode. Check out the Tutorial tab for learning materials and an instructional video!
 *
 * Task
 * Given an array, X, of N integers, calculate and print the respective mean, median, and mode on separate lines. If your array contains more than one modal value, choose the numerically smallest one.
 *
 * Note: Other than the modal value (which will always be an integer), your answers should be in decimal form, rounded to a scale of 1 decimal place (i.e., 12.3, 7.0 format).
 *
 * Example
 * N = 6
 * X = [1,2,3,4,5,5]
 * The mean is 20/6 = 3.3.
 * The median is (3+4)/2 = 3.5.
 * The mode is 5 because 5 occurs most frequently.
 *
 * Input Format
 *
 * The first line contains an integer, N, the number of elements in the array.
 * The second line contains N space-separated integers that describe the array's elements.
 *
 * Constraints
 * 10<= N <= 2500
 * 0 < x[i] <= 10^5, where x[i] is the i^th element of the array.
 *
 * Output Format
 *
 * Print 3 lines of output in the following order:
 *
 * 1. Print the mean on the first line to a scale of 1 decimal place (i.e., 12.3, 7.0).
 * 2. Print the median on a new line, to a scale of 1 decimal place (i.e., 12.3, 7.0).
 * 3. Print the mode on a new line. If more than one such value exists, print the numerically smallest one.
 *
 * Sample Input
 *
 * 10
 * 64630 11735 14216 99233 14470 4978 73429 38120 51135 67060
 *
 * Sample Output
 *
 * 43900.6
 * 44627.5
 * 4978
 */

package leightek.hackerrank.tendaysstat;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MeanMedianMode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int len = sc.nextInt();
        sc.nextLine();
        String string = sc.nextLine();
        sc.close();

        Integer[] numberArr = Arrays.stream(string.split(" "))
                .map(Integer::valueOf)
                .sorted()
                .toArray(Integer[]::new);

        double mean = Arrays.stream(numberArr).collect(Collectors.averagingDouble(n -> n));
        System.out.printf("%.1f%n", mean);

        int midIndex = len % 2 == 0 ? len / 2 - 1 : (len - 1) / 2;
        double median =  len % 2 == 0 ? (double) (numberArr[midIndex] + numberArr[midIndex + 1]) / 2 :
                numberArr[midIndex];
        System.out.printf("%.1f%n", median);

        Map<Integer, Long> countMap = Arrays.stream(numberArr)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Optional<Map.Entry<Integer, Long>> modeEntryO = countMap.entrySet()
                .stream()
                .sorted((e1, e2) -> (e1.getValue() == e2.getValue() ?
                        e1.getKey() - e2.getKey() :
                        e2.getValue().intValue() - e1.getValue().intValue()))
                .findFirst();
        int mode = modeEntryO.isPresent() ? modeEntryO.get().getKey() : 0;
        System.out.println(mode);
    }
}

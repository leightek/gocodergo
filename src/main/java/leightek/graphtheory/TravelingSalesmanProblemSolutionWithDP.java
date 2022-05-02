package leightek.graphtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Traveling Salesman Problem Solution: uses Dynamic Programming
 * Time complexity: O(n^2 * 2^n), Space Complexity: O(n * 2^n)
 */
public class TravelingSalesmanProblemSolutionWithDP {

    private final double MAXIMUM_COST = Double.POSITIVE_INFINITY;

    private final int N, start;
    private final double[][] distance;
    private List<Integer> tour = new ArrayList<>();
    private double minTourCost = MAXIMUM_COST;
    private boolean solved;

    public TravelingSalesmanProblemSolutionWithDP(double[][] distance) {
        this(0, distance);
    }

    public TravelingSalesmanProblemSolutionWithDP(int start, double[][] distance) {
        N = distance.length;

        if (N <= 2)
            throw new IllegalArgumentException("The node for traveling is less than 3!");
        if (N != distance[0].length)
            throw new IllegalArgumentException("The distance matrix is not square!");
        if (N > 32)
            throw new IllegalArgumentException("Matrix too large! The time complexity will be too much for home computer.");

        this.start = start;
        this.distance = distance;
    }

    /**
     * Get the optimal tour of the Traveling Salesman Problem
     * @return the optical tour
     */
    public List<Integer> getTour() {
        if (!solved)
            solve();

        return tour;
    }

    /**
     * Get the minimum tour cost of the Traveling Salesman Problem
     * @return minimum tour cost
     */
    public double getTourCost() {
        if (!solved)
            solve();

        return minTourCost;
    }

    // Solve the Traveling Salesman Problem with Dynamic Programming
    private void solve() {
        if (solved)
            return;

        final int END_STATE = (1 << N) - 1;
        Double[][] memo = new Double[N][1 << N];

        // Add all outgoing edges from the start node to memo table
        for (int end = 0; end < N; end++) {
            if (end == start)
                continue;
            memo[end][1 << start | 1 << end] = distance[start][end];
        }

        for (int r = 3; r <= N; r++) {
            for (int subset : combinations(r, N)) {
                if (notIn(start, subset))
                    continue;
                for (int next = 0; next < N; next++) {
                    if (next == start || notIn(next, subset))
                        continue;
                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = MAXIMUM_COST;
                    for (int end = 0; end < N; end++) {
                        if (end == start || end == next || notIn(end, subset))
                            continue;
                        double newDist = memo[end][subsetWithoutNext] + distance[end][next];
                        if (newDist < minDist)
                            minDist = newDist;
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        // Connect tour back to start node and minimize cost
        for (int i = 0; i < N; i++) {
            if (i == start)
                continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if (tourCost < minTourCost)
                minTourCost = tourCost;
        }

        reconstructTSPPath(END_STATE, memo);

        solved = true;
    }

    // Reconstruct TSP path from memo table
    private void reconstructTSPPath(int state, Double[][] memo) {
        int lastIndex = start;
        tour.add(start);

        for (int i = 1; i < N; i++) {
            int bestIndex = -1;
            double bestDist = MAXIMUM_COST;
            for (int j = 0; j < N; j++) {
                if (j == start || notIn(j, state))
                    continue;
                double newDist = memo[j][state] + distance[j][lastIndex];
                if (newDist < bestDist) {
                    bestIndex = j;
                    bestDist = newDist;
                }
            }

            tour.add(bestIndex);
            state = state ^ (1 << bestIndex);
            lastIndex = bestIndex;
        }

        tour.add(start);
        Collections.reverse(tour);
    }

    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    // Generate all bit sets of size n where r bits are set to one.
    private static List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();

        combinations(0, 0, r, n, subsets);

        return subsets;
    }

    // Find all the combinations of size r recursively until r elements (r = 0) have been selected
    // or select an element after the position of last selected element
    private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

        int remainingElements = n - at;
        if (remainingElements < r)
            return;

        if (r == 0) // slected 'r' elements
            subsets.add(set);
        else {
            for (int i = at; i < n; i++) {
                // Including this element
                set ^= (1 << i);
                combinations(set, i + 1, r - 1, n, subsets);

                // Backtrack the instance not including element
                set ^= (1 << i);
            }
        }
    }

    public static void main(String[] args) {
        int n = 6;
        double[][] m = new double[n][n];
        for (double[] row : m)
            Arrays.fill(row, 10000);
        m[5][0] = 10;
        m[1][5] = 12;
        m[4][1] = 2;
        m[2][4] = 4;
        m[3][2] = 6;
        m[0][3] = 8;

        int start = 0;
        TravelingSalesmanProblemSolutionWithDP solver = new TravelingSalesmanProblemSolutionWithDP(start, m);

        System.out.println("Tours: " + solver.getTour());  // [0, 3, 2, 4, 1, 5, 0]
        System.out.println("Tour cost: " + solver.getTourCost());  // 42.0
    }
}

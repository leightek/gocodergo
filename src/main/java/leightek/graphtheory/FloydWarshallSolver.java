package leightek.graphtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

/**
 * Floyd Warshall algorithm, solution of APSP (all pairs of the shortest paths) between nodes in a directed graph and
 * also detects negative cycles. Uses DP (dynamic programming).
 */
public class FloydWarshallSolver {

    private int n; // the size of the adjacency matrix
    private boolean solved;
    private double[][] dp; // the memo table that will contain APSP solution
    private Integer[][] next; // the matrix used to reconstruct the shortest paths

    private static final int REACHES_NEGATIVE_CYCLE = -1;

    public FloydWarshallSolver(double[][] matrix) {
        n = matrix.length;
        dp = new double[n][n];
        next = new Integer[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != POSITIVE_INFINITY)
                    next[i][j] = j;
                dp[i][j] = matrix[i][j];
            }
        }
    }

    /**
     * Run Floyd Warshall algorithm to compute APSP and detect negative cycle
     * @return the APSP matrix
     */
    public double[][] getAPSPMatrix() {
        if (!solved) {
            for (int k = 0; k < n; k ++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dp[i][k] + dp[k][j] < dp[i][j]) {
                            dp[i][j] = dp[i][k] + dp[k][j];
                            next[i][j] = next[i][k];
                        }
                    }
                }
            }

            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dp[i][k] != POSITIVE_INFINITY && dp[k][j] != POSITIVE_INFINITY && dp[k][k] < 0) {
                            dp[i][j] = NEGATIVE_INFINITY;
                            next[i][j] = REACHES_NEGATIVE_CYCLE;
                        }
                    }
                }
            }

            solved = true;
        }

        return dp;
    }

    /**
     * Reconstruct the shortest path from start node to the end node
     * @param start start node
     * @param end end node
     * @return the shortest path, could be null or empty
     */
    public List<Integer> reconstructShortestPath(int start, int end) {
        getAPSPMatrix();
        List<Integer> path = new ArrayList<>();

        if (dp[start][end] != POSITIVE_INFINITY) {
            int at = start;
            for (; at != end; at = next[at][end]) {
                if (at == REACHES_NEGATIVE_CYCLE)
                    return null;
                path.add(at);
            }

            if (next[at][end] == REACHES_NEGATIVE_CYCLE)
                return null;
            path.add(end);
        }

        return path;
    }

    /**
     * Create a directed graph
     * @param n the number of nodes
     * @return the adjacency matrix
     */
    public static double[][] createGraph(int n) {
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(matrix[i], POSITIVE_INFINITY);
            matrix[i][i] = 0;
        }

        return matrix;
    }

    public void printAPSP(double[][] dist) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("The shortest path from node %d to node %d is %.3f\n", i, j, dist[i][j]);
            }
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                List<Integer> path = reconstructShortestPath(i, j);
                String pathString;

                if (path == null) {
                    pathString = "reaches negative cycle!";
                } else if (path.size() == 0) {
                    pathString = "does not exist!";
                } else {
                    pathString = path.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(" -> "));
                }
                System.out.printf("The shortest path from node %d to node %d: %s\n", i, j, pathString);
            }

        }
    }

    public static void main(String[] args) {
        int n = 7;
        double[][] m = createGraph(n);

        m[0][1] = 2;
        m[0][2] = 5;
        m[0][6] = 10;
        m[1][2] = 2;
        m[1][4] = 11;
        m[2][6] = 2;
        m[6][5] = 11;
        m[4][5] = 1;
        m[5][4] = -2;

        FloydWarshallSolver solver = new FloydWarshallSolver(m);
        double[][] dist = solver.getAPSPMatrix();
        solver.printAPSP(dist);
    }
}

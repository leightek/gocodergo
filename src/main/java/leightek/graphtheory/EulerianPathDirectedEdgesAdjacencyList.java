package leightek.graphtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Eulerian Path and Circuit Algorithm: find Eulerian path
 */
public class EulerianPathDirectedEdgesAdjacencyList {

    private final int n;
    private int edgeCount;
    private int[] in, out;
    private LinkedList<Integer> path;
    private List<List<Integer>> graph;

    public EulerianPathDirectedEdgesAdjacencyList(List<List<Integer>> graph) {
        if (graph == null)
            throw new IllegalArgumentException("Graph cannot be null!");
        n = graph.size();
        this.graph = graph;
        path = new LinkedList<>();
    }

    /**
     * Get Eulerian path, a list of edgeCount + 1 node ids
     * @return the Eulerian path with an int array, or null if the graph is disconnected
     */
    public int[] getEulerianPath() {
        setUp();

        if (!graphHasEulerianPath())
            return null;

        dfs(findStartNode());

        // Traverse all edges of the graph
        if (path.size() != edgeCount + 1) // The graph disconnected
            return null;

        int[] solution = new int[edgeCount + 1];
        for (int i = 0; !path.isEmpty(); i++)
            solution[i] = path.removeFirst();

        return solution;
    }

    private void setUp() {
        in = new int[n];
        out = new int[n];

        edgeCount = 0;

        for (int from = 0; from < n; from++) {
            for (int to : graph.get(from)) {
                in[to]++;
                out[from]++;
                edgeCount++;
            }
        }
    }

    private boolean graphHasEulerianPath() {
        if (edgeCount == 0)
            return false;

        int startNodes = 0, endNodes = 0;
        for (int i = 0; i < n; i++) {
            if (out[i] - in[i] > 1 || in[i] - out[i] > 1)
                return false;
            else if (out[i] - in[i] == 1)
                startNodes++;
            else if (in[i] - out[i] == 1)
                endNodes++;
        }

        return (endNodes == 0 && startNodes == 0 || endNodes == 1 && startNodes == 1);
    }

    private int findStartNode() {
        int start = 0;
        for (int i = 0; i < n; i++) {
            if (out[i] - in[i] == 1)
                return i;
            if (out[i] - in[i] > 0) // start at node with an outgoing edge
                start = i;
        }

        return start;
    }

    // Perform DFS to find Eulerian path
    private void dfs(int at) {
        while (out[at] != 0) {
            int next = graph.get(at).get(--out[at]);
            dfs(next);
        }
        path.addFirst(at);
    }

    public static List<List<Integer>> initializeEmptyGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);

        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());

        return graph;
    }

    public static void addDirectedEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    public static void main(String[] args) {
        int n = 7;
        List<List<Integer>> graph = initializeEmptyGraph(n);
        addDirectedEdge(graph, 1, 2);
        addDirectedEdge(graph, 1, 3);
        addDirectedEdge(graph, 2, 2);
        addDirectedEdge(graph, 2, 4);
        addDirectedEdge(graph, 2, 4);
        addDirectedEdge(graph, 3, 1);
        addDirectedEdge(graph, 3, 2);
        addDirectedEdge(graph, 3, 5);
        addDirectedEdge(graph, 4, 3);
        addDirectedEdge(graph, 4, 6);
        addDirectedEdge(graph, 5, 6);
        addDirectedEdge(graph, 6, 3);
        EulerianPathDirectedEdgesAdjacencyList solver = new EulerianPathDirectedEdgesAdjacencyList(graph);

        System.out.println(Arrays.toString(solver.getEulerianPath())); // [1,3, 5, 6, 3, 2, 4, 3, 1, 2, 2, 4, 6]
    }
}

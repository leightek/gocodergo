package leightek.graphtheory;

import java.util.*;

/**
 * Tarjan's Strongly Connected Components algorithm: find SCCs using adjacency list and stack and dfs.
 * Time complexity: O(V+E)
 */
public class TarjanSccAdjacencyList {

    private int n;
    private List<List<Integer>> graph;

    private boolean solved;
    private int sccCount, id;
    private boolean[] visited; // visit status array, index as the node id
    private int[] ids;  // node id array, initialized as -1
    private int[] low;  // low-link value of the node, index as the node id
    private int[] sccs; // the array of Strongly Connected Components, index as the node id
    private Deque<Integer> stack;

    private static final int UNVISITED = -1;

    public TarjanSccAdjacencyList(List<List<Integer>> graph) {
        if (graph == null)
            throw new IllegalArgumentException("Graph cannot be null!");

        n = graph.size();
        this.graph = graph;
    }

    /**
     * Get Strongly Connected Component count
     * @return the number of SCCs
     */
    public int getSccCount() {
        if (!solved)
            findSccs();
        return sccCount;
    }

    /**
     * Get the number of nodes
     * @return the number f nodes
     */
    public int getNumberOfNodes() {
        return n;
    }

    /**
     * Get Strongly Connected Component
     * @return the array of SCCs, index as the node id
     */
    public int[] getSccs() {
        if (!solved)
            findSccs();
        return sccs;
    }

    /**
     * Run the algorithm to find SCCs
     */
    public void findSccs() {
        if (!solved) {
            ids = new int[n];
            low = new int[n];
            sccs = new int[n];
            visited = new boolean[n];
            stack = new ArrayDeque<>();
            Arrays.fill(ids, UNVISITED);

            for (int i = 0; i < n; i++) {
                if (ids[i] == UNVISITED)
                    dfs(i);
            }

            solved = true;
        }
    }

    public static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        return graph;
    }

    public static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
    }

    public static void printSccs(int n, int sccCount, int[] sccs) {
        Map<Integer, List<Integer>> sccMap = new HashMap<>();

        if (sccs != null && sccs.length > 0) {
            for (int i = 0; i < n; i++) {
                if (!sccMap.containsKey(sccs[i]))
                    sccMap.put(sccs[i], new ArrayList<>());
                sccMap.get(sccs[i]).add(i);
            }

        }

        System.out.printf("Number of Strongly Connected Components: %d\n", sccCount);
        for (List<Integer> scc : sccMap.values()) {
            System.out.println("Nodes: " + scc + " form a Strongly Connected Component.");
        }
    }

    private void dfs(int at) {
        ids[at] = low[at] = id++;
        stack.push(at);
        visited[at] = true;

        for (int to : graph.get(at)) {
            if (ids[to] == UNVISITED) {
                dfs(to);
                //low[at] = Math.min(low[at], low[to]);
            }
            if (visited[to]) {
                low[at] = Math.min(low[at], low[to]);
            }
        }

        // empty the stack until back to root
        if (ids[at] == low[at]) {
            for (int node = stack.pop();; node = stack.pop()) {
                visited[node] = false;
                sccs[node] = sccCount;
                if (node == at)
                    break;
            }
            sccCount++;
        }
    }

    public static void main(String[] args) {
        int n = 8;

        List<List<Integer>> graph = createGraph(n);
        addEdge(graph, 6, 0);
        addEdge(graph, 6, 2);
        addEdge(graph, 3, 4);
        addEdge(graph, 6, 4);
        addEdge(graph, 2, 0);
        addEdge(graph, 0, 1);
        addEdge(graph, 4, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 3, 7);
        addEdge(graph, 7, 5);
        addEdge(graph, 1, 2);
        addEdge(graph, 7, 3);
        addEdge(graph, 5, 0);

        TarjanSccAdjacencyList tarjanSccAdjacencyList = new TarjanSccAdjacencyList(graph);
        printSccs(n, tarjanSccAdjacencyList.getSccCount(), tarjanSccAdjacencyList.getSccs());
    }
}

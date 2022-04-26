package leightek.graphtheory;

import java.util.ArrayList;
import java.util.List;

/**
 * Bridges and Articulation Points algorithm: finds all the bridges on an undirected graph.
 */
public class BridgesAdjacencyList {

    private int n;   // the number of nodes
    private int id;
    private int[] low, ids;
    private boolean solved;
    private boolean[] visited;
    private List<List<Integer>> graph;
    private List<Integer> bridges;

    public BridgesAdjacencyList(List<List<Integer>> graph, int n) {
        if (graph == null || n <= 0 || graph.size() != n) {
            throw new IllegalArgumentException();
        }

        this.graph = graph;
        this.n = n;
    }

    /**
     * Run Bridges and Articulation Points Algorithm to find the bridges, each is formed by a pair of nodes.
     * The pair has even length and index (2*i, 2*i+1).
     * @return the bridges
     */
    public List<Integer> findBridges() {
        if (solved) {
            return bridges;
        }

        id = 0;
        low = new int[n];   // low link values
        ids = new int[n];   // nodes ids
        visited = new boolean[n];

        bridges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
           if (!visited[i]) {
               dfs(i, -1, bridges);
           }
        }

        solved = true;

        return bridges;
    }

    private void dfs(int at, int parent, List<Integer> bridges) {
        visited[at] = true;
        low[at] = ids[at] = ++id;

        for (Integer to : graph.get(at)) {
            if (to == parent)
                continue;
            if (!visited[to]) {
                dfs(to, at, bridges);
                low[at] = Math.min(low[at], low[to]);
                if (ids[at] < low[to]) {
                    bridges.add(at);
                    bridges.add(to);
                }
            } else {
                low[at] = Math.min(low[at], ids[to]);
            }
        }
    }

    public static List<List<Integer>> createGraph(int n) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        return graph;
    }

    public static void addEdge(List<List<Integer>> graph, int from, int to) {
        graph.get(from).add(to);
        graph.get(to).add(from);
    }

    public void printBridges(List<Integer> bridges) {
        if (bridges == null || bridges.size() == 0) {
            System.out.println("No bridge in this undirected graph.");
        }

        for (int i = 0; i < this.bridges.size() / 2; i++) {
            int node1 = this.bridges.get(2 * i);
            int node2 = this.bridges.get(2 * i +1);
            System.out.printf("Bridge between nodes: %d and %d\n", node1, node2);
        }
    }

    public static void main(String[] args) {

        int n = 9;
        List<List<Integer>> graph = createGraph(n);

        addEdge(graph, 0, 1);
        addEdge(graph, 0, 2);
        addEdge(graph, 1, 2);
        addEdge(graph, 2, 3);
        addEdge(graph, 3, 4);
        addEdge(graph, 2, 5);
        addEdge(graph, 5, 6);
        addEdge(graph, 6, 7);
        addEdge(graph, 7, 8);
        addEdge(graph, 8, 5);

        BridgesAdjacencyList solver = new BridgesAdjacencyList(graph, n);
        List<Integer> bridges = solver.findBridges();
        solver.printBridges(bridges);
    }
}

package leightek.graphtheory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Lazy version of Prim's Minimum Spanning Tree algorithm, using priority queue, time complexity O(ElogE)
 */
public class LazyPrimsMSTAdjacencyList {

    static class Edge implements Comparable<Edge> {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o) {
            return cost - o.cost;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Edge{");
            sb.append("from=").append(from);
            sb.append(", to=").append(to);
            sb.append(", cost=").append(cost);
            sb.append('}');
            return sb.toString();
        }
    }

    private final int n;
    private final List<List<Edge>> graph;

    private boolean solved;
    private boolean mstExists;
    private boolean[] visited;
    private PriorityQueue<Edge> pq;

    private long minCostSum;
    private Edge[] mstEdges;

    public LazyPrimsMSTAdjacencyList(List<List<Edge>> graph) {
        if (graph == null || graph.isEmpty())
            throw new IllegalArgumentException();

        this.n = graph.size();
        this.graph = graph;
    }

    /**
     * Get the edges in the minimum spanning tree
     * @return the edges if MST exists, or else null
     */
    public Edge[] getMST() {
        solve();
        return mstExists ? mstEdges : null;
    }

    /**
     * Get the cost summary in the minimum spanning tree
     * @return cost summary if MST exists, or else null
     */
    public Long getMSTCost() {
        solve();
        return mstExists ? minCostSum : null;
    }

    // Compute the minimum spanning tree and its cost
    private void solve() {
        if (solved)
            return;

        int m = n - 1, edgeCount = 0;
        pq = new PriorityQueue<>();
        visited = new boolean[n];
        mstEdges = new Edge[m];

        addEdges(0);

        while (!pq.isEmpty() && edgeCount != m) {
            Edge edge = pq.poll();
            int nodeIndex = edge.to;

            if (visited[nodeIndex])
                continue;

            mstEdges[edgeCount++] = edge;
            minCostSum += edge.cost;

            addEdges(nodeIndex);
        }

        mstExists = (edgeCount == m);
        solved = true;
    }

    private void addEdges(int nodeIndex) {
        visited[nodeIndex] = true;

        List<Edge> edges = graph.get(nodeIndex);
        for (Edge e : edges)
            if (!visited[e.to])
                pq.offer(e);
    }

    static List<List<Edge>> createEmptyGraph(int n) {
        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }

        return g;
    }

    static void addDirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        g.get(from).add(new Edge(from, to, cost));
    }

    static void addUndirectedEdge(List<List<Edge>> g, int from, int to, int cost) {
        addDirectedEdge(g, from, to, cost);
        addDirectedEdge(g, to, from, cost);
    }

    public static void main(String[] args) {
        int n = 8;
        List<List<Edge>> g = createEmptyGraph(n);

        addDirectedEdge(g, 0, 1, 10);
        addDirectedEdge(g, 0, 2, 1);
        addDirectedEdge(g, 0, 3, 4);

        addDirectedEdge(g, 2, 1, 3);
        addDirectedEdge(g, 2, 5, 8);
        addDirectedEdge(g, 2, 3, 2);
        addDirectedEdge(g, 2, 0, 1);

        addDirectedEdge(g, 3, 2, 2);
        addDirectedEdge(g, 3, 5, 2);
        addDirectedEdge(g, 3, 6, 7);
        addDirectedEdge(g, 3, 0, 4);

        addDirectedEdge(g, 5, 2, 8);
        addDirectedEdge(g, 5, 4, 1);
        addDirectedEdge(g, 5, 7, 9);
        addDirectedEdge(g, 5, 6, 6);
        addDirectedEdge(g, 5, 3, 2);

        addDirectedEdge(g, 4, 1, 0);
        addDirectedEdge(g, 4, 5, 1);
        addDirectedEdge(g, 4, 7, 8);

        addDirectedEdge(g, 1, 0, 10);
        addDirectedEdge(g, 1, 2, 3);
        addDirectedEdge(g, 1, 4, 0);

        addDirectedEdge(g, 6, 3, 7);
        addDirectedEdge(g, 6, 5, 6);
        addDirectedEdge(g, 6, 7, 12);

        addDirectedEdge(g, 7, 4, 8);
        addDirectedEdge(g, 7, 5, 9);
        addDirectedEdge(g, 7, 6, 12);

        LazyPrimsMSTAdjacencyList solver = new LazyPrimsMSTAdjacencyList(g);
        Long cost = solver.getMSTCost();

        if (cost == null) {
            System.out.println("No MST does not exist");
        } else {
            System.out.println("MST cost: " + cost); // 20
            System.out.println("MST: " + Arrays.toString(solver.getMST())); // 0->2->3->5-> 4->1,4->7,6
        }
    }
}

package leightek.graphtheory;

import leightek.datastructure.pq.MinIndexedDHeap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Eager version of Prim's Minimum Spanning Tree algorithm, using indexed priority queue, time complexity O(ElogV)
 */
public class EagerPrimsMSTAdjacencyList {

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
    private MinIndexedDHeap<Edge> ipq;

    private long minCostSum;
    private Edge[] mstEdges;

    public EagerPrimsMSTAdjacencyList(List<List<Edge>> graph) {
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
    public Long getMstCost() {
        solve();
        return mstExists? minCostSum : null;
    }

    // Compute the minimum spanning tree and its cost
    private void solve() {
        if (solved)
            return;

        int m = n - 1, edgeCount = 0;
        visited = new boolean[n];
        mstEdges = new Edge[m];

        int degree = (int) Math.ceil(Math.log(n) / Math.log(2));
        ipq = new MinIndexedDHeap<>(Math.max(2, degree), n);
        relaxEdgesAtNode(0);

        while (!ipq.isEmpty() && edgeCount != m) {
            int destNodeIndex = ipq.peekMinKeyIndex();
            Edge edge = ipq.pollMinValue();

            mstEdges[edgeCount++] = edge;
            minCostSum += edge.cost;

            relaxEdgesAtNode(destNodeIndex);
        }

        mstExists = (edgeCount == m);

        solved = true;
    }

    private void relaxEdgesAtNode(int currentNodeIndex) {
        visited[currentNodeIndex] = true;
        List<Edge> edges = graph.get(currentNodeIndex);

        for (Edge edge: edges) {
            int destNodeIndex = edge.to;

            if (visited[destNodeIndex])
                continue;

            if (!ipq.contains(destNodeIndex))
                ipq.insert(destNodeIndex, edge);
            else
                ipq.decrease(destNodeIndex, edge); // try to choose the cheapest edge for the destination node of the
                                                   // current edge
        }
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
        int n = 7;
        List<List<Edge>> g = createEmptyGraph(n);

        addDirectedEdge(g, 0, 2, 0);
        addDirectedEdge(g, 0, 5, 7);
        addDirectedEdge(g, 0, 3, 5);
        addDirectedEdge(g, 0, 1, 9);

        addDirectedEdge(g, 2, 0, 0);
        addDirectedEdge(g, 2, 5, 6);

        addDirectedEdge(g, 3, 0, 5);
        addDirectedEdge(g, 3, 1, -2);
        addDirectedEdge(g, 3, 6, 3);
        addDirectedEdge(g, 3, 5, 2);

        addDirectedEdge(g, 1, 0, 9);
        addDirectedEdge(g, 1, 3, -2);
        addDirectedEdge(g, 1, 6, 4);
        addDirectedEdge(g, 1, 4, 3);

        addDirectedEdge(g, 5, 2, 6);
        addDirectedEdge(g, 5, 0, 7);
        addDirectedEdge(g, 5, 3, 2);
        addDirectedEdge(g, 5, 6, 1);

        addDirectedEdge(g, 6, 5, 1);
        addDirectedEdge(g, 6, 3, 3);
        addDirectedEdge(g, 6, 1, 4);
        addDirectedEdge(g, 6, 4, 6);

        addDirectedEdge(g, 4, 1, 3);
        addDirectedEdge(g, 4, 6, 6);

        EagerPrimsMSTAdjacencyList solver = new EagerPrimsMSTAdjacencyList(g);
        Long minCost = solver.getMstCost();

        if (minCost == null)
            System.out.println("No MST exists");
        else {
            System.out.println("MST cost: " + minCost);
            System.out.println("MST edges: " + Arrays.toString(solver.getMST()));
        }
    }
}

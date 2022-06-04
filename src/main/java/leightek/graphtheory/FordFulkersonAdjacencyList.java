package leightek.graphtheory;

import java.util.List;

/**
 * Implementation of Ford Fulkerson algorithm, using DFS to find augmenting paths, time complexity O(fE), f - max flow
 */

public class FordFulkersonAdjacencyList extends NetworkFlowBase {

    public FordFulkersonAdjacencyList(int n, int s, int t) {
        super(n, s, t);
    }

    @Override
    public void solve() {
        for (long f = dfs(s, INF); f != 0; f = dfs(s, INF)) {
            markAllNodesAsUnvisited();
            maxFlow += f;
        }

        for (int i = 0; i < n; i++) {
            if (visited(i))
                minCut[i] = true;
        }
    }

    private long dfs(int node, long flow) {
        if (node == t)
            return flow;

        List<Edge> edges = graph[node];
        visit(node);

        for (Edge edge : edges) {
            long recap = edge.remainingCapacity();
            if (recap > 0 && !visited(edge.to)) {
                long bottleneck = dfs(edge.to, Math.min(flow, recap));

                if (bottleneck > 0) {
                    edge.augment(bottleneck);
                    return bottleneck;
                }
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        int n = 12;
        int s = n - 2;
        int t = n - 1;

        FordFulkersonAdjacencyList solver = new FordFulkersonAdjacencyList(n, s, t);

        solver.addEdge(s, 1, 2);
        solver.addEdge(s, 2, 1);
        solver.addEdge(s, 0, 7);

        solver.addEdge(0, 3, 2);
        solver.addEdge(0, 4, 4);

        solver.addEdge(1, 4, 5);
        solver.addEdge(1, 5, 6);

        solver.addEdge(2, 3, 4);
        solver.addEdge(2, 7, 8);

        solver.addEdge(3, 6, 7);
        solver.addEdge(3, 7, 1);

        solver.addEdge(4, 5, 8);
        solver.addEdge(4, 8, 3);
        solver.addEdge(4, 6, 3);

        solver.addEdge(5, 8, 3);

        solver.addEdge(6, t, 1);
        solver.addEdge(7, t, 3);
        solver.addEdge(8, t, 4);

        System.out.println("maxFlow = " + solver.getMaxFlow());

        List<Edge>[] g = solver.getGraph();
        for (List<Edge> edges : g) {
            for (Edge edge : edges) {
                if (edge.to == s || edge.from == t)
                    continue;
                if (edge.from == s || edge.to == t || edge.from < edge.to)
                    System.out.println(edge.toString(s, t));
            }
        }

    }
}

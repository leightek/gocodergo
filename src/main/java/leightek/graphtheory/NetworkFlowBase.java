package leightek.graphtheory;

import java.util.ArrayList;
import java.util.List;

public abstract class NetworkFlowBase {
    protected static final long INF = Long.MAX_VALUE / 2;

    public static class Edge {
        public int from, to;
        public Edge residual;
        public long flow, cost;
        public final long capacity, originalCost;

        public Edge(int from, int to, long capacity) {
            this(from, to, capacity, 0);
        }

        public Edge(int from, int to, long capacity, long cost) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.originalCost = this.cost = cost;
        }

        public boolean isResidual() {
            return capacity == 0;
        }

        public long remainingCapacity() {
            return capacity - flow;
        }

        public void augment(long bottleneck) {
            flow += bottleneck;
            residual.flow -= bottleneck;
        }

        public String toString(int s, int t) {
            String u = (from == s) ? "s" : ((from == t) ? "t" : String.valueOf(from));
            String v = (to == s) ? "s" : ((to == t) ? "t" : String.valueOf(to));
            return String.format("Edge %s -> %s | flow = %d | capacity = %d | is residual: %s", u, v, flow, capacity,
                    isResidual());
        }
    }

    protected final int n, s, t;  // n - number of nodes, s - source, t - sink
    protected long maxFlow;
    protected long minCost;
    protected boolean[] minCut;
    protected List<Edge>[] graph;

    private int visitedToken = 1; // Incremented when marking all nodes as unvisited
    private int[] visited;
    private boolean solved;

    /**
     * Create an instance of a flow network base
     * @param n the number of nodes
     * @param s the index of source node, 0 <= s < n
     * @param t the index of sink node, 0 <= t < n, t != s
     */
    public NetworkFlowBase(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        initializeGraph();
        minCut = new boolean[n];
        visited = new int[n];
    }

    /**
     * Add a directed edge to the flow graph
     * @param from the index of start node
     * @param to the index of end node
     * @param capacity the capacity of the edge
     */
    public void addEdge(int from, int to, long capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity < 0");
        Edge e1 = new Edge(from, to, capacity);
        Edge e2 = new Edge(to, from, 0);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
    }

    public void addEdge(int from, int to, long capacity, long cost) {
        Edge e1 = new Edge(from, to, capacity, cost);
        Edge e2 = new Edge(to, from, 0, -cost);
        e1.residual = e2;
        e2.residual = e1;
        graph[from].add(e1);
        graph[to].add(e2);
    }

    /**
     * Mark node i as visited
     * @param i the index of the node
     */
    public void visit(int i) {
        visited[i] = visitedToken;
    }

    /**
     * Check node i visited or not
     * @param i the index of the node
     */
    public boolean visited(int i) {
        return visited[i] == visitedToken;
    }

    /**
     * Reset all nodes as unvisited. Especially useful to do between interation of finding augmenting paths, O(1)
     */
    public void markAllNodesAsUnvisited() {
        visitedToken++;
    }

    /**
     * Get the graph after execution
     * @return the graph
     */
    public List<Edge>[] getGraph() {
        execute();
        return graph;
    }

    /**
     * Get the maximu flow from the source to the sink
     * @return maxFlow
     */
    public long getMaxFlow() {
        execute();
        return maxFlow;
    }

    /**
     * Get the minimum cost from the source to the sink
     * @return minCost
     */
    public long getMinCost() {
        execute();
        return minCost;
    }

    /**
     * Get the min cut of the flow network, in which left side nodes of the cut with the source are marked as true,
     * right side nodes of the cut with the sink are marked as false.
     * @return minCut
     */
    public boolean[] getMinCut() {
        execute();
        return minCut;
    }

    @SuppressWarnings("unchecked")
    private void initializeGraph() {
        graph = new List[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<Edge>();
    }

    // execution method
    private void execute() {
        if (solved)
            return;

        solved = true;
        solve();
    }

    // method to implement network flow solution
    public abstract void solve();
}

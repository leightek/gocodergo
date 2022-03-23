package leightek.graphtheory;

import leightek.datastructure.pq.MinIndexedDHeap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An implementation of Dijkstra's shortest path algorithm from a start node to a specific ending node.
 */
public class DijkstrasShortestPathWithDHeap {

    // An edge class to represent a directed edge between two nodes with a certain cost
    public static class Edge {
        int to;
        double cost;

        public Edge(int to, double cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    private final int n; // the number of nodes in the graph

    private int edgeCount;
    private double[] dist; // an array to the minimum distance to each node
    private Integer[] prev;
    private List<List<Edge>> graph;

    public DijkstrasShortestPathWithDHeap(int n) {
        this.n = n;
        createEmptyGraph();
    }

    /**
     * Constrct an empty graph with n nodes with the source and sink nodes.
     */
    private void createEmptyGraph() {
        graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
    }

    /**
     * Adds a directed edge to the graph
     * @param from   the index of the start node
     * @param to     the index of the end node
     * @param cost   the cost, can't be negative
     */
    public void addEdge(int from, int to, int cost) {
        edgeCount++;
        graph.get(from).add(new Edge(to, cost));
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }

    /**
     * Run Dijkstra's algorithm on a directed graph to find the shortest path
     * @param start    start node
     * @param end      end node
     * @return the shortest path if exists, or else Double.POSITIVE_INFINITY
     */
    public double dijkstra(int start, int end) {

        int degree = edgeCount / n;
        MinIndexedDHeap<Double> ipq = new MinIndexedDHeap<>(degree, n); // keep an Indexed Priority Queue of the next
                                                                        // most promising node to visit
        ipq.insert(start, 0.0);

        dist = new double[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0.0;

        boolean[] visited = new boolean[n];
        prev = new Integer[n];

        while (!ipq.isEmpty()) {
            int nodeId = ipq.peekMinKeyIndex();
            visited[nodeId] = true;
            double minValue = ipq.pollMinValue();

            if (minValue > dist[nodeId])  // compare with existing path
                continue;

            for (Edge edge : graph.get(nodeId)) {
                if (visited[edge.to])  // already visited
                    continue;

                double newDist = dist[nodeId] + edge.cost;
                if (newDist < dist[edge.to]) { // find better path
                    prev[edge.to] = nodeId;
                    dist[edge.to] = newDist;
                    if (!ipq.contains(edge.to))
                        ipq.insert(edge.to, newDist); // insert the node with cost into the PQ for the first time
                    else
                        ipq.decrease(edge.to, newDist); // update the node with decreased cost
                }
            }
            if (nodeId == end)
                return dist[end]; // return the end node early as Dijkstra's is greedy and no negative edge weights
        }

        return Double.POSITIVE_INFINITY; // the end node is unreachable
    }

    /**
     * Reconstructs the shortest path from start node to end node
     * @param start   start node
     * @param end     end node
     * @return an array of node indexes of the shortest path from start to end, or else empty array
     */
    public List<Integer> reconstructPath(int start, int end) {
        if (end < 0 || end >= n || start < 0 || start >= n)
            throw new IllegalArgumentException("Invalid node index");

        List<Integer> path = new ArrayList<>();
        double dist = dijkstra(start, end);
        if (dist == Double.POSITIVE_INFINITY)
            return path;

        for (Integer at = end; at != null; at = prev[at])
            path.add(at);
        Collections.reverse(path);
        return path;
    }

}

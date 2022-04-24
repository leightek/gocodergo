package leightek.graphtheory;

import java.util.Arrays;

/**
 * An implementation of the Bellman-Ford algorithm, which finds the shortest path between a starting node and all other
 * nodes in the graph and also detects negative cycles.
 */
public class BellmanFordEdgeList {

    // A directed edge
    public static class Edge {
        double cost;
        int from, to;

        public Edge(int from, int to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    /**
     * Run Bellman Ford algorithm twice
     * @param edges  the edge list of the directed graph
     * @param V  the number of vertices
     * @param start  the id of the starting nocde
     * @return an array of shortest path between the node and started node
     */
    public static double[] bellmanFord(Edge[] edges, int V, int start) {
        double[] dist = new double[V];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start] = 0;

        boolean relaxedAnEdge = true;

        // Run Bellman Ford algorithm at the first time, apply relaxation for the edges
        for (int v = 0; v < V - 1 && relaxedAnEdge; v++) {
            relaxedAnEdge = false;
            for (Edge edge : edges) {
                if (dist[edge.from] + edge.cost < dist[edge.to]) {
                    dist[edge.to] = dist[edge.from] + edge.cost;
                    relaxedAnEdge = true;
                }
            }
        }

        // Run Bellman Ford algorithm at the second time to detect negative cycle if a better path can be found
        relaxedAnEdge = true;
        for (int v = 0; v < V - 1 && relaxedAnEdge; v++) {
            relaxedAnEdge = false;
            for (Edge edge : edges) {
                if (dist[edge.from] + edge.cost < dist[edge.to]) {
                    dist[edge.to] = Double.NEGATIVE_INFINITY;
                    relaxedAnEdge = true;
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        int E = 13, V = 11, start = 0;
        Edge[] edges = new Edge[E];
        edges[0] = new Edge(0, 1, 5);
        edges[1] = new Edge(1, 2, 20);
        edges[2] = new Edge(1, 5, 30);
        edges[3] = new Edge(1, 6, 60);
        edges[4] = new Edge(2, 3, 10);
        edges[5] = new Edge(2, 4, 75);
        edges[6] = new Edge(3, 2, -15);
        edges[7] = new Edge(5, 4, 25);
        edges[8] = new Edge(5, 6, 5);
        edges[9] = new Edge(5, 8, 50);
        edges[10] = new Edge(4, 9, 100);
        edges[11] = new Edge(6, 7, -50);
        edges[12] = new Edge(7, 8, -10);

        double[] d = bellmanFord(edges, V, start);
        for (int i = 0; i < V; i++) {
            System.out.printf("The cost from node %d to node %d is %.2f\n", start, i, d[i]);
        }

    }
}

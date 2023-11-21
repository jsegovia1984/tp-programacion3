package imp;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
    private static final int INFINITY = Integer.MAX_VALUE;
    public DijkstraAlgorithm() {
    }
    public int[][] dijkstra(Graph graph, ArrayList<Integer> distributionCenters) {
        int totalNodes = graph.getTotalNodes();
        int[][] distances = new int[distributionCenters.size()][totalNodes];

        int count = 0;

        for (Integer distCenter : distributionCenters) {
            distances[count] = dijkstraUtil(graph, distCenter);
            count++;
        }

        return distances;
    }

    private int[] dijkstraUtil(Graph graph, int source) {
        int totalNodes = graph.getTotalNodes();
        int[] dist = new int[totalNodes];
        boolean[] visited = new boolean[totalNodes];
        // Initialize distances and visited array
        for (int count = 0; count < totalNodes; count++) {
            dist[count] = INFINITY;
            visited[count] = false;
        }
        dist[source] = 0;

        // Use PriorityQueue to store nodes with distance-based priority
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(dist[a], dist[b]));
        priorityQueue.offer(source); // cambie offer x insert

        while (!priorityQueue.isEmpty()) {
            int u = priorityQueue.poll(); //cambie poll x remove
            visited[u] = true;
            // Explore neighbors of u
            for (int v = 0; v < totalNodes; v++) {
                if (!visited[v] && graph.getCost(u, v) != INFINITY && dist[u] != INFINITY &&
                        dist[u] + graph.getCost(u, v) < dist[v]) {
                    // Update distance and enqueue v if a shorter path is found
                    dist[v] = dist[u] + graph.getCost(u, v);
                    priorityQueue.offer(v);
                }
            }
        }
        return dist;
    }
}




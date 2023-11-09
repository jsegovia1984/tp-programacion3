package imp;

import java.util.*;

public class DijkstraAlgorithm {
    private static final int INFINITY = Integer.MAX_VALUE; // Valor infinito para representar la distancia entre nodos

    public int[][] dijkstra(Graph graph, List<Integer> distributionCenters) {
        int totalNodes = graph.getTotalNodes();
        int[][] distances = new int[distributionCenters.size()][totalNodes];
        int count = 0;

        for (int distCenter : distributionCenters) {
            int distCenterIdx = 0;
            for (int i = 0; i < totalNodes; i++) {
                distances[count][i] = dijkstraUtil(graph, distCenter, i);
            }
            count++;
        }

        return distances;
    }

    private int dijkstraUtil(Graph graph, int source, int destination) {
        int totalNodes = graph.getTotalNodes();
        int[] dist = new int[totalNodes];
        boolean[] visited = new boolean[totalNodes];

        for (int i = 0; i < totalNodes; i++) {
            dist[i] = INFINITY;
            visited[i] = false;
        }

        dist[source] = 0;

        for (int count = 0; count < totalNodes - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;

            for (int v = 0; v < totalNodes; v++) {
                if (!visited[v] && graph.getCost(u, v) != INFINITY && dist[u] != INFINITY &&
                        dist[u] + graph.getCost(u, v) < dist[v]) {
                    dist[v] = dist[u] + graph.getCost(u, v);
                }
            }
        }

        return dist[destination];
    }

    private int minDistance(int[] dist, boolean[] visited) {
        int min = INFINITY, minIndex = -1;
        for (int v = 0; v < dist.length; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}

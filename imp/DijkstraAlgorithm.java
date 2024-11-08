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
        // Inicializa los arreglos de distancias y visitados
        for (int count = 0; count < totalNodes; count++) {
            dist[count] = INFINITY;
            visited[count] = false;
        }
        dist[source] = 0;

        //usa cola de prioridad para almacenar nodos con prioridad basada en la distancia
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>((a, b) -> Integer.compare(dist[a], dist[b]));
        priorityQueue.offer(source); // cambie offer x insert

        while (!priorityQueue.isEmpty()) {
            int u = priorityQueue.poll(); //cambie poll x remove
            visited[u] = true;
            // expande los vecinos de u
            for (int v = 0; v < totalNodes; v++) {
                if (!visited[v] && graph.getCost(u, v) != INFINITY && dist[u] != INFINITY &&
                        dist[u] + graph.getCost(u, v) < dist[v]) {
                    // actualiza la distancia y encola v si se encuentra un camino más corto
                    dist[v] = dist[u] + graph.getCost(u, v);
                    priorityQueue.offer(v);
                }
            }
        }
        return dist;
    }
}




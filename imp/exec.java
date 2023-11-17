package imp;

import java.io.IOException;
import java.util.ArrayList;


public class exec {

    public static void main(String[] args) {

        // We start reading the files and loading it into the file reader structure
        FileReader fileReader = new FileReader();
        try {
            fileReader.loadClients();
            fileReader.loadDistCenters(fileReader.getTotalNodes()); // We obtain the amount of clients
            fileReader.loadRoutes();
            // The load order was wrong, it was previously clients, routes and then the distribution centers. How do you add routes to non-existent distribution centers? Lol
            // Whoever wrote the load sequence is a dummy
        } catch (IOException e) {
            e.printStackTrace();
        }
        // also the file reader creates the graph with clients, and the distribution centers
        Graph filledGraph = fileReader.getGraph();
        ArrayList<Integer> distCenters = fileReader.getDistCenters();
        System.out.println(distCenters);

        
        //Dijkstra -----------------------------------------------------------------------------------------------------

        // we define the dijkstra structure
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        // we assign the result of the calculations into a matrix
        int[][] dijkstra_matrix = dijkstra.dijkstra(filledGraph, distCenters);

        // this is a function to show in console the dijktras matrix
        System.out.println("Matriz Dijkstra ");
        System.out.println("");
        show_Dijkstra(dijkstra_matrix, distCenters.size());

        
        // Greedy ------------------------------------------------------------------------------------------------------

        GreedyAlgorithm greedy = new GreedyAlgorithm();
        int [][] greedy_matrix = greedy.set_Closest_Distrib_Center(filledGraph, dijkstra_matrix, distCenters);

        // this is a function to show in console the greedy matrix
        System.out.println("Matriz Greedy ");
        System.out.println("");
        show_Greedy(greedy_matrix, distCenters.size());

        // in this example we show on console how we can get a full client and use their data
        // Client node = (Client) filledGraph.search_Node(6);
        // System.out.println("Node ID: " + node.ID + " Annual Prod: " + node.annual_Prod);
        // System.out.println("Total Nodes: " + filledGraph.getTotalNodes());

        BacktrackingAlgorithm backtracking = new BacktrackingAlgorithm();
        int [][] backtracking_matrix = backtracking.Backtracking(filledGraph,dijkstra_matrix,greedy_matrix,distCenters);
        show_backtracking(backtracking_matrix,distCenters.size());

    }


    public static void show_Dijkstra(int[][] matrix, int distCenters){

        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }
        System.out.println("");

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length-distCenters; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE) {
                    // Si el valor es el máximo entero, imprime el símbolo de infinito
                    System.out.print(String.format("%4d ", -1));
                } else {
                    // Imprime el valor normal con un ancho de campo especificado
                    System.out.print(String.format("%4d ", matrix[i][j]));
                }
            }
            // Agrega un salto de línea después de cada fila
            System.out.println();
        }
    }

    public static void show_Greedy(int[][] matrix, int distCenters){

        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }
        System.out.println("");
        for (int i = 0; i < matrix.length-distCenters; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE) {
                    // Si el valor es el máximo entero, imprime el símbolo de infinito
                    System.out.print(String.format("%4d ", -1));
                } else {
                    // Imprime el valor normal con un ancho de campo especificado
                    System.out.print(String.format("%4d ", matrix[i][j]));
                }
            }
            // Agrega un salto de línea después de cada fila
            System.out.println();
        }
    }

    public static void show_backtracking(int [][] matrix, int distCenters){

        int[] array = new int[distCenters];
        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }
        System.out.println("");
        for (int i = 0; i < matrix.length-distCenters; i++) {
            int centro = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1){
                    centro = j;
                    array[j] += 1;
                }
            }
            System.out.print("Cliente: " + i + " Centro: " + String.format("%4d ", centro));
            // Agrega un salto de línea después de cada fila
            System.out.println();
        }
        System.out.println("Clientes por cada centro:");
        for (int i= 0 ; i < array.length ; i++){
            System.out.println("Centro " + (i+1) + " " + array[i]);
        }
    }
}

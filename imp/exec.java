package imp;

import java.io.IOException;
import java.util.ArrayList;


public class exec {
    public static void main(String[] args) {

//------FileReader -----------------------------------------------------------------------------------------------------

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

//------Dijkstra -------------------------------------------------------------------------------------------------------

        // we define the dijkstra structure
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        // we assign the result of the calculations into a matrix
        int[][] dijkstra_matrix = dijkstra.dijkstra(filledGraph, distCenters);

        // this is a function to show in console the dijktras matrix
        // System.out.println("Matriz Dijkstra ");
        // System.out.println("");
        // show_Dijkstra(dijkstra_matrix, distCenters.size());

        // int[] distValores = new int[distCenters.size()];
        // int[] disClientes = new int[distCenters.size()];
        // for (int i = 0; i < dijkstra_matrix.length; i++) {
        //     for (int j = 0; j < dijkstra_matrix[0].length-distCenters.size(); j++) {
        //         distValores[i] += dijkstra_matrix[i][j]*10;
        //         disClientes[i] += 1*10 ;
        //     }
        // }
        // for (int i = 0 ; i < distCenters.size(); i++){
        //     int anual = filledGraph.search_Node(50+i).getAnnualProd();
        //     int transporte = filledGraph.search_Node(50+i).getPortCost();
        //     System.out.println(distValores[i] + anual + disClientes[i]*transporte);
        // }

//------Greedy----------------------------------------------------------------------------------------------------------

        GreedyAlgorithm greedy = new GreedyAlgorithm();
        int [][] greedy_matrix = greedy.set_Closest_Distrib_Center(filledGraph, dijkstra_matrix, distCenters);

        // this is a function to show in console the greedy matrix
        // System.out.println("Matriz Greedy ");
        // System.out.println("");
        // show_Greedy(greedy_matrix, distCenters.size());

        // in this example we show on console how we can get a full client and use their data
        // Client node = (Client) filledGraph.search_Node(6);
        // System.out.println("Node ID: " + node.ID + " Annual Prod: " + node.annual_Prod);
        // System.out.println("Total Nodes: " + filledGraph.getTotalNodes());

//------Backtracking ---------------------------------------------------------------------------------------------------

        BacktrackingAlgorithm backtracking = new BacktrackingAlgorithm();
        int [][] backtracking_matrix = backtracking.Backtracking(filledGraph,dijkstra_matrix,greedy_matrix,distCenters);
        show_backtracking(backtracking_matrix,distCenters.size());
    }


    // This function shows the Dijkstra Matrix
    public static void show_Dijkstra(int[][] matrix, int distCenters) {

        // this cycle healp us to see wich column corresponds to wich client
        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }
        System.out.println("");

        // in this cycle we shows the cost of transport the productos to all the distribution centers
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length-distCenters; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE) {
                    // if the value is the maximun integer, shows 0
                    System.out.print(String.format("%4d ", 0));
                } else {
                    System.out.print(String.format("%4d ", matrix[i][j]));
                }
            }
            System.out.println();
        }
    }

    // This function shows the Greedy Matrix
    public static void show_Greedy(int[][] matrix, int distCenters){

        // this cycle healp us to see wich column corresponds to wich client
        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }
        System.out.println("");

        // in this cycle we shows the selecrion of transport the best transport center
        for (int i = 0; i < matrix.length-distCenters; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == Integer.MAX_VALUE) {
                    // if the value is the maximun integer, shows 0
                    System.out.print(String.format("%4d ", -0));
                } else {
                    System.out.print(String.format("%4d ", matrix[i][j]));
                }
            }
            System.out.println();
        }
    }

    // This function shows the backtracking Matrix
    public static void show_backtracking(int [][] matrix, int distCenters){

        // in this cycle we shows the cost of transport the productos to all the distribution centers
        for (int i = 0; i < matrix[0].length-distCenters; i++){
            System.out.print(String.format("%4d ", i));
        }

        int[] array = new int[distCenters];
        System.out.println("");
        for (int i = 0; i < matrix.length; i++) {
            int centro = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1){
                    centro = j;
                    array[j] += 1;
                }
            }
            // System.out.print("Cliente: " + i + " Centro: " + String.format("%4d ", centro));
            // System.out.println();
        }
        // this code shows the assigned center to all the clientes
        // System.out.println("Clientes por cada centro:");
        // for (int i= 0 ; i < array.length ; i++){
        //     System.out.println("Centro " + (i+1) + " " + array[i]);
        // }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(String.format("%4d ", matrix[i][j]));
            }
            System.out.println();
        }
    }
}

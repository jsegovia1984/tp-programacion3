package imp;

import java.io.IOException;
import java.util.ArrayList;


public class exec {

    public static void main(String[] args) {

        long tiempoInicio = System.currentTimeMillis();

// Empezamos a leer los archivos y cargarlos en la estructura del lector de archivos


FileReader fileReader = new FileReader();
        try {
            fileReader.loadClients();
            fileReader.loadDistCenters(fileReader.getTotalNodes()); // Obtenemos la cantidad de clientes
            fileReader.loadRoutes();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // además, el lector de archivos crea el grafo con los clientes y los centros de distribución


        Graph filledGraph = fileReader.getGraph();
        ArrayList<Integer> distCenters = fileReader.getDistCenters();
        //System.out.println(distCenters);

        //Dijkstra -----------------------------------------------------------------------------------------------------

        //se define la estructura de dijkstra
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        // se asignan los resultados de los cálculos en una matriz
        int[][] dijkstra_matrix = dijkstra.dijkstra(filledGraph, distCenters);

        // esta es una función para mostrar en consola la matriz de dijkstra
        //show_Dijkstra(dijkstra_matrix, distCenters.size());
        
        // Backtracking
        
        BacktrackingAlgorithm backtracking = new BacktrackingAlgorithm();
        int [][] backtracking_matrix = backtracking.Backtracking(filledGraph,dijkstra_matrix,distCenters);
        System.out.println("Tiempo de ejecucion: " + (System.currentTimeMillis() - tiempoInicio) + "ms");
        System.out.println("");
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
    	
    	 System.out.print("  ");
    	
        for (int i = 0; i < distCenters; i++){
            System.out.print(String.format("%4d ", matrix.length - distCenters + i));
        }
        System.out.println("");

        for (int i = 0; i < matrix.length-distCenters; i++) {
        	
        	if(i < 10) {	
        		System.out.print(" " + i);
        	}
        	else {
        		System.out.print(i);
        	}
        	
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
    
    public static void show_backtracking(int [][] matrix, int distCenters) {

        int[] array = new int[distCenters];
        for (int i = 0; i < matrix.length; i++) {
            //int centro = 0;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1) {
                    //centro = j;
                    array[j] += 1;
                }
            }
            //System.out.print("Cliente: " + i + " Centro: " + String.format("%4d ", centro));
            // Agrega un salto de línea después de cada fila
            //System.out.println();
        }
        System.out.println("Clientes por cada centro:");

        for (int i = 0; i < array.length; i++) {
            System.out.println();
            System.out.println("Centro " + (matrix.length + i) + ": [" + array[i] + "] Clientes " + get_center_clients(matrix, i));
        }
        System.out.println("");
    }

    public static ArrayList<Integer> get_center_clients(int[][] matrix, int center){
        ArrayList<Integer> clients = new ArrayList<Integer>();

        for (int i= 0 ; i < matrix.length ; i++){
            if (matrix[i][center] == 1) {
                clients.add(i);
            }
        }

        return clients;
    }
}

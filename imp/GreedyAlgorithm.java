package imp;

import java.util.*;

public class GreedyAlgorithm {

	public int[][] set_Closest_Distrib_Center(Graph graph, int[][] Distances ,List<Integer> distributionCenters) {
	
		int totalNodes = graph.getTotalNodes(); // Gets the total amount of nodes in the graph
		
		int[][] Client_Assignations = new int[totalNodes][distributionCenters.size()];  // Creates the Result Matrix with all values set to 0 initially
		
		for(int current_Node = 0; current_Node < totalNodes; current_Node++) { // For each client in the graph
			
			if ( !distributionCenters.contains(current_Node)) { // Checks if the current node is not a distribution centre
				
				int Node_Closest_Center = find_Closest_Distrib_Center(current_Node, distributionCenters, Distances);
				
				Client_Assignations[current_Node][Node_Closest_Center] = 1;
				
			}
			
		}
		
		
		return Client_Assignations;
		
		}
	
		private int find_Closest_Distrib_Center(int Client_ID, List<Integer> distributionCenters, int[][] Distances) { // Returns the closest distribution centre of the given client
			
			int Closest_Distrib = distributionCenters.get(0); // Starts with the first Distribution Centre Assigned
			
			for(int current_Center = 1; current_Center < distributionCenters.size(); current_Center++) { // Compares each remaining Distribution Centres value in the Client_ID row
			
				if(Distances[current_Center][Client_ID] < Distances[Closest_Distrib][Client_ID]) { // If the distance is less then it becomes the closest centress
					
					Closest_Distrib = current_Center;
				}
			
			}
			
			return Closest_Distrib;
				
		}
	
	/* 
	 *  OUTPUT:
	 *  
	 *  CENTRO DE DISTRIBUCION 
	 *						 	| 51 | 52 | 53 | 54 | 55 | 56 |
	 *  CLIENTES
	 * 						1	| 1	   0     0   0     0   0
	 * 						2	| 0    0     0   0     0   1				 
	 *						3	| 0	   1     0   0     0   0
	 *						4	| 0    0     1   0     0   0					   
	 *						5	| 1    0     0   0     0   0							
	 * */
}

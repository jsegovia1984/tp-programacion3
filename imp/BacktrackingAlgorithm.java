package imp;

import java.util.ArrayList;
import imp.Graph.Client;
import imp.Graph.Dist_center;

public class BacktrackingAlgorithm {

	public static class Backtracking_Node {
		
		int Node_Level;
		int[][] Assignment;
		int Total_Annual_Cost;
		int Max_Total_Annual_Cost; // The Max Possible Cost going through this Node, is now used as the comparison for the upper limit
		
		public void calculate_Annual_Cost(Graph graph, int Distances[][], int distributionCenters) { // Calculates the Annual_Cost of the current Client Assigment
			
			int Total_Nodes = Distances[0].length;
			
			for(int i = 0; i < distributionCenters ; i++) { // For each Distribution Center
				
				boolean Dist_Used = false;
				
				//System.out.println("Current Node: " + i);
				
				Dist_center Dist_Center = (Dist_center) graph.search_Node(Total_Nodes - distributionCenters + i); // Retrieve the Dist_Center from Graph
				
				for(int j = 0; j < Total_Nodes - distributionCenters; j++) { // For each Client
					
					if(Assignment[j][i] == 1) { // When The assignment is found
						
						Dist_Used = true; // This means the Distribution Center is in use

						Client Client = (Graph.Client) graph.search_Node(j); // Retrieve the Client from Graph
						
						int Distance_Client_to_Dist = Distances[i][j]; // Obtains the distance between Client to the Distribution Center
						
						int Client_Prod = Client.getAnnualProd(); // Retrieve Annual Production Value from Client
						
						Total_Annual_Cost = Total_Annual_Cost + (Client_Prod * Distance_Client_to_Dist) + (Dist_Center.getPortCost() * Client_Prod);
						
						// The calculation goes is the Following :  Client Annual Production * Unitary Cost of Transport to the Distribution Centre + Unitary Port Transport Cost * Client Production
						
						// This is done to each Client and summed up in the single Total_Annual_Cost Variable
					}
				}
				
				if(Dist_Used == true) {
					
					//System.out.println("Dist_Centre: " + i + " Used Adding cost: " + Dist_Center.Annual_Cost );
					
					Total_Annual_Cost = (Total_Annual_Cost + Dist_Center.Annual_Cost); // Adds the annual maintenance cost of the Distribution Centre, if it is in use

				}

			}
		}
	
		public void calculate_Max_Annual_Cost(Graph graph, int Distances[][]) { // Calculates the maximum cost possible, ignoring Inactive Distribution Centres, similar to the Backpack Problem
			
			Max_Total_Annual_Cost = 0;
			
			int Total_Nodes = Distances[0].length;
				
			for(int Current_Dist_Center = 0; Current_Dist_Center < Assignment[0].length; Current_Dist_Center++) { // For each distribution centre
				
				boolean Dist_Used = false;
				
				Dist_center Dist_Center = (Dist_center) graph.search_Node(Total_Nodes - Assignment[0].length + Current_Dist_Center);
				
				for(int Client_ID = 0; Client_ID < Total_Nodes - Assignment.length; Client_ID++) { 
					
					Client Client = (Graph.Client) graph.search_Node(Client_ID);
					
					int Aux_Value = Integer.MIN_VALUE;
					
					int Max_Dist_Center = -1;
					
					if (Distances[Current_Dist_Center][Client_ID] > Aux_Value && Assignment[Client_ID][Current_Dist_Center] != -1) { // If the Value is higher than the Aux and it is not a unassigned Center
						
						Dist_Used = true;
						
						Aux_Value = Distances[Current_Dist_Center][Client_ID];
						
						Max_Dist_Center = Current_Dist_Center;
						
					}
					
					if(Max_Dist_Center != -1) {
					
						int Distance_Client_to_Dist = Distances[Max_Dist_Center][Client_ID]; // Obtains the distance between Client to the Distribution Center
						
						int Client_Prod = Client.getAnnualProd(); // Retrieve Annual Production Value from Client
						
						Max_Total_Annual_Cost = Max_Total_Annual_Cost + (Client_Prod * Distance_Client_to_Dist) + (Dist_Center.getPortCost() * Client_Prod);
					}
					
				}
				
				if(Dist_Used == true) {
					
					//System.out.println("Dist_Centre: " + i + " Used Adding cost: " + Dist_Center.Annual_Cost );
					
					Max_Total_Annual_Cost = (Max_Total_Annual_Cost + Dist_Center.Annual_Cost); // Adds the annual maintenance cost of the Distribution Centre, if it is in use

				}
				
			}
			
		}
		
		public void add_Dist_Center(int Dist_Center, int Distances[][]) { // Sets a Distribution Centre Column to 0 to be made available for Assignment
			
			//System.out.println("Adding Center: " + (Dist_Center + 50));
			
			for(int Current_Client = 0; Current_Client < Assignment.length; Current_Client++) {
				
					Assignment[Current_Client][Dist_Center] = 0;
					
					next_minDist_center(Current_Client, Dist_Center, Distances);  // Checks if the currently enabled Distribution Centre is better than the previous options
					
				}

			}
		
		private void next_minDist_center(int Client_ID, int Added_Dist_ID, int Distances[][]) { // Finds the next Closest Center for the affected Client
			
			int Aux_Value = Integer.MAX_VALUE;
			
			int Min_Dist_Center = -1;
			
			//System.out.println("Finding min with: " + (Added_Dist_ID + 50));
			
			for(int Current_Dist_Center = 0; Current_Dist_Center < Assignment[0].length; Current_Dist_Center++) { // For each distribution centre
				
				if (Distances[Current_Dist_Center][Client_ID] < Aux_Value && Assignment[Client_ID][Current_Dist_Center] != -1) { // If the Value is lower than the Aux and it is not a unassigned Center
					
					Aux_Value = Distances[Current_Dist_Center][Client_ID];
					
					Min_Dist_Center = Current_Dist_Center;
					
				}
				
				if(Assignment[Client_ID][Current_Dist_Center] == 1 || Assignment[Client_ID][Current_Dist_Center] != -1) {
					
					Assignment[Client_ID][Current_Dist_Center] = 0; // Sets the current distribution centre to not used
					
				}

			}
			
			Assignment[Client_ID][Min_Dist_Center] = 1;
			
		}
		
		public void clear_Annual_Cost() { // Sets cost to 0 to be recalculated
			Total_Annual_Cost = 0;
		}
		
	}
	
	public int[][] Backtracking(Graph graph, int Distances[][], ArrayList<Integer> distributionCenters){
		
		Backtracking_Node Result = new Backtracking_Node(); // Starts as null
		
		Result.Total_Annual_Cost = Integer.MAX_VALUE; // Starts at Infinity
		
		Backtracking_Node Root = create_RootNode(Distances[0].length, distributionCenters.size());// Creates the Root Node Assigment Matrix with no centers assigned
		
		int upper_Limit = Integer.MAX_VALUE; // Sets the upper limit to the Root Annual Cost initially
	
		PriorityQueue Alive_Nodes = new PriorityQueue(); // Create a Min-heap Priority Queue
		
		Alive_Nodes.insert(Root, Root.Total_Annual_Cost); // Insert the Root into the Node
		
		int Iterations = 1; // Remove this later, just checking for times executed
		
		int Trim_Counter = 0; // Remove this later, just counting Trims
		
		while (!Alive_Nodes.isEmpty()) { // When this still has nodes to check
			
			Iterations++; // Counting Iterations
			
			Backtracking_Node Current_Node = Alive_Nodes.remove(); // Obtains the Lowest Annual Cost Node and removes it from the Queue 
			
			/*
			
			System.out.println("---------------------------------------------");
			
			show_Greedy(Current_Node.Assignment, Current_Node.Assignment[0].length);
			
			System.out.println("---------------------------------------------");
			
			*/
			
			//System.out.println("Annual cost of child: " + Current_Node.Total_Annual_Cost);
			
			//System.out.println("Current upper limit: " + upper_Limit);
			
			if(Current_Node.Node_Level < Current_Node.Assignment[0].length  && Current_Node.Max_Total_Annual_Cost < upper_Limit) { // Without this level check the Algorithm blows up , Also decides if it should be Trimmed or Not
				
				//System.out.println("--------------Node not Trimmed!-------------------");
								
				ArrayList<Backtracking_Node> Current_Children = create_Children(graph, Current_Node, Current_Node.Node_Level, Distances, distributionCenters);
				
				for(int i = 0; i < Current_Children.size(); i++) {
					
					Backtracking_Node Current_Child = Current_Children.get(i); // Grab the Children
					
					if(i == 0) { // If the Child is the one where the Distribution Centre is not added, there is no need to clear and recalcualte the Annual Cost / Max_Annual_Cost as those remain the same
												
						Current_Child.clear_Annual_Cost(); // Removes previous Value
						
						Current_Child.calculate_Annual_Cost(graph, Distances, distributionCenters.size()); // Calculates the cost of the child if it is a Removed Centre
						
						Current_Child.calculate_Max_Annual_Cost(graph, Distances); // Calculates the new Max_Annual_Cost taking into account the newly added Distribution Centre
						
						//System.out.println("Node Level: " + Current_Child.Node_Level);

						//System.out.println("Annual cost of child: " + Current_Child.Total_Annual_Cost);
						
						//System.out.println("Current upper limit: " + upper_Limit);

						//System.out.println("---------------Client Assignments of Added Node------------------");
						
						//show_Greedy(Current_Child.Assignment, Current_Child.Assignment[0].length);
						
						//System.out.println("-----------------------------------------------------------");
						
					}
					
					//System.out.println("Max Annual cost of child: " + Current_Child.Max_Total_Annual_Cost);

						if(is_Leaf(Current_Child, distributionCenters.size())) { // Check if it is a Leaf
								
							//show_Greedy(Current_Child.Assignment, Current_Child.Assignment[0].length);
							
							//System.out.println("Node Level: " + Current_Child.Node_Level);
							
							//System.out.println("Annual cost of child: " + Current_Child.Total_Annual_Cost);
							
							if (Current_Child.Total_Annual_Cost < Result.Total_Annual_Cost) { // If it is better than the current Result
								
								Result = Current_Child; // It becomes the result
								
								upper_Limit = Math.min(upper_Limit, Current_Child.Total_Annual_Cost); // Updates the upper Limit
								}
							}
						
						else {
							
							Alive_Nodes.insert(Current_Child, Current_Child.Total_Annual_Cost); // Inserts the Child with its Annual Cost into the Heap
							
							}
						//upper_Limit = Math.min(upper_Limit, Current_Child.Total_Annual_Cost);
						}
				}	
			else {
				
				//System.out.println("--------------Node Trimmed!-----------------------");
				
				Trim_Counter++;
			}
		}
		
		System.out.println("");
		
		System.out.println("--------------------Results-----------------------");
		
		System.out.println("");
		
		System.out.println("Iterations: " + Iterations);
		
		System.out.println("");
		
		System.out.println("Times Trimmed: " + Trim_Counter);
		
		System.out.println("");
		
		System.out.println("The Final Annual Cost is: " + Result.Total_Annual_Cost);
		
		System.out.println("");
		
		System.out.println("-------------Assignment Results--------------------");
		
		System.out.println("");
		
		show_Greedy(Result.Assignment, Result.Assignment[0].length);

		System.out.println("");
		
		return Result.Assignment;
	}
	

	public Backtracking_Node create_CloneNode(Backtracking_Node Parent) { // Literally just copies the Parent Node
		
		Backtracking_Node Copy = new Backtracking_Node();
		
		Copy.Assignment = new int[Parent.Assignment.length][Parent.Assignment[0].length]; // Initializes the Matrix in the copy
		
		for (int i = 0; i < Parent.Assignment.length; i++) {
			
			for (int j = 0; j < Parent.Assignment[0].length; j++) {
				
				Copy.Assignment[i][j] = Parent.Assignment[i][j];  // Copy the values over
			}
		}
		
		Copy.Total_Annual_Cost = Parent.Total_Annual_Cost;
		
		Copy.Max_Total_Annual_Cost = Parent.Max_Total_Annual_Cost;
		
		return Copy;
		
	}
	
	public Backtracking_Node create_RootNode(int Total_Nodes, int Distribution_Centers) { // Creates the initial root "Node"
		
		Backtracking_Node Root = new Backtracking_Node();
		
		int Clients = Total_Nodes - Distribution_Centers; // Gets the amount of Clients
		
		Root.Assignment = new int[Total_Nodes - Distribution_Centers][Distribution_Centers];
		
		for(int i = 0; i <  Distribution_Centers; i++) { // Sets all Centers as Unnasigned, that is -1
			
			for(int j = 0; j < Clients; j++) {
				
					Root.Assignment[j][i] = -1; 
				}
			}
		
		Root.Total_Annual_Cost = Integer.MAX_VALUE; // As it is empty, the cost is Infinite
		
		Root.Node_Level = 0; // The Root Node level starts at 0
		
		Root.Max_Total_Annual_Cost = Integer.MIN_VALUE;
		
		return Root;
	}
	
	public ArrayList<Backtracking_Node> create_Children(Graph graph ,Backtracking_Node Parent, int Dist_Center_Level, int Distances[][], ArrayList<Integer> distributionCenters){
		
		ArrayList<Backtracking_Node> Children = new ArrayList<Backtracking_Node>();
		
		Backtracking_Node Added_Dist_Cent = create_CloneNode(Parent); // This is just a copy of the Parent
		
		Backtracking_Node Non_Added_Dist_Cent = create_CloneNode(Parent); // This starts as just a copy
		
		Added_Dist_Cent.add_Dist_Center(Dist_Center_Level, Distances); // Adds the Distribution Center
		
		//show_Greedy(Added_Dist_Cent.Assignment, distributionCenters.size());
		
		//System.out.println("-------------------------------------------");
		
		//show_Greedy(Non_Added_Dist_Cent.Assignment, distributionCenters.size());
		
		Added_Dist_Cent.clear_Annual_Cost(); // Resets Cost
		
		Added_Dist_Cent.calculate_Annual_Cost(graph, Distances, distributionCenters.size()); // Recalculates Annual Cost
		
		Added_Dist_Cent.Node_Level = Dist_Center_Level + 1;
		
		Non_Added_Dist_Cent.Node_Level = Dist_Center_Level + 1;
		
		Children.add(Added_Dist_Cent);
		
		Children.add(Non_Added_Dist_Cent);
		
		return Children;
	}
	
	public boolean is_Leaf(Backtracking_Node Node, int Distrib_Center) { // Checks if the Node is a Leaf or Not
		
		return Node.Node_Level == Distrib_Center;
	}
	
	// Temp Function, REMOVE LATER 
	
	public static void show_Greedy(int[][] matrix, int distCenters){
    	
   	 System.out.print("  ");
   	
       for (int i = 0; i < distCenters; i++){
           System.out.print(String.format("%4d ", matrix.length + i));
       }
       System.out.println("");

       for (int i = 0; i < matrix.length; i++) {
       	
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
	
	/*
	 * 	NOTES
	 * 
	 * The algorithm without Trimming has an iteration count of 2^8 = 256 times, in accordance to the cost calculation of (Amount of Children) ^ Amount of Levels
	 * 
	 * With the use of Trimming the Iterations are almost halved
	 * 
	 */
}

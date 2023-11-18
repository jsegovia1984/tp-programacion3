package imp;

import java.util.ArrayList;

import imp.Graph.Client;
import imp.Graph.Dist_center;

public class BacktrackingAlgorithm {

	public static class Backtracking_Node {
		
		int Node_Level;
		int[][] Assignment;
		int Total_Annual_Cost;
		
		public void calculate_Annual_Cost(Graph graph, int Distances[][], ArrayList<Integer> distributionCenters) { // Calculates the Annual_Cost of the current Client Assigment
			
			for(int i = 0; i < distributionCenters.size() ; i++) { // For each Client
				
				for(int j = 0; j < Distances[0].length - distributionCenters.size(); j++) { // For each Distribution Center
					
					if(Assignment[j][i] == 1) { // When The assignment is found
						
						Dist_center Dist_Center = (Dist_center) graph.search_Node(distributionCenters.get(i)); // Retrieve the Dist_Center from Graph
						
						Client Client = (Graph.Client) graph.search_Node(i); // Retrieve the Client from Graph
						
						int Distance_Client_to_Dist = Distances[i][j]; // Obtains the distance between Client to the Distribution Center
						
						int Client_Prod = Client.getAnnualProd(); // Retrieve Annual Production Value from Client
						
						Total_Annual_Cost = Total_Annual_Cost + (Client_Prod * Distance_Client_to_Dist) + (Dist_Center.getPortCost() * Client_Prod) + Dist_Center.getAnnualCost();
						
						// The calculation goes is the Following :  Client Annual Production * Unitary Cost of Transport to the Distribution Centre + Unitary Port Transport Cost * Client Production + Dist_Centre Annual Maintenance Cost
						
						// This is done to each Client and summed up in the single Total_Annual_Cost Variable
					}
					
				}
			}
		}
		
		public void remove_Dist_Center(int Dist_Center, int Distances[][]) { // Sets the removed center to -1 in the Assignment Matrix
			
			System.out.println("Removing Center: " + Dist_Center);
			
			for(int Current_Client = 0; Current_Client < Assignment.length; Current_Client++) {
				
				//System.out.println(Assignment[Current_Client][Dist_Center]);
				
				if(Assignment[Current_Client][Dist_Center] == 1) {
					
					//System.out.println("Removing Assignment");
					
					Assignment[Current_Client][Dist_Center] = -1; // Sets it to removed
					
					//System.out.println("Result: " + Assignment[Current_Client][Dist_Center]);
					
					next_minDist_center(Current_Client, Dist_Center, Distances); 
					
				}
				else {
					
					//System.out.println("Setting it to -1");
					
					Assignment[Current_Client][Dist_Center] = -1; // Sets it to removed	
					
					//System.out.println("Result: " + Assignment[Current_Client][Dist_Center]);
				}
				
			}
			
		}
		
		private void next_minDist_center(int Client_ID, int Removed_Dist_ID, int Distances[][]) { // Finds the next Closest Center for the affected Client
			
			int Aux_Value = Integer.MAX_VALUE;
			
			int Min_Dist_Center = 0;
			
			for(int Current_Dist_Center = 0; Current_Dist_Center < Assignment[0].length; Current_Dist_Center++) { // For each distribution center
				
				if (Distances[Current_Dist_Center][Client_ID] < Aux_Value && Assignment[Client_ID][Current_Dist_Center] != -1) { // If the Value is lower than the Aux and it is not a removed Center
					
					Aux_Value = Distances[Current_Dist_Center][Client_ID];
					
					Min_Dist_Center = Current_Dist_Center;
				}
			}
			
			Assignment[Client_ID][Min_Dist_Center] = 1;
			
		}
		
		public void clear_Annual_Cost() { // Sets cost to 0 to be recalculated
			Total_Annual_Cost = 0;
		}
		
	}
	
	public int[][] Backtracking(Graph graph, int Distances[][], int Client_Assignments[][], ArrayList<Integer> distributionCenters){
		
		Backtracking_Node Result = new Backtracking_Node(); // Starts as null
		
		Result.Total_Annual_Cost = Integer.MAX_VALUE;
		
		int Dist_Center_Level = 0; // Starts at the first level
		
		Backtracking_Node Root = new Backtracking_Node();
		
		Root.Assignment = create_RootNode(Distances, Client_Assignments, distributionCenters); // Creates the Root Node Assigment Matrix
		
		Root.calculate_Annual_Cost(graph, Distances, distributionCenters); // Calculate the annual Cost of the Root
		
		Root.Node_Level = Dist_Center_Level;
		
		//int upper_Limit = Root.Total_Annual_Cost; // Sets the upper limit to the Root Annual Cost initially
		
		int upper_Limit = Integer.MAX_VALUE; // Sets the upper limit to the Root Annual Cost initially
	
		PriorityQueue Alive_Nodes = new PriorityQueue(); // Min-heap Priority Queue
		
		Alive_Nodes.insert(Root, Root.Total_Annual_Cost);
		
		while (!Alive_Nodes.isEmpty()) {
			
			Backtracking_Node Current_Node = Alive_Nodes.remove(); // Obtains the Highest Priority Node and removes it from the Queue 
			
			//System.out.println("Current Node:" + Current_Node);
			
			System.out.println("Current Level: " + Current_Node.Node_Level);
			
			//System.out.println("---------------------------------------------");
			
			//show_Greedy(Current_Node.Assignment, Current_Node.Assignment[0].length);
			
			//System.out.println("---------------------------------------------");
			
			
			if(!is_Leaf(Current_Node) && Current_Node.Node_Level < Current_Node.Assignment[0].length) { // If the Current Node is not a Leaf
				
				ArrayList<Backtracking_Node> Current_Children = create_Children(graph, Current_Node, Current_Node.Node_Level, Distances, distributionCenters);
				
				for(int i = 0; i < Current_Children.size(); i++) {
					
					Backtracking_Node Current_Child = Current_Children.get(i);
					
					//System.out.println("Current Child: " + Current_Child);
					
					if(i == 0) { // If it is the cloned Child there is no need to clear and recalcualte the Annual Cost
						
						//System.out.println("Removed Center");
						
						Current_Child.clear_Annual_Cost(); // Removes previous Value
						
						Current_Child.calculate_Annual_Cost(graph, Distances, distributionCenters); // Calculates the cost of the child if it is a removed centre
						
						//System.out.println("---------------Client Assignments of Removed Node------------------");
						
						//show_Greedy(Current_Child.Assignment, Current_Child.Assignment[0].length);
						
						//System.out.println("-----------------------------------------------------------");
					}
					
					/*
					else {
						System.out.println("Non-Removed Center");
					}
					*/
					
					System.out.println("Annual cost of child: " + Current_Child.Total_Annual_Cost);
					
					//System.out.println("Current upper limit: " + upper_Limit);
					
					if(Current_Child.Total_Annual_Cost < upper_Limit) { // Decides if it gets trimmed or not
							
							if (Current_Child.Total_Annual_Cost < Result.Total_Annual_Cost) { // If it is better than the current Result
								
								//System.out.println("New best result");
								
								Result = Current_Child; // It becomes the result
								
								//upper_Limit = Current_Child.Total_Annual_Cost;
							}
							
							//System.out.println("Added to Alives");
							
							Alive_Nodes.insert(Current_Child, Current_Child.Total_Annual_Cost); // Inserts the Child with its Annual Cost into the Heap
							
							//if(Current_Child.Total_Annual_Cost < upper_Limit) { // Updates the upper_Limit with the smaller value
								//upper_Limit = Current_Child.Total_Annual_Cost;
							//}
							
						
					}
					else {
						System.out.println("Node Trimmed");
					}
				}	
					}
				
			else { // If it is a Leaf, Just compare it to the result to see if it is better or not
				
				System.out.println("Leaf Total Annual Cost: " + Current_Node.Total_Annual_Cost);
				
				if (Current_Node.Total_Annual_Cost < Result.Total_Annual_Cost) { // If it is better than the current Result
					
					//System.out.println("New best result from Leaf");
					
					Result = Current_Node; // Assign it as the new Result
					
					if(Current_Node.Total_Annual_Cost < upper_Limit) { // Updates the upper_Limit with the smaller value
						upper_Limit = Current_Node.Total_Annual_Cost;
				}
			
				}
			}
		}
		System.out.println("The Final Annual Cost is:" + Result.Total_Annual_Cost);
		
		return Result.Assignment;
	}
	
	public int[][] create_RootNode(int Distances[][], int Client_Assignments[][], ArrayList<Integer> distributionCenters) { // Creates the initial root "Node"
		
		int[][]  Node = new int[Distances[0].length - distributionCenters.size()][distributionCenters.size()];
		
		for(int i = 0; i < distributionCenters.size(); i++) { 
			
			for(int j = 0; j < Distances[0].length; j++) {
				
				if(Client_Assignments[j][i] == 1) { // If it is assigned to the current center
					Node[j][i] = 1; // Assign it in the new Node
				}
			}
		}
		
		return Node;
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
		
		return Copy;
		
	}
	
	public ArrayList<Backtracking_Node> create_Children(Graph graph ,Backtracking_Node Parent, int Dist_Center_Pending_Removal, int Distances[][], ArrayList<Integer> distributionCenters){
		
		ArrayList<Backtracking_Node> Children = new ArrayList<Backtracking_Node>();
		
		Backtracking_Node Non_Removed_Dist_Cent = create_CloneNode(Parent); // This is just a copy of the Parent
		
		Backtracking_Node Removed_Dist_Cent = create_CloneNode(Parent); // This starts as just a copy
		
		Removed_Dist_Cent.remove_Dist_Center(Dist_Center_Pending_Removal, Distances); // Removes the Distribution Center
		
		Removed_Dist_Cent.clear_Annual_Cost(); // Resets Cost
		
		Removed_Dist_Cent.calculate_Annual_Cost(graph, Distances, distributionCenters); // Recalculates Annual Cost
		
		Removed_Dist_Cent.Node_Level = Dist_Center_Pending_Removal + 1;
		
		Non_Removed_Dist_Cent.Node_Level = Dist_Center_Pending_Removal + 1;
		
		Children.add(Removed_Dist_Cent);
		
		Children.add(Non_Removed_Dist_Cent);
		
		return Children;
	}
	
	public boolean is_Leaf(Backtracking_Node Node) { // Checks if the Node is a Leaf or Not
		
		//System.out.println("---------------------------------------------");
		
		//System.out.println("Leaf Check!");
		
		int Inactive_Centers = 0;
		
		int Dist_Center_Amount = Node.Assignment[0].length;
		
		for(int i = 0; i < Dist_Center_Amount; i++) { // Checks each column
			
			//System.out.println("Current Center: " + Node.Assignment[0][i]);
			
			if(Node.Assignment[0][i] == -1) { // The current distribution Centre is removed
				
				Inactive_Centers++;
			}
		}
		
		return Inactive_Centers == Dist_Center_Amount - 1; // Returns True or False if there is only one Centre left or not
	}
	
	// Temp Function 
	
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
}

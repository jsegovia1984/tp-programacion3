package imp;

import java.util.ArrayList;

import imp.Graph.Client;
import imp.Graph.Dist_center;

public class BacktrackingAlgorithm {

	public static class Backtracking_Node {
		
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
			
			for(int Current_Client = 0; Current_Client < Assignment.length; Current_Client++) {
				
				if(Assignment[Current_Client][Dist_Center] == 1) {
					
					Assignment[Current_Client][Dist_Center] = -1; // Sets it to removed
					
				}
				
				next_minDist_center(Current_Client, Dist_Center, Distances); 
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
		
		Backtracking_Node Root = new Backtracking_Node();
		
		Root.Assignment = create_RootNode(Distances, Client_Assignments, distributionCenters); // Creates the Root Node Assigment Matrix
		
		Root.calculate_Annual_Cost(graph, Distances, distributionCenters); // Calculate the annual Cost of the Root
		
		int upper_Limit = Root.Total_Annual_Cost; // Sets the upper limit to the Root Annual Cost initially
		
		PriorityQueue Alive_Nodes = new PriorityQueue(); // Min-heap Priority Queue
		
		Alive_Nodes.insert(Root, Root.Total_Annual_Cost);
		
		int Dist_Center_Level = 0; // Starts with the first Dist_Center
		
		while (!Alive_Nodes.isEmpty()) {
			
			Backtracking_Node Current_Node = Alive_Nodes.remove(); // Obtains the Highest Priority Node and removes it from the Queue and
			
			if(!is_Leaf(Current_Node)) { // If the Current Node is not a Leaf
				
				ArrayList<Backtracking_Node> Current_Children = create_Children(graph, Current_Node, Dist_Center_Level, Distances, distributionCenters);
				
				for(int i = 0; i < Current_Children.size(); i++) {
					
					Backtracking_Node Current_Child = Current_Children.get(i);
					
					if(Current_Child.Total_Annual_Cost < upper_Limit) { // Decides if it gets trimmed or not
							
							if (Current_Child.Total_Annual_Cost < Result.Total_Annual_Cost) { // If it is better than the current Result
							
								Result = Current_Child; // It becomes the result
							}
							
							Alive_Nodes.insert(Current_Child, Current_Child.Total_Annual_Cost); // Inserts the Child with its Annual Cost into the Heap
							
							if(Current_Child.Total_Annual_Cost < upper_Limit) { // Updates the upper_Limit with the smaller value
								upper_Limit = Current_Child.Total_Annual_Cost;
							}
					}
				}	
					}
				
			else { // If it is a Leaf, Just compare it to the result to see if it is better or not
				
				if (Current_Node.Total_Annual_Cost < Result.Total_Annual_Cost) { // If it is better than the current Result
					
					Result = Current_Node; // Assign it as the new Result
					
					upper_Limit = Current_Node.Total_Annual_Cost; // Update the upper_Limit
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
		
		Children.add(Removed_Dist_Cent);
		
		Children.add(Removed_Dist_Cent);
		
		return Children;
	}
	
	public boolean is_Leaf(Backtracking_Node Node) { // Checks if the Node is a Leaf or Not
		
		int Active_Centers = 0;
		
		for(int i = 0; i < Node.Assignment[0].length; i++) { // Checks each column
		
			if(Node.Assignment[0][i] == 1) { // There is atleast one active centre
				
				Active_Centers++;
			}
		}
		
		return Active_Centers == 1; // Returns True or False if there is only one Centre Left
	}
}

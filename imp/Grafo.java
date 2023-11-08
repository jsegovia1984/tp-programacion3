package imp;

import java.util.*;

public class Grafo {

	public abstract class Node {
		int ID;
		Node nextNode;
		Route nextRoute;

		public abstract void get_Cost();
	}

	class Client extends Node {
		int ID;
		int annual_Prod;

		@Override
		public void get_Cost() {
			// TODO: Implementar
		}
	}

	class Dist_center extends Node { // Somehow merge with Client or Standarize the Client class into a Node Class
		int ID;
		int Port_Cost;
		int Annual_Cost;

		@Override
		public void get_Cost() {
			// TODO: Implementar
		}
	}

	class Route {
		int Uni_Cost;
		Node Destination;
		Route nextRoute;
	}

	Client Origin; // This node is used as an indicator that the Graph is initially empty

	public void InitializeGraph() {

		Origin = null; // It sets the Origin node to null

	}

	public void add_Client(int ID, int annual_Prod) { // Method to add Clients to the Graph

		Client new_Client = new Client();
		new_Client.ID = ID;
		new_Client.annual_Prod = annual_Prod;
		new_Client.nextRoute = null; // Starts without Routes
		new_Client.nextNode = Origin;
		Origin = new_Client;

	}

	public void add_Dist_center(int ID, int Port_Cost, int Annual_Cost) {

		Dist_center new_Center = new Dist_center();
		new_Center.ID = ID;
		new_Center.Port_Cost = Port_Cost;
		new_Center.Annual_Cost = Annual_Cost;

	}

	public void add_Node(Node node) {
		if (node instanceof Client) {
			add_Client(node.ID, ((Client) node).annual_Prod);
		} else if (node instanceof Dist_center) {
			add_Dist_center(node.ID, ((Dist_center) node).Port_Cost, ((Dist_center) node).Annual_Cost);
		}

	}

	// public void add_Route(int ID_Source, int ID_Dest, int Uni_Cost) { // Method
	// to add Routes between a client and
	// // another

	// Client Source = search_Client(ID_Source);
	// Client Destination = search_Client(ID_Dest);
	// // Create an exception when either is null
	// Route new_Route = new Route();
	// new_Route.Uni_Cost = Uni_Cost; // Sets the Unitary Cost of Transport through
	// this Route
	// new_Route.clientDest = Destination; // Sets the Route Destination
	// new_Route.nextRoute = Source.Route; // Sets to the Origins previous Route
	// Source.Route = new_Route; // Becomes the new Origin´s first route

	// }

	public void add_Route_Between_Nodes(int ID_Source, int ID_Dest, int Uni_Cost) { // Method to add Routes between a
																					// client and another

		Node Source = search_Node(ID_Source);
		Node Destination = search_Node(ID_Dest);
		// Create an exception when either is null
		Route new_Route = new Route();
		new_Route.Uni_Cost = Uni_Cost; // Sets the Unitary Cost of Transport through this Route
		new_Route.Destination = Destination; // Sets the Route Destination
		new_Route.nextRoute = Source.nextRoute; // Sets to the Origins previous Route
		Source.nextRoute = new_Route; // Becomes the new Origin´s first route

	}

	public int get_Route_Cost(int ID_Source, int ID_Dest) { // Gets the unitary transport cost between the origin and
															// destination

		Node Source = search_Node(ID_Source); // Searches the Client Node
		Route Aux = Source.nextRoute; // Gets the first route in the Client Node
		while (Aux.Destination.ID != ID_Dest) { // Tries to find the Unitary Cost
			Aux = Aux.nextRoute;
		}
		return Aux.Uni_Cost;

	}

	public List<Node> get_Neighbors(int ID) {

		List<Node> Neighbors = new ArrayList<Node>();
		Node Source = search_Node(ID);
		if (Source != null) {
			Route act_Route = Source.nextRoute;
			while (act_Route != null) {
				Neighbors.add(act_Route.Destination);
				act_Route = act_Route.nextRoute;
			}
		}
		return Neighbors;

	}

	private Node search_Node(int ID) { // Searches the Corresponding Client Node with the given ID, Returns Null if
										// not found

		Node Aux = Origin;
		while (Aux != null && Aux.ID != ID) {
			Aux = Aux.nextNode;
		}
		return Aux;

	}

}

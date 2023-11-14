package imp;

import java.util.*;

public class Graph {

	public static final int INFINITY = Integer.MAX_VALUE; // Nota para nosotrs, asi se declara un infinito en java, es
															// el maximo valor q puede tener un int
	public Node Origin; // This node is used as an indicator that the Graph is initially empty

	public Graph() { // Constructor for the Graph
		Origin = null;
	}

	public abstract class Node {
		int ID;
		Node nextNode;
		Route nextRoute;
	}

	public class Client extends Node {

		int annual_Prod;

		public int getAnnualProd() {
			return annual_Prod;
		}

		public Client(int id, int annualProd) {
			this.ID = id;
			this.annual_Prod = annualProd;
			nextRoute = null;
			nextNode = null;
		}
	}

	public class Dist_center extends Node { // Somehow merge with Client or Standarize the Client class into a Node
											// Class

		int Port_Cost;
		int Annual_Cost;

		public int getPortCost() {
			return Port_Cost;
		}

		public int getAnnualCost() {
			return Annual_Cost;
		}
	}

	public class Route {
		int Uni_Cost;
		Node Destination;
		Route nextRoute;
	}

	public void add_Client(int ID, int annual_Prod) { // Method to add Clients to the Graph
		Client new_Client = new Client(ID, annual_Prod);
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

	public void add_Route_Between_Nodes(int ID_Source, int ID_Dest, int Uni_Cost) { // Method to add Routes between a
																					// client and another

		Node Source = search_Node(ID_Source);
		Node Destination = search_Node(ID_Dest);
		// Create an exception when either is null
		if (Source == null || Destination == null) {
			// throw new RuntimeException("No se puede agregar una ruta entre nodos que no
			// existen " + ID_Source + " "
			// + ID_Dest);
			return;
		}
		Route new_Route = new Route();
		new_Route.Uni_Cost = Uni_Cost; // Sets the Unitary Cost of Transport through this Route
		new_Route.Destination = Destination; // Sets the Route Destination
		new_Route.nextRoute = Source.nextRoute; // Sets to the Origins previous Route
		if (Source != null) {
			Source.nextRoute = new_Route; // Becomes the new Origin´s first route
		}

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

	public Node search_Node(int ID) { // Searches the Corresponding Client Node with the given ID, Returns Null if
										// not found

		Node Aux = Origin;
		while (Aux != null && Aux.ID != ID) {
			Aux = Aux.nextNode;
		}
		return Aux;

	}

	// harry soy santi, agregue esto para ayudarme en el dijkstra
	public int getTotalNodes() {
		int count = 0;
		Node current = Origin;

		while (current != null) {
			count++;
			current = current.nextNode;
		}

		return count;
	}

	public int getCost(int source, int destination) {
		Node src = search_Node(source);
		Node dest = search_Node(destination);

		if (src == null || dest == null) {
			return INFINITY;
		}

		Route route = src.nextRoute;
		while (route != null) {
			if (route.Destination.ID == destination) {
				return route.Uni_Cost; // Return the cost of the rout
			}
			route = route.nextRoute;
		}
		return INFINITY; // If there is no route, returns infinity
	}

	public boolean nodeExist(int id) {
		return search_Node(id) != null;
	}

	public Node getOriginNode() {
		return Origin;
	}

}

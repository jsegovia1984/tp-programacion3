package imp;

import java.util.*;

public class Graph {

	public static final int INFINITY = Integer.MAX_VALUE; // se declara un infinito en java
	public Node Origin;  // este nodo se usa como indicador de que el grafo está inicialmente vacío

	public Graph() { // constructor del grafo
		Origin = null;
	}

	public abstract class Node {
		int ID;
		Node nextNode;
		Route nextRoute;

		public int getAnnualProd(){
			return 0;
		}

		public int getPortCost(){
			return 0;
		}
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

	public class Dist_center extends Node { 
											
		int Port_Cost;
		int Annual_Cost;

		public int getPortCost() {
			return Port_Cost;
		}

		public int getAnnualCost() {
			return Annual_Cost;
		}
		
		public Dist_center(int id, int annualCost, int portCost) {
			this.ID = id;
			this.Annual_Cost = annualCost;
			this.Port_Cost = portCost;
			nextRoute = null;
			nextNode = null;
		}
	}

	public class Route {
		int Uni_Cost;
		Node Destination;
		Route nextRoute;
	}

	public void add_Client(int ID, int annual_Prod) { // metodo para agregar un cliente al grafo
		Client new_Client = new Client(ID, annual_Prod);
		new_Client.nextRoute = null; // comienza sin rutas
		new_Client.nextNode = Origin;
		Origin = new_Client;
		//System.out.println("Client " + ID + " added");
		//System.out.println("---------------------------------------------");
	}

	public void add_Dist_center(int ID, int Port_Cost, int Annual_Cost) {
		Dist_center new_Center = new Dist_center(ID, Annual_Cost, Port_Cost);
		new_Center.ID = ID;
		new_Center.Port_Cost = Port_Cost;
		new_Center.Annual_Cost = Annual_Cost;
		new_Center.nextNode = null;
		new_Center.nextNode = Origin; // Esto, de alguna manera, se estableció en null en lugar de Origin, por lo que los Centros de Distribución no se enlazarían con los nodos anteriores
		Origin = new_Center; // Esto tampoco se asignó nunca, por lo que Origin siempre sería null

		//System.out.println("Distribution Center " + ID + " added");
		//System.out.println("---------------------------------------------");
	}

	public void add_Node(Node node) {
		if (node instanceof Client) {
			add_Client(node.ID, ((Client) node).annual_Prod);
		} else if (node instanceof Dist_center) {
			add_Dist_center(node.ID, ((Dist_center) node).Port_Cost, ((Dist_center) node).Annual_Cost);
		}
	}

	public void add_Route_Between_Nodes(int ID_Source, int ID_Dest, int Uni_Cost) { // metodos para agregar rutas entre nodos
		Node Source = search_Node(ID_Source);
		Node Destination = search_Node(ID_Dest);
		
		// crea una excepción cuando cualquiera de los dos es nulo
		if (Source == null || Destination == null) {
			// throw new RuntimeException("No se puede agregar una ruta entre nodos que no
			// existen " + ID_Source + " "
			// + ID_Dest);
			if(Source == null) {
				System.out.println("Source is null?");
			}
			else {
				System.out.println("Destination is null?");
			}
			System.out.println("Error");
			return;
		}
		Route new_Route = new Route();
		new_Route.Uni_Cost = Uni_Cost;  // Costo unitario de transporte a través de esta ruta
		new_Route.Destination = Destination; // ruta destino
		new_Route.nextRoute = Source.nextRoute; // Establece la ruta anterior del origen
		Source.nextRoute = new_Route; // Becomes the new Origin´s first route
		//System.out.println("Route added!");
		//System.out.println("Start Point: " + Source.ID + " End Point: " + Destination.ID);
		/*
		if (Source.nextRoute != null) { // esta esta mal
			Source.nextRoute = new_Route; // Se convierte en la primera ruta del nuevo origen
		}
		*/

	}



	public int get_Route_Cost(int ID_Source, int ID_Dest) { 	// Obtiene el costo unitario de transporte entre el origen y el destino

		Node Source = search_Node(ID_Source); // Busca el nodo del cliente
		Route Aux = Source.nextRoute; // Obtiene la primera ruta en el nodo del cliente
		while (Aux.Destination.ID != ID_Dest) { // Intenta encontrar el costo unitario
			Aux = Aux.nextRoute;
		}
		return Aux.Uni_Cost;
	
	}
	
	
	
	
	
	

	public ArrayList<Node> get_Neighbors(int ID) {

		ArrayList<Node> Neighbors = new ArrayList<Node>();
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

	public Node search_Node(int ID) { // Busca el nodo de cliente correspondiente con el ID dado, devuelve null si
									  // no se encuentra
		
		
		//System.out.println("Searched Node: " + ID);
		
		Node Aux = Origin;
		
		//System.out.println("Current Node ID: " + Aux.ID);
		
		while (Aux != null && Aux.ID != ID) {
			Aux = Aux.nextNode;
			//System.out.println("Current Node ID: " + Aux.ID);
		}
		
		if (Aux != null) {
			//System.out.println("Node " + ID + " Found!");
		}
		return Aux;

	}

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
				return route.Uni_Cost; // retorna el costo de la ruta
			}
			route = route.nextRoute;
		}
		return INFINITY; // si no hay una ruta retorna infinito
	}

	public boolean nodeExist(int id) {
		return search_Node(id) != null;
	}

	public Node getOriginNode() {
		return Origin;
	}

}

package imp;

import java.util.*;

public class Grafo {

	class Client {
		int ID;
		int annual_Prod; 
		Client nextClient;
		Route Route;
	}
	
	class Dist_center { // Somehow merge with Client or Standarize the Client class into a Node Class
		int ID;
		int Port_Cost;
		int Annual_Cost;
	}
	
	class Route {
		int Uni_Cost;
		Client clientDest;
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
		new_Client.Route = null; 				// Starts without Routes
		new_Client.nextClient = Origin;
		Origin = new_Client;
		
	}
	
	public void add_Dist_center(int ID, int Port_Cost, int Annual_Cost) {
		
		Dist_center new_Center = new Dist_center();
		new_Center.ID = ID;
		new_Center.Port_Cost = Port_Cost;
		new_Center.Annual_Cost = Annual_Cost;
		
	}
	
	public void add_Route(int ID_Source,int ID_Dest, int Uni_Cost) { // Method to add Routes between a client and another
		
		Client Source = search_Client(ID_Source);
		Client Destination = search_Client(ID_Dest);
		// Create an exception when either is null
		Route new_Route = new Route();
		new_Route.Uni_Cost = Uni_Cost; // Sets the Unitary Cost of Transport through this Route
		new_Route.clientDest = Destination; // Sets the Route Destination
		new_Route.nextRoute = Source.Route; // Sets to the Origins previous Route
		Source.Route = new_Route; // Becomes the new Origin´s first route 
		
	}
	
	public int get_Route_Cost(int ID_Source, int ID_Dest) { // Gets the unitary transport cost between the origin and destination
		
		Client Source = search_Client(ID_Source); // Searches the Client Node
		Route Aux = Source.Route; // Gets the first route in the Client Node
		while (Aux.clientDest.ID != ID_Dest) { // Tries to find the Unitary Cost 
			Aux = Aux.nextRoute;
		}
		return Aux.Uni_Cost;
		
	}
	
	public List<Client> get_Neighbors(int Client_ID){
		
		List<Client> Neighbors = new ArrayList<Client>();
		Client Source = search_Client(Client_ID);
		if(Source != null) {
			Route act_Route = Source.Route;
			while(act_Route != null) {
				Neighbors.add(act_Route.clientDest);
				act_Route = act_Route.nextRoute;
			}
		}
		return Neighbors;
		
	}
	
	private Client search_Client(int ID) { // Searches the Corresponding Client Node with the given ID, Returns Null if not found
		
		Client Aux = Origin;
		while (Aux != null && Aux.ID != ID) {
			Aux = Aux.nextClient;
		}
		return Aux;
		
		
	}
	
}

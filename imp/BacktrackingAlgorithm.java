package imp;

import java.util.ArrayList;
import imp.Graph.Client;
import imp.Graph.Dist_center;

public class BacktrackingAlgorithm {

	public static class Backtracking_Node {
		
		int Node_Level;
		int[][] Assignment;
		int Total_Annual_Cost;
		int Max_Total_Annual_Cost; // El costo máximo posible al pasar por este nodo, ahora se utiliza como la comparación para el límite superior


		
		public void calculate_Annual_Cost(Graph graph, int Distances[][], int distributionCenters) { // Calcula el Costo Anual de la asignación actual del Cliente


			
			int Total_Nodes = Distances[0].length;
			
			for(int i = 0; i < distributionCenters ; i++) { // para cada centro de distribución
				
				boolean Dist_Used = false;
				
				//System.out.println("Nodo Actual: " + i);
				
				Dist_center Dist_Center = (Dist_center) graph.search_Node(Total_Nodes - distributionCenters + i); // Recupera el Centro de Distribución del Grafo

				
				for(int j = 0; j < Total_Nodes - distributionCenters; j++) { // para cada cliente
					
					if(Assignment[j][i] == 1) { // Cuando se encuentra la asignación

						
						Dist_Used = true; // Esto significa que el Centro de Distribución está en uso


						Client Client = (Graph.Client) graph.search_Node(j); // Recupera el Cliente del Grafo


						
						int Distance_Client_to_Dist = Distances[i][j]; // Obtiene la distancia entre el Cliente y el Centro de Distribución

						
						int Client_Prod = Client.getAnnualProd(); // Recupera el valor de producción anual del Cliente


						
						Total_Annual_Cost = Total_Annual_Cost + (Client_Prod * Distance_Client_to_Dist) + (Dist_Center.getPortCost() * Client_Prod);
					
						// El cálculo es el siguiente: Producción Anual del Cliente * Costo Unitario de Transporte al Centro de Distribución + Costo Unitario de Transporte en Puerto * Producción del Cliente

						// Esto se hace para cada Cliente y se suma en la única variable Total_Annual_Cost

					}
				}
				
				if(Dist_Used == true) {
					
					//System.out.println("Dist_Centre: " + i + " Used Adding cost: " + Dist_Center.Annual_Cost );
					
					Total_Annual_Cost = (Total_Annual_Cost + Dist_Center.Annual_Cost); // Agrega el costo anual de mantenimiento del centro de distribución, si está en uso

				}

			}
		}
	
		public void calculate_Max_Annual_Cost(Graph graph, int Distances[][]) { // Calcula el costo máximo posible, ignorando los centros de distribución inactivos, similar al problema de la mochila

			
			Max_Total_Annual_Cost = 0;
			
			int Total_Nodes = Distances[0].length;
				
			for(int Current_Dist_Center = 0; Current_Dist_Center < Assignment[0].length; Current_Dist_Center++) { // para cada centro de distribución
				
				boolean Dist_Used = false;
				
				Dist_center Dist_Center = (Dist_center) graph.search_Node(Total_Nodes - Assignment[0].length + Current_Dist_Center);
				
				for(int Client_ID = 0; Client_ID < Total_Nodes - Assignment.length; Client_ID++) { 
					
					Client Client = (Graph.Client) graph.search_Node(Client_ID);
					
					int Aux_Value = Integer.MIN_VALUE;
					
					int Max_Dist_Center = -1;
					
					if (Distances[Current_Dist_Center][Client_ID] > Aux_Value && Assignment[Client_ID][Current_Dist_Center] != -1) { // Si el valor es mayor que el auxiliar y no es un centro no asignado

						
						Dist_Used = true;
						
						Aux_Value = Distances[Current_Dist_Center][Client_ID];
						
						Max_Dist_Center = Current_Dist_Center;
						
					}
					
					if(Max_Dist_Center != -1) {
					
						int Distance_Client_to_Dist = Distances[Max_Dist_Center][Client_ID];// Obtiene la distancia entre el cliente y el centro de distribución

						
						int Client_Prod = Client.getAnnualProd(); // Recupera el valor de producción anual del cliente

						
						Max_Total_Annual_Cost = Max_Total_Annual_Cost + (Client_Prod * Distance_Client_to_Dist) + (Dist_Center.getPortCost() * Client_Prod);
					}
					
				}
				
				if(Dist_Used == true) {
					
					//System.out.println("Dist_Centre: " + i + " Used Adding cost: " + Dist_Center.Annual_Cost );
					
					Max_Total_Annual_Cost = (Max_Total_Annual_Cost + Dist_Center.Annual_Cost);// Agrega el costo anual de mantenimiento del centro de distribución, si está en uso


				}
				
			}
			
		}
		
		public void add_Dist_Center(int Dist_Center, int Distances[][]) { // Establece una columna del centro de distribución a 0 para que esté disponible para asignación

			
			//System.out.println("Adding Center: " + (Dist_Center + 50));
			
			for(int Current_Client = 0; Current_Client < Assignment.length; Current_Client++) {
				
					Assignment[Current_Client][Dist_Center] = 0;
					
					next_minDist_center(Current_Client, Dist_Center, Distances); // Verifica si el centro de distribución habilitado actualmente es mejor que las opciones anteriores

					
				}

			}
		
		private void next_minDist_center(int Client_ID, int Added_Dist_ID, int Distances[][]) { // Encuentra el siguiente centro más cercano para el cliente afectado

			
			int Aux_Value = Integer.MAX_VALUE;
			
			int Min_Dist_Center = -1;
			
			//System.out.println("Finding min with: " + (Added_Dist_ID + 50));
			
			for(int Current_Dist_Center = 0; Current_Dist_Center < Assignment[0].length; Current_Dist_Center++) { // por cada centro de distribucion
				
				if (Distances[Current_Dist_Center][Client_ID] < Aux_Value && Assignment[Client_ID][Current_Dist_Center] != -1) { // Si el valor es menor que el auxiliar y no es un centro no asignado

					
					Aux_Value = Distances[Current_Dist_Center][Client_ID];
					
					Min_Dist_Center = Current_Dist_Center;
					
				}
				
				if(Assignment[Client_ID][Current_Dist_Center] == 1 || Assignment[Client_ID][Current_Dist_Center] != -1) {
					
					Assignment[Client_ID][Current_Dist_Center] = 0; // Establece el centro de distribución actual como no utilizado

					
				}

			}
			Assignment[Client_ID][Min_Dist_Center] = 1;
		}
		
		public void clear_Annual_Cost() { //Pone el costo en cero para recalcularlo
			Total_Annual_Cost = 0;
		}
		
	}
	
	public int[][] Backtracking(Graph graph, int Distances[][], ArrayList<Integer> distributionCenters){
		
		Backtracking_Node Result = new Backtracking_Node(); // Comienza como null
		
		Result.Total_Annual_Cost = Integer.MAX_VALUE; // Comienza como infinito
		
		Backtracking_Node Root = create_RootNode(Distances[0].length, distributionCenters.size());// // Crea la matriz de asignación del nodo raíz sin centros asignados

		
		int upper_Limit = Integer.MAX_VALUE; // Establece el límite superior al costo anual raíz inicialmente

		PriorityQueue Alive_Nodes = new PriorityQueue(); // Crea una cola de prioridad Min-heap
		
		Alive_Nodes.insert(Root, Root.Total_Annual_Cost); // Inserta el nodo raíz en la cola de nodos
		
		int Iterations = 1; // Eliminar esto más tarde, solo contando las iteraciones realizadas
		
		int Trim_Counter = 0; // Eliminar esto más tarde, solo contando los recortes
		
		while (!Alive_Nodes.isEmpty()) { // Mientras haya nodos por verificar
			
			Iterations++; // cuenta las iteraciones
			
			Backtracking_Node Current_Node = Alive_Nodes.remove(); // Obtiene el nodo con el menor costo anual y lo elimina de la cola 
			
			/*
			
			System.out.println("---------------------------------------------");
			
			show_Greedy(Current_Node.Assignment, Current_Node.Assignment[0].length);
			
			System.out.println("---------------------------------------------");
			
			*/
			
			//System.out.println("Annual cost of child: " + Current_Node.Total_Annual_Cost);
			
			//System.out.println("Current upper limit: " + upper_Limit);
			
			if(Current_Node.Node_Level < Current_Node.Assignment[0].length  && Current_Node.Max_Total_Annual_Cost < upper_Limit) { // Sin esta verificación de nivel, el algoritmo falla. También decide si debe ser recortado o no

				
								
				ArrayList<Backtracking_Node> Current_Children = create_Children(graph, Current_Node, Current_Node.Node_Level, Distances, distributionCenters);
				
				for(int i = 0; i < Current_Children.size(); i++) {
					
					Backtracking_Node Current_Child = Current_Children.get(i); // Obtiene los hijos
					
					if(i == 0) { // Si el hijo es el que no tiene el centro de distribución agregado, no es necesario borrar y recalcular el costo anual / Max_Annual_Cost, ya que esos permanecen iguales

						Current_Child.clear_Annual_Cost(); // Elimina el valor anterior
					
						Current_Child.calculate_Annual_Cost(graph, Distances, distributionCenters.size()); // Calcula el costo del hijo si es un centro eliminado
					
						Current_Child.calculate_Max_Annual_Cost(graph, Distances); // Calcula el nuevo Max_Annual_Cost teniendo en cuenta el centro de distribución recién agregado
					
						//System.out.println("Node Level: " + Current_Child.Node_Level);

						//System.out.println("Annual cost of child: " + Current_Child.Total_Annual_Cost);
						
						//System.out.println("Current upper limit: " + upper_Limit);

						//System.out.println("---------------------------------");
						
						//show_Greedy(Current_Child.Assignment, Current_Child.Assignment[0].length);
						
						//System.out.println("-----------------------------------------------------------");
						
					}
					
					//System.out.println("Max Annual cost of child: " + Current_Child.Max_Total_Annual_Cost);

						if(is_Leaf(Current_Child, distributionCenters.size())) { // Check if it is a Leaf
								
							//show_Greedy(Current_Child.Assignment, Current_Child.Assignment[0].length);
							
							//System.out.println("Node Level: " + Current_Child.Node_Level);
							
							//System.out.println("Annual cost of child: " + Current_Child.Total_Annual_Cost);
							
							if (Current_Child.Total_Annual_Cost < Result.Total_Annual_Cost) { // Si es mejor que el resultado actual

								
								Result = Current_Child; // Se convierte en el resultado

								upper_Limit = Math.min(upper_Limit, Current_Child.Total_Annual_Cost); // Actualiza el límite superior
								}
							}
						
						else {
							
							Alive_Nodes.insert(Current_Child, Current_Child.Total_Annual_Cost); // Inserta el hijo con su costo anual en el Heap

							
							}
						//upper_Limit = Math.min(upper_Limit, Current_Child.Total_Annual_Cost);
						}
				}	
			else {
				
				
				Trim_Counter++;
			}
		}
		
		System.out.println("");
		
		System.out.println("--------------------Resultados-----------------------");
		
		System.out.println("");
		
		System.out.println("Interaciones: " + Iterations);
		
		System.out.println("");
		
		System.out.println("Candidad de Pruneos: " + (int)(Math.pow(2, distributionCenters.size()) - Iterations));
		
		System.out.println("");

		System.out.println("Total de Hojas Calculadas: " + (int)Math.pow(2, distributionCenters.size()));

		System.out.println("");
		
		System.out.println("El costo final Anual es: " + Result.Total_Annual_Cost);
		
		System.out.println("");
		
		System.out.println("-------------Resultados de la asignación-------------------");
		
		System.out.println("");
		
		//show_Greedy(Result.Assignment, Result.Assignment[0].length);

		System.out.println("");
		
		return Result.Assignment;
	}
	

	public Backtracking_Node create_CloneNode(Backtracking_Node Parent) { //copia el nodo padre
		
		Backtracking_Node Copy = new Backtracking_Node();
		
		Copy.Assignment = new int[Parent.Assignment.length][Parent.Assignment[0].length];  // Inicializa la matriz en la copia

		
		for (int i = 0; i < Parent.Assignment.length; i++) {
			
			for (int j = 0; j < Parent.Assignment[0].length; j++) {
				
				Copy.Assignment[i][j] = Parent.Assignment[i][j];  // Copio los valores
			}
		}
		
		Copy.Total_Annual_Cost = Parent.Total_Annual_Cost;
		
		Copy.Max_Total_Annual_Cost = Parent.Max_Total_Annual_Cost;
		
		return Copy;
		
	}
	
	public Backtracking_Node create_RootNode(int Total_Nodes, int Distribution_Centers) { // crea el nodo raiz
		
		Backtracking_Node Root = new Backtracking_Node();
		
		int Clients = Total_Nodes - Distribution_Centers; // Obtiene la cantidad de clientes
		
		Root.Assignment = new int[Total_Nodes - Distribution_Centers][Distribution_Centers];
		
		for(int i = 0; i <  Distribution_Centers; i++) { //Establece todos los centros como no asignados, es decir, -1
			
			for(int j = 0; j < Clients; j++) {
				
					Root.Assignment[j][i] = -1; 
				}
			}
		
		Root.Total_Annual_Cost = Integer.MAX_VALUE; // Como está vacío, el costo es infinito

		
		Root.Node_Level = 0; //  El nivel del nodo raíz comienza en 0

		
		Root.Max_Total_Annual_Cost = Integer.MIN_VALUE;
		
		return Root;
	}
	
	public ArrayList<Backtracking_Node> create_Children(Graph graph ,Backtracking_Node Parent, int Dist_Center_Level, int Distances[][], ArrayList<Integer> distributionCenters){
		
		ArrayList<Backtracking_Node> Children = new ArrayList<Backtracking_Node>();
		
		Backtracking_Node Added_Dist_Cent = create_CloneNode(Parent); // Esto es solo una copia del padre

		
		Backtracking_Node Non_Added_Dist_Cent = create_CloneNode(Parent); // Esto comienza como solo una copia

		
		Added_Dist_Cent.add_Dist_Center(Dist_Center_Level, Distances); // agrega el centro de distribucion
		
		//show_Greedy(Added_Dist_Cent.Assignment, distributionCenters.size());
		
		//System.out.println("-------------------------------------------");
		
		//show_Greedy(Non_Added_Dist_Cent.Assignment, distributionCenters.size());
		
		Added_Dist_Cent.clear_Annual_Cost(); // Resets Cost
		
		Added_Dist_Cent.calculate_Annual_Cost(graph, Distances, distributionCenters.size()); // Recalcula el costo anual


		
		Added_Dist_Cent.Node_Level = Dist_Center_Level + 1;
		
		Non_Added_Dist_Cent.Node_Level = Dist_Center_Level + 1;
		
		Children.add(Added_Dist_Cent);
		
		Children.add(Non_Added_Dist_Cent);
		
		return Children;
	}
	
	public boolean is_Leaf(Backtracking_Node Node, int Distrib_Center) { // chequeas si el nodo es hoja o no
		
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
 * 	NOTAS
 * 
 * El algoritmo sin prunear  tiene un número de iteraciones de 2^8 = 256 veces, de acuerdo con el cálculo de costo de (Cantidad de Hijos) ^ Cantidad de Niveles
 * 
 * pruneando las iteraciones se reducen casi a la mitad
 * 
 */

}

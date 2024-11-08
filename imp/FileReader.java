package imp;

import java.io.*;
import java.util.ArrayList;

public class FileReader {

    public Graph graph;
    public ArrayList<Integer> distCentersList;
    public String routesFileName = "data/routes.txt";
    public String clientFileName = "data/clientData.txt";
    public String distCenterFileName = "data/distCenter.txt";

    public FileReader() {
        graph = new Graph();
        distCentersList = new ArrayList<Integer>();
    }

    public void loadRoutes() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(routesFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Salta lineas comentadas
            }
            String[] parts = line.split(",");
            int idSource = Integer.parseInt(parts[0]);
            int idDest = Integer.parseInt(parts[1]);
            int uniCost = Integer.parseInt(parts[2]);
            //System.out.println("New Route Source : " + idSource + "  Destination : " + idDest + " Cost: " + uniCost);
            graph.add_Route_Between_Nodes(idSource, idDest, uniCost);
        }
        reader.close();
    }

    public void loadClients() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(clientFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Salta lineas comentadas
            }
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            int annualProd = Integer.parseInt(parts[1]);
            //System.out.println("New Client ID: " + id + " AnnualProd: " + annualProd);
            graph.add_Client(id, annualProd);
        }
        reader.close();
    }

    public void loadDistCenters(int clientID_count) throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(distCenterFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Skip comment lines
            }
            String[] parts = line.split(",");
            int id = (Integer.parseInt(parts[0]) + clientID_count); // Al obtener la cantidad de clientes de antemano, podemos evitar repetir IDs
            int portCost = Integer.parseInt(parts[1]);
            int annualCost = Integer.parseInt(parts[2]);
            //System.out.println("New Distribution Center: " + id + " Port Cost: " + portCost + " Annual Cost: " + annualCost);
            distCentersList.add(id);
            graph.add_Dist_center(id, portCost, annualCost);
        }
        reader.close();
    }

    public Graph getGraph() {
        return graph;
    }
    
    public int getTotalNodes() {
    	return graph.getTotalNodes();
    }

    public ArrayList<Integer> getDistCenters() {
        return distCentersList;
    }

}

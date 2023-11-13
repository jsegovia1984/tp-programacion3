package imp;

import java.io.*;

import imp.Graph.Client;

public class FileReader {

    public Graph graph;
    public String routesFileName = "data/routes.txt";
    public String clientFileName = "data/clientData.txt";
    public String distCenterFileName = "data/distCenter.txt";

    public FileReader() {
        graph = new Graph();
        graph.InitializeGraph();
    }

    public void loadRoutes() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(routesFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Skip comment lines
            }
            String[] parts = line.split(",");
            int idSource = Integer.parseInt(parts[0]);
            int idDest = Integer.parseInt(parts[1]);
            int uniCost = Integer.parseInt(parts[2]);
            System.out.println("New Route Source : " + idSource + "  Destination : " + idDest + " Cost: " + uniCost);
            graph.add_Route_Between_Nodes(idSource, idDest, uniCost);
        }
        reader.close();
    }

    public void loadClients() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(clientFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Skip comment lines
            }
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            int annualProd = Integer.parseInt(parts[1]);
            System.out.println("New Client ID: " + id + " AnnualProd: " + annualProd);
            graph.add_Client(id, annualProd);
        }
        reader.close();
    }

    public void loadDistCenters() throws IOException {

        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(distCenterFileName)));

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue; // Skip comment lines
            }
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            int portCost = Integer.parseInt(parts[1]);
            int annualCost = Integer.parseInt(parts[2]);
            System.out.println(
                    "New Distribution Center: " + id + " Port Cost: " + portCost + " Annual Cost: " + annualCost);
            graph.add_Dist_center(id, portCost, annualCost);
        }
        reader.close();
    }

    public Graph getGraph() {
        return graph;
    }

}

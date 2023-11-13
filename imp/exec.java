package imp;

import java.io.IOException;

import imp.Graph.Client;
import imp.Graph.Node;

public class exec {

    public static void main(String[] args) {
        FileReader fileReader = new FileReader();
        try {
            fileReader.loadClients();
            fileReader.loadRoutes();
            fileReader.loadDistCenters();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Graph filledGraph = fileReader.getGraph();
        Client node = (Client) filledGraph.search_Node(6);

        System.out.println("Node ID: " + node.ID + " Annual Prod: " + node.annual_Prod);
        System.out.println("Total Nodes: " + filledGraph.getTotalNodes());

    }

}

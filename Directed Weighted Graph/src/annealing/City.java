package annealing;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import implementation.Node;

public class City {
    NodeData node;


    // Constructs a randomly placed city
    public City(){
        this.node = new Node();
    }

    // Constructs a city at chosen Node
    public City(NodeData node){
        this.node = new Node(node);

    }

    public NodeData getNode(){
        return this.node;
    }


    // Gets the distance to given city
    public double distanceTo(City city, DirectedWeightedGraphAlgorithms graphAlgorithms){

        double distance = graphAlgorithms.shortestPathDist(this.node.getKey(), city.node.getKey());

        return distance;
    }

    @Override
    public String toString() {
        return "City{" +
                "node=" + node +
                '}';
    }
}

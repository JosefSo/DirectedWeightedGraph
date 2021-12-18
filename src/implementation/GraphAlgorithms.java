package implementation;

import Json.GraphJsonDeserializer;
import Json.JsonGraph;
import annealing.City;
import annealing.Tour;
import annealing.TourManager;
import api.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class GraphAlgorithms implements DirectedWeightedGraphAlgorithms {
    private DirectedWeightedGraph graph;

    public GraphAlgorithms() {
        this.graph = new Graph();
    }

    public GraphAlgorithms(DirectedWeightedGraph graph) {
        this.graph = graph;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        this.graph = g;
    }

    @Override
    public boolean isConnected() {
        if (this.graph.nodeSize() == 0||this.graph.nodeSize() == 1)
            return true;
        DirectedWeightedGraphAlgorithms algo = new GraphAlgorithms(this.copy());
        Iterator<NodeData> nodeDataIterator = algo.getGraph().nodeIter();

        Dijkstra(algo.getGraph(), nodeDataIterator.next().getKey());
        nodeDataIterator = algo.getGraph().nodeIter();
        while(nodeDataIterator.hasNext()){
            if(nodeDataIterator.next().getWeight() == Integer.MAX_VALUE){
                return false;
            }
        }
        nodeDataIterator = algo.getGraph().nodeIter();
        int d=nodeDataIterator.next().getKey();
        int s=nodeDataIterator.next().getKey();
        if(this.shortestPathDist(s,d)==-1)
            return false;
        return true;
    }
    private LinkedList findInit(DirectedWeightedGraph g1) {
        LinkedList<NodeData> queue = new LinkedList<NodeData>();
        Iterator<NodeData> nitr = g1.nodeIter();
        while (nitr.hasNext()) {
            NodeData ni = nitr.next();
            ni.setWeight(Integer.MAX_VALUE);
            ni.setTag(Integer.MIN_VALUE);
            ni.setInfo("");
            queue.add(ni);
        }
        return queue;
    }

    //Dijkstra
    //d-->weight
    //Pi-->tag
    private int ExtractMin(LinkedList<NodeData> q) {
        LinkedList<NodeData> qcopy = new LinkedList<NodeData>();
        NodeData ni = q.poll();
        qcopy.add(ni);
        double minweight = ni.getWeight();
        int key = ni.getKey();
        while (!q.isEmpty()) {
            ni = q.poll();
            qcopy.add(ni);
            if (ni.getWeight() < minweight) {
                minweight = ni.getWeight();
                key = ni.getKey();
            }
        }
        while (!qcopy.isEmpty()) {
            q.add(qcopy.poll());
        }
        return key;
    }

    private void relax(DirectedWeightedGraph g1, int src, int dest) {
        if ((g1.getNode(src).getWeight()<Integer.MAX_VALUE) && (g1.getNode(dest).getWeight() > g1.getNode(src).getWeight() + g1.getEdge(src, dest).getWeight())) {
            g1.getNode(dest).setWeight(g1.getNode(src).getWeight() + g1.getEdge(src, dest).getWeight());
            g1.getNode(dest).setTag(src);
        }
    }

    private void Dijkstra(DirectedWeightedGraph g1, int src) {
        LinkedList<NodeData> queue = findInit(g1);
        g1.getNode(src).setWeight(0);
        while (!queue.isEmpty()) {
            NodeData node=g1.getNode(ExtractMin(queue));
            queue.remove(node);
            Iterator<EdgeData>eitr=g1.edgeIter(node.getKey());
            while (eitr.hasNext())
            {
                relax(g1,node.getKey(),eitr.next().getDest());
            }
        }

    }


    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest)
            return 0;
        DirectedWeightedGraph gcopy = this.copy();
        this.Dijkstra(gcopy,src);
        if ((int) (gcopy.getNode(dest).getWeight()) == Integer.MAX_VALUE)
            return -1;
        return gcopy.getNode(dest).getWeight();
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        LinkedList<NodeData> stack = new LinkedList<NodeData>();
        stack.push(this.graph.getNode(dest));
        if (src == dest)
            return stack;
        DirectedWeightedGraph gcopy = this.copy();
        this.Dijkstra(gcopy,src);
        NodeData node=gcopy.getNode(dest);
        if ((int) (node.getWeight()) == Integer.MAX_VALUE)
            return null;
        while (node.getTag()!=src) {
            node = gcopy.getNode(node.getTag());
            stack.push(this.graph.getNode(node.getKey()));
        }
        LinkedList list =new LinkedList();
        list.add(this.graph.getNode(src));
        while (!stack.isEmpty())
            list.add(stack.pop());
        return list;
    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.graph;
    }

    @Override
    public DirectedWeightedGraph copy() {
        DirectedWeightedGraph graph = new Graph();
        Iterator<NodeData> nit = this.graph.nodeIter();
        while (nit.hasNext()) {
            NodeData n = nit.next();
            graph.addNode(new Node(n));
        }
        Iterator<EdgeData> eit = this.graph.edgeIter();
        while (eit.hasNext()) {
            EdgeData edge = new Edge(eit.next());
            graph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
            graph.getEdge(edge.getSrc(), edge.getDest()).setTag(edge.getTag());
            graph.getEdge(edge.getSrc(), edge.getDest()).setInfo(edge.getInfo());
        }
        return graph;
    }

    @Override
    public NodeData center() {
        if (this.graph == null || this.graph.nodeSize() == 0)
            return null;
        if (isConnected()){
            int center = 0;
            int V = this.graph.nodeSize();
            double[][] dist = new double[V][V];
//        int next[][] = new int[V][V];

            for(int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    dist[i][j] = Double.POSITIVE_INFINITY;
                }
            }

            Iterator<EdgeData> edgeIter = this.graph.edgeIter();
            while (edgeIter.hasNext()) {
                EdgeData edge = new Edge(edgeIter.next());
                dist[edge.getSrc()][edge.getDest()] = edge.getWeight();
//            next[edge.getSrc()][edge.getDest()] = edge.getSrc();
            }
            Iterator<NodeData> nodeIter = this.graph.nodeIter();
            while (nodeIter.hasNext()) {
                NodeData n = nodeIter.next();
                dist[n.getKey()][n.getKey()] = 0;
//            next[n.getKey()][n.getKey()] = n.getKey();
            }

            for(int k = 0; k < V; k++){
                for(int i = 0; i < V; i++){
                    for(int j = 0; j < V; j++){
                        if(dist[i][j] > dist[i][k] + dist[k][j]){
                            dist[i][j] = dist[i][k] + dist[k][j];
//                        next[i][j] = next[i][k];
                        }
                    }
                }
            }
            double[] arr = new double[V];
            double max_value;
            for(int i = 0; i < V; i++){
                arr[i] = dist[i][0];
                max_value = dist[i][0];
                for (int j = 0; j < V; j++){
                    if (i != j){
                        if(dist[i][j] > max_value){
                            max_value = dist[i][j];
                            arr[i] = max_value;
                        }
                    }
                }
            }
            double min_value = arr[0];
            for(int i = 0; i < V; i++){
                if(arr[i] < min_value){
                    center = i;
                    min_value = arr[i];
                }
            }
            return new Node(this.graph.getNode(center));
        }
        return null;
    }
    public List<NodeData> maketsp(List<NodeData> cities) {

        if (this.graph.nodeSize() == 0 || !this.isBindingComponent(this.graph,cities))
            return null;

        DirectedWeightedGraphAlgorithms graphAlgo = new GraphAlgorithms();
        DirectedWeightedGraph graph = new Graph();


        for (int i = 0; i < cities.size(); i++){
            NodeData node = cities.get(i);
            graph.addNode(node);
        }

        for (int i = 0; i < cities.size(); i++){
            NodeData node = cities.get(i);
            Iterator<EdgeData> edgeDataIterator = this.graph.edgeIter(node.getKey());
            while (edgeDataIterator.hasNext()){
                EdgeData edge = edgeDataIterator.next();
                graph.connect(edge.getSrc(), edge.getDest(), edge.getWeight());
            }
        }

        graphAlgo.init(graph);

        List<NodeData> cities2 = new LinkedList<NodeData>();

        if(TourManager.numberOfCities()!= 0)
            TourManager.deleteAll();

        Iterator<NodeData> nodeDataIterator2 = graphAlgo.getGraph().nodeIter();
        while(nodeDataIterator2.hasNext()){
            NodeData node = nodeDataIterator2.next();
            cities2.add(node);

            // Create and add our cities
            City city = new City(node);
            TourManager.addCity(city);
        }


        // Set initial temp
        double temp = 10000;

        // Cooling rate
        double coolingRate = 0.003;

        // Initialize intial solution
        Tour currentSolution = new Tour();
        currentSolution.generateIndividual();

        System.out.println("Initial solution distance: " + currentSolution.getDistance(graphAlgo));

        // Set as current best
        Tour best = new Tour(currentSolution.getTour());

        // Loop until system has cooled
        while (temp > 1) {
            // Create new neighbour tour
            Tour newSolution = new Tour(currentSolution.getTour());

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());

            // Get the cities at selected positions in the tour
            City citySwap1 = newSolution.getCity(tourPos1);
            City citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);

            // Get energy of solutions
            double currentEnergy = currentSolution.getDistance(graphAlgo);
            double neighbourEnergy = newSolution.getDistance(graphAlgo);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
                currentSolution = new Tour(newSolution.getTour());
            }

            // Keep track of the best solution found
            if (currentSolution.getDistance(graphAlgo) < best.getDistance(graphAlgo)) {
                best = new Tour(currentSolution.getTour());
            }

            // Cool system
            temp *= 1-coolingRate;
        }

        System.out.println("Final solution distance: " + best.getDistance(graphAlgo));
        //System.out.println("Tour: " + best);

        final Tour tour = best;


        //CONVERSE from List tour to List NodeData
        List<NodeData> tmp;
        List<NodeData> ans = new LinkedList<NodeData>();
        boolean firstTime = true;
        for(int i = 0; i < tour.getTour().size(); i++){
            //ans.add(new Node(tour.getCity(i).getNode()));
            if (i+1 != tour.getTour().size()){
                tmp = shortestPath(tour.getCity(i).getNode().getKey(), tour.getCity(i+1).getNode().getKey());
                for(int j = 1; j<tmp.size(); j++){
                    if (!firstTime){
                        ans.add(new Node(tmp.get(j)));
                    }
                    else{
                        ans.add(new Node(tmp.get(0)));
                        ans.add(new Node(tmp.get(1)));
                        firstTime = false;
                    }
                }
            }
        }

        for(int i = 0; i<ans.size(); i++){
            System.out.print(ans.get(i).getKey());
            if (i+1 != ans.size())
                System.out.print("->");
        }

        //ans.add(ans.get(0));

        return ans;
    }
    private Boolean isBindingComponent(DirectedWeightedGraph graph,List<NodeData> cities){
        DirectedWeightedGraphAlgorithms algo=new GraphAlgorithms();
        for(NodeData node:cities)
        {
            algo.getGraph().addNode(graph.getNode(node.getKey()));
        }
        for(NodeData node:cities)
        {
            Iterator<EdgeData>eitr=graph.edgeIter(node.getKey());
            while (eitr.hasNext()) {
                EdgeData edge =eitr.next();
                algo.getGraph().connect(edge.getSrc(),edge.getDest(),edge.getWeight());
            }
        }
        return algo.isConnected();
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
        GraphAlgorithms algo = new GraphAlgorithms();
        algo.init(this.copy());
        return algo.maketsp(cities);
    }
    
    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

    @Override
    public boolean save(String file) {
        //Make JSON!!
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        //Write JSON to file
        try
        {
            PrintWriter pw = new PrintWriter(new File(file));
            DirectedWeightedGraph gcopy=this.copy();
            Iterator<NodeData>nitr=gcopy.nodeIter();
            while (nitr.hasNext())
            {
                NodeData node=nitr.next();
                GeoLocation g = new Geo_Location(node.getLocation().x(),node.getLocation().y(),node.getLocation().z());
                node.setLocation(g);
            }
            JsonGraph gr=new JsonGraph(gcopy);
            pw.write(gson.toJson(gr));
            pw.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        DirectedWeightedGraph ans = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Graph.class, new GraphJsonDeserializer());
            Gson gson = builder.create();
            //continue as usual..
            FileReader reader = new FileReader(file);
            ans = gson.fromJson(reader, Graph.class);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Iterator<NodeData>nitr=ans.nodeIter();
        while (nitr.hasNext())
        {
            NodeData node=nitr.next();
            GeoLocation g = new Geo_Location(node.getLocation().x(),node.getLocation().y(),node.getLocation().z());
            node.setLocation(g);
        }
        this.init(ans);
        return true;
    }
}

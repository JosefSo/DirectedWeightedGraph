package GUI;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;



import api.EdgeData;
import api.NodeData;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import implementation.Graph;
import implementation.GraphAlgorithms;
import implementation.Node;

import javax.swing.*;
import java.lang.reflect.Type;



/**
* This class is the main class for GUI.Ex2 - your implementation will be tested using this class.
*/
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DirectedWeightedGraphAlgorithms algo =new GraphAlgorithms();
        if(algo.load(json_file))
            return algo.getGraph();
        return new Graph();
    }

    /**
     * This static function will be used to test your implementation
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms algo =new GraphAlgorithms();
        if(algo.load(json_file))
            return algo;
        return new GraphAlgorithms();
    }

    /**
     * This static function will run your GUI using the json fime.
     *
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     */
    public static void runGUI(String json_file) {
        DirectedWeightedGraphAlgorithms alg = new GraphAlgorithms();
        alg.load(json_file);
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }

        GraphGUI gui = new GraphGUI(json_file);
    }


    public static void main(String[] args) {
//        if(args.length>0)
//           runGUI(args[0]);
//        else
//           runGUI("data\\G1.json");
        DirectedWeightedGraphAlgorithms graphAlgo = new GraphAlgorithms();
        graphAlgo.load("data/G1.json");



        List<NodeData> cities = new LinkedList<>();

        Iterator<NodeData> nodeDataIterator = graphAlgo.getGraph().nodeIter();
        while(nodeDataIterator.hasNext()){
            NodeData node = new Node(nodeDataIterator.next());
            cities.add(node);
        }
        List<NodeData> ans = new LinkedList<>();

        ans = graphAlgo.tsp(cities);
        System.out.println();



        System.out.println(ans.size());




    }
}


package tests;

import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import implementation.Geo_Location;
import implementation.Graph;
import implementation.GraphAlgorithms;
import implementation.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static GUI.Ex2.getGrapgAlgo;
import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {
    DirectedWeightedGraphAlgorithms algog1;
    DirectedWeightedGraph graph1;
    @BeforeEach
    void setUp() {
        algog1 = new GraphAlgorithms();
        graph1 = new Graph();
        graph1.addNode(new Node("0,0,0",0));
        graph1.addNode(new Node("0,0,0",1));
        graph1.addNode(new Node("0,0,0",2));
        graph1.addNode(new Node("0,0,0",3));
        graph1.connect(0,1,0);
        graph1.connect(2,1,0);
        graph1.connect(3,2,0);
    }

    @Test
    void init() {
        algog1.init(graph1);
        assertEquals(graph1.toString(),algog1.getGraph().toString());
    }

    @Test
    void isConnected() {
        graph1.connect(1,3,0);
        graph1.connect(2,0,0);
        algog1.init(graph1);
        algog1.isConnected();
        assertTrue(algog1.isConnected());
    }

    @Test
    void shortestPathDist() {
        DirectedWeightedGraphAlgorithms alg = new GraphAlgorithms();
        alg.getGraph().addNode(new Node("0,0,0",1));
        alg.getGraph().addNode(new Node("0,0,0",2));
        alg.getGraph().addNode(new Node("0,0,0",3));
        alg.getGraph().addNode(new Node("0,0,0",4));
        alg.getGraph().addNode(new Node("0,0,0",5));
        alg.getGraph().addNode(new Node("0,0,0",6));
        alg.getGraph().addNode(new Node("0,0,0",7));
        alg.getGraph().connect(1,3,2);
        alg.getGraph().connect(3,5,3);
        alg.getGraph().connect(5,7,4);
        alg.getGraph().connect(1,2,1);
        alg.getGraph().connect(2,4,2);
        alg.getGraph().connect(4,6,3);
        alg.getGraph().connect(6,7,4);

        alg.getGraph().connect(7,2,6);
        alg.getGraph().connect(6,1,4);
        alg.getGraph().connect(7,1,13);
        alg.getGraph().connect(2,1,1);
        assertEquals(9,alg.shortestPathDist(1,7));
        System.out.println(alg.isConnected());
        System.out.println(alg.shortestPath(1,7));

    }

    @Test
    void getGraph() {
        algog1.init(graph1);
        DirectedWeightedGraph get1=algog1.getGraph();
        assertEquals(graph1.toString(),algog1.getGraph().toString());
        assertEquals(get1.toString(),algog1.getGraph().toString());
        get1.removeNode(0);
        assertEquals(get1.toString(),algog1.getGraph().toString());
    }

    @Test
    void copy() {
        algog1.init(graph1);
        DirectedWeightedGraph copy1=algog1.copy();
        assertEquals(graph1.toString(),copy1.toString());
        assertEquals(copy1.toString(),algog1.getGraph().toString());
        copy1.removeNode(0);
        assertNotEquals(copy1.toString(),algog1.getGraph().toString());
    }

    @Test
    void shortestPath() {
        DirectedWeightedGraphAlgorithms alg1 = new GraphAlgorithms();
        alg1.load("data/G1.json");
        List<NodeData> list1 = new ArrayList<NodeData>();
        list1 = alg1.shortestPath(0, 15);

        assertEquals(0, list1.get(0).getKey());
        assertEquals(16, list1.get(1).getKey());
        assertEquals(15, list1.get(2).getKey());


        DirectedWeightedGraphAlgorithms alg2 = new GraphAlgorithms();
        alg2.load("data/G2.json");
        List<NodeData> list2 = new ArrayList<NodeData>();
        list2 = alg2.shortestPath(0, 15);

        assertEquals(0, list2.get(0).getKey());
        assertEquals(16, list2.get(1).getKey());
        assertEquals(15, list2.get(2).getKey());


        DirectedWeightedGraphAlgorithms alg3 = new GraphAlgorithms();
        alg3.load("data/G3.json");
        List<NodeData> list3 = new ArrayList<NodeData>();
        list3 = alg3.shortestPath(0, 15);

        assertEquals(0, list3.get(0).getKey());
        assertEquals(2, list3.get(1).getKey());
        assertEquals(3, list3.get(2).getKey());
        assertEquals(13, list3.get(3).getKey());
        assertEquals(14, list3.get(4).getKey());
        assertEquals(15, list3.get(5).getKey());



    }

    @Test
    void center() {
        assertNull(algog1.center());
        algog1.load("data/G1.json");
        assertEquals(8,algog1.center().getKey());
        algog1.load("data/G2.json");
        assertEquals(0,algog1.center().getKey());
        algog1.load("data/G3.json");
        assertEquals(40,algog1.center().getKey());
    }

    @Test
    void tsp() {
//        DirectedWeightedGraphAlgorithms alg1, alg2, alg3;
//        alg1 = getGrapgAlgo("data/G1.json");
//        alg2 = getGrapgAlgo("data/G2.json");
//        alg3 = getGrapgAlgo("data/G3.json");
//        List<NodeData> city = new LinkedList<NodeData>();
//
//        List<NodeData> lst1, lst2, lst3;
//
//        Iterator<NodeData> nodeIter = alg1.getGraph().nodeIter();
//        while (nodeIter.hasNext()) {
//            NodeData n = nodeIter.next();
//            city.add(n);
//        }
//
//        lst1 = alg1.tsp(city);
//        lst2 = alg2.tsp(city);
//        lst3 = alg3.tsp(city);





    }

    @Test
    void save() {
        try {
            assertFalse(algog1.load("nojsonfile"));
        }
        catch(Exception e)
        {
            assertEquals(" nojsonfile (The system cannot find the file specified)",e.toString());
        }

        assertTrue(algog1.save("data/test.json"));

    }

    @Test
    void load() {
        try {
            assertFalse(algog1.load("unexistfile.json"));
        }
        catch(Exception e)
        {
            assertEquals("datanexistfile.json (The system cannot find the file specified)",e.toString());
        }

        assertTrue(algog1.load("data/G1.json"));
        assertEquals(17,algog1.getGraph().nodeSize());
        assertTrue(algog1.load("data/G2.json"));
        assertEquals(31,algog1.getGraph().nodeSize());
        assertTrue(algog1.load("data/G3.json"));
        assertEquals(48,algog1.getGraph().nodeSize());
    }
}

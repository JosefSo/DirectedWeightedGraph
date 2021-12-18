package tests;

import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static GUI.Ex2.getGrapgAlgo;
import static org.junit.jupiter.api.Assertions.assertTimeout;

class TimeOutTest {
    List<NodeData> cities;
    DirectedWeightedGraphAlgorithms graphAlgo1, graphAlgo2, graphAlgo3, graphAlgo4, graphAlgo5, graphAlgo6 ;

    @BeforeEach
    void setUp() {
        graphAlgo1 = getGrapgAlgo("data/G1.json");
        graphAlgo2 = getGrapgAlgo("data/G2.json");
        graphAlgo3 = getGrapgAlgo("data/G3.json");
        graphAlgo4 = getGrapgAlgo("data/1000Nodes.json");
        graphAlgo5 = getGrapgAlgo("data/10000Nodes.json");
        graphAlgo6 = getGrapgAlgo("data/100000.json");
    }

    @Test
    void test_timeout_tsp() {
        assertTimeout(Duration.ofSeconds(5), () -> tspTest(graphAlgo1)); // pass under 5sec
        assertTimeout(Duration.ofSeconds(10), () -> tspTest(graphAlgo2)); // pass under 10sec
        assertTimeout(Duration.ofSeconds(20), () -> tspTest(graphAlgo3)); // pass under 20sec
        assertTimeout(Duration.ofSeconds(20), () -> tspTest(graphAlgo4)); // pass under 20sec
        assertTimeout(Duration.ofSeconds(20), () -> tspTest(graphAlgo5)); // pass under 20sec
        assertTimeout(Duration.ofSeconds(20), () -> tspTest(graphAlgo6)); // pass under 20sec
    }
    @Test
    void test_timeout_shortestPathDist() {
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathDistTest(graphAlgo1, 0, 16)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathDistTest(graphAlgo2, 0, 30)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathDistTest(graphAlgo3, 0, 47)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathDistTest(graphAlgo4, 0, 999)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(5), () -> shortestPathDistTest(graphAlgo5, 0, 9999)); // pass under 5sec
    }
    @Test
    void test_timeout_shortestPath() {
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathTest(graphAlgo1, 0, 16)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathTest(graphAlgo2, 0, 30)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathTest(graphAlgo3, 0, 47)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(1), () -> shortestPathTest(graphAlgo4, 0, 999)); // pass under 1sec
        assertTimeout(Duration.ofSeconds(5), () -> shortestPathTest(graphAlgo5, 0, 9999)); // pass under 5sec
    }
    @Test
    void test_timeout_center() {
        assertTimeout(Duration.ofSeconds(1), () -> centerTest(graphAlgo1)); // pass under 1 sec
        assertTimeout(Duration.ofSeconds(1), () -> centerTest(graphAlgo2)); // pass under 1 sec
        assertTimeout(Duration.ofSeconds(1), () -> centerTest(graphAlgo3)); // pass under 1 sec
        assertTimeout(Duration.ofSeconds(10), () -> centerTest(graphAlgo4)); // pass under 10 sec
        assertTimeout(Duration.ofSeconds(10), () -> centerTest(graphAlgo5)); // pass under 10 sec
        assertTimeout(Duration.ofSeconds(10), () -> centerTest(graphAlgo6)); // pass under 10 sec


    }
    @Test
    void test_timeout_isConnected() {
        assertTimeout(Duration.ofSeconds(1), () -> isConnectedTest(graphAlgo1)); // pass under _sec
        assertTimeout(Duration.ofSeconds(1), () -> isConnectedTest(graphAlgo2)); // pass under _sec
        assertTimeout(Duration.ofSeconds(1), () -> isConnectedTest(graphAlgo3)); // pass under _sec
        assertTimeout(Duration.ofSeconds(1), () -> isConnectedTest(graphAlgo4)); // pass under _sec
        assertTimeout(Duration.ofSeconds(2), () -> isConnectedTest(graphAlgo5)); // pass under 2 sec
        assertTimeout(Duration.ofSeconds(2), () -> isConnectedTest(graphAlgo6)); // pass under 2 sec
    }


    void tspTest(DirectedWeightedGraphAlgorithms graphAlgorithms) {
        cities = new LinkedList<NodeData>();
        Iterator<NodeData> nodeDataIterator = graphAlgorithms.getGraph().nodeIter();
        while(nodeDataIterator.hasNext()){
            NodeData node = nodeDataIterator.next();
            cities.add(node);
        }
        List<NodeData> ans = new LinkedList<>();
        ans = graphAlgorithms.tsp(cities);
    }
    void shortestPathDistTest(DirectedWeightedGraphAlgorithms graphAlgorithms, int src, int dest){
        graphAlgorithms.shortestPathDist(src,dest);
    }
    void shortestPathTest(DirectedWeightedGraphAlgorithms graphAlgorithms, int src, int dest){
        graphAlgorithms.shortestPath(src,dest);
    }
    void centerTest(DirectedWeightedGraphAlgorithms graphAlgorithms){
        graphAlgorithms.center();
    }
    void isConnectedTest(DirectedWeightedGraphAlgorithms graphAlgorithms){
        graphAlgorithms.isConnected();
    }
}


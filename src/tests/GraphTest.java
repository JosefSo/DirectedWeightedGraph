package tests;

import api.DirectedWeightedGraph;
import api.EdgeData;
import api.NodeData;
import implementation.Geo_Location;
import implementation.Graph;
import implementation.Node;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InvalidAttributeValueException;
import javax.management.ValueExp;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    DirectedWeightedGraph graph1;
    @BeforeEach
    void setUp() {
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
    void getNode() {
        assertEquals(null,graph1.getNode(8));
        NodeData n1 =graph1.getNode(1);
        assertEquals(1,n1.getKey());
        assertEquals(0,n1.getWeight());
        assertEquals(0,n1.getTag());
        assertEquals("",n1.getInfo());
        assertEquals(0,n1.getLocation().x());
        assertEquals(0,n1.getLocation().y());
        assertEquals(0,n1.getLocation().z());
    }

    @Test
    void getEdge() {
        assertEquals(null,graph1.getEdge(1,2));
        EdgeData e1=graph1.getEdge(3,2);
        assertEquals(3,e1.getSrc());
        assertEquals(2,e1.getDest());
        assertEquals(0,e1.getWeight());
        assertEquals(0,e1.getTag());
        assertEquals("",e1.getInfo());

    }

    @Test
    void addNode() {
        assertEquals(4,graph1.nodeSize());
        graph1.addNode(new Node("1,1,1",0));
        assertEquals(4,graph1.nodeSize());
        assertEquals(0,graph1.getNode(0).getLocation().x());
        graph1.addNode(new Node("1,1,1",5));
        assertEquals(5,graph1.nodeSize());
        assertEquals(1,graph1.getNode(5).getLocation().x());
    }

    @Test
    void connect() {
        assertEquals(3,graph1.edgeSize());
        graph1.connect(0,1,3);
        graph1.connect(2,1,4);
        graph1.connect(3,2,5);
        graph1.connect(2,2,5);
        assertEquals(0,graph1.getEdge(0,1).getWeight());
        assertEquals(3,graph1.edgeSize());
        graph1.connect(3,1,3);
        graph1.connect(0,2,4);
        assertEquals(3,graph1.getEdge(3,1).getWeight());
        assertEquals(4,graph1.getEdge(0,2).getWeight());
        assertEquals(5,graph1.edgeSize());
    }

    @Test
    void nodeIter() {
        Iterator<NodeData>itr=graph1.nodeIter();
       // graph1.connect(1,2,5);
        assertEquals(0,itr.next().getKey());
        assertEquals(1,itr.next().getKey());
        assertEquals(2,itr.next().getKey());
        graph1.removeEdge(0,1);
        try {
            itr.next();
            throw new RuntimeException("Expected Exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals("java.lang.RuntimeException: The graph modified.",e.toString());
        }
    }

    @Test
    void edgeIter() {
        Iterator<EdgeData>itr=graph1.edgeIter();
        assertEquals(0,itr.next().getSrc());
        assertEquals(2,itr.next().getSrc());
        graph1.addNode(new Node("9,9,9",4));
        try {
            itr.next();
            throw new RuntimeException("Expected Exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals("java.lang.RuntimeException: The graph modified.",e.toString());
        }
    }

    @Test
    void testEdgeIter() {
        graph1.connect(0,2,0);
        graph1.connect(0,3,0);
        Iterator<EdgeData>itr=graph1.edgeIter(0);
        assertEquals(0,itr.next().getSrc());
        assertEquals(0,itr.next().getSrc());
        graph1.connect(2,3,0);
        try {
            itr.next();
            throw new RuntimeException("Expected Exception.");
        }
        catch(RuntimeException e)
        {
            assertEquals("java.lang.RuntimeException: The graph modified.",e.toString());
        }
    }

    @Test
    void removeNode() {
        graph1.connect(0,2,0);
        graph1.connect(0,3,0);
        assertEquals(4,graph1.nodeSize());
        assertEquals(5,graph1.edgeSize());
        assertEquals(0,graph1.removeNode(0).getKey());
        assertNull(graph1.removeNode(0));
        assertNull(graph1.removeNode(4));
        assertEquals(3,graph1.nodeSize());
        assertEquals(2,graph1.edgeSize());
    }

    @Test
    void removeEdge() {
        assertEquals(3,graph1.edgeSize());
        assertEquals(0,graph1.removeEdge(0,1).getSrc());
        assertNull(graph1.removeEdge(1,1));
        assertNull(graph1.removeEdge(1,0));
        assertEquals(2,graph1.edgeSize());
    }

    @Test
    void nodeSize() {
        assertEquals(4,graph1.nodeSize());
    }

    @Test
    void edgeSize() {
        assertEquals(3,graph1.edgeSize());
    }

    @Test
    void getMC() {
        assertEquals(7,graph1.getMC());
        graph1.addNode(new Node("1,1,1",0));
        graph1.addNode(new Node("1,1,1",5));
        assertEquals(8,graph1.getMC());
        graph1.connect(0,2,0);
        graph1.connect(0,3,0);
        graph1.connect(0,3,0);
        graph1.connect(0,1,0);
        graph1.connect(2,1,0);
        graph1.connect(2,0,0);
        assertEquals(11,graph1.getMC());
        graph1.removeEdge(2,1);
        graph1.removeEdge(0,0);
        graph1.removeEdge(1,2);
        assertEquals(12,graph1.getMC());
        graph1.removeNode(4);
        graph1.removeNode(5);
        assertEquals(13,graph1.getMC());
        graph1.removeNode(0);
        assertEquals(19,graph1.getMC());
    }
}
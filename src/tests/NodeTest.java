package tests;
import api.NodeData;
import implementation.Geo_Location;
import implementation.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    NodeData node1;
    NodeData node2;
    NodeData node3;
    NodeData node4;
    @BeforeEach
    void setUp() {
        node1= new Node();
        node2 = new Node(1,2.1,4,"good node",new Geo_Location(1,1,1));
        node3 = new Node(node2);
        node4 = new Node("5.5,0.002,3.05",6);
    }

    @Test
    void getKey() {
        assertEquals(0,node1.getKey());
        assertEquals(1,node2.getKey());
        assertEquals(1,node3.getKey());
        assertEquals(6,node4.getKey());
    }

    @Test
    void getLocation() {
        assertEquals(0,node1.getLocation().x());
        assertEquals(0,node1.getLocation().y());
        assertEquals(0,node1.getLocation().z());
        assertEquals(1,node2.getLocation().x());
        assertEquals(1,node2.getLocation().y());
        assertEquals(1,node2.getLocation().z());
        assertEquals(1,node3.getLocation().x());
        assertEquals(1,node3.getLocation().y());
        assertEquals(1,node3.getLocation().z());
        assertEquals(5.5,node4.getLocation().x());
        assertEquals(0.002,node4.getLocation().y());
        assertEquals(3.05,node4.getLocation().z());

    }

    @Test
    void setLocation() {
        node1.setLocation(new Geo_Location(4,5,6));
        assertEquals(4,node1.getLocation().x());
        assertEquals(5,node1.getLocation().y());
        assertEquals(6,node1.getLocation().z());
    }

    @Test
    void getWeight() {
        assertEquals(0,node1.getWeight());
        assertEquals(2.1,node2.getWeight());
        assertEquals(2.1,node3.getWeight());
        assertEquals(0,node4.getWeight());
    }

    @Test
    void setWeight() {
        node1.setWeight(3.3);
        assertEquals(3.3,node1.getWeight());
    }

    @Test
    void setInfo() {
        node1.setInfo("the best info ever!!");
        assertEquals("the best info ever!!",node1.getInfo());
    }

    @Test
    void getInfo() {
        assertEquals("",node1.getInfo());
        assertEquals("good node",node2.getInfo());
        assertEquals("good node",node2.getInfo());
        assertEquals("",node4.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(0,node1.getTag());
        assertEquals(4,node2.getTag());
        assertEquals(4,node2.getTag());
        assertEquals(0,node4.getTag());
    }

    @Test
    void setTag() {
        node1.setTag(6);
        assertEquals(6,node1.getTag());
    }

}
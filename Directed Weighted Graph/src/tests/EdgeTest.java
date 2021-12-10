package tests;

import api.EdgeData;
import implementation.Edge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    EdgeData edge1;
    EdgeData edge2;
    EdgeData edge3;
    @BeforeEach
    void setUp() {
        edge1= new Edge(5,7,3,2.5,"good edge");
        edge2= new Edge(4,4.6,2);
        edge3= new Edge(edge1);
    }

    @Test
    void getSrc() {
        assertEquals(5,edge1.getSrc());
        assertEquals(4,edge2.getSrc());
        assertEquals(5,edge3.getSrc());
    }

    @Test
    void getDest() {
        assertEquals(7,edge1.getDest());
        assertEquals(2,edge2.getDest());
        assertEquals(7,edge3.getDest());
    }

    @Test
    void getWeight() {
        assertEquals(2.5,edge1.getWeight());
        assertEquals(4.6,edge2.getWeight());
        assertEquals(2.5,edge3.getWeight());
    }

    @Test
    void getInfo() {
        assertEquals("good edge",edge1.getInfo());
        assertEquals("",edge2.getInfo());
        assertEquals("good edge",edge3.getInfo());
    }

    @Test
    void setInfo() {
        edge2.setInfo("the best edge ever!");
        assertEquals("the best edge ever!",edge2.getInfo());
    }

    @Test
    void getTag() {
        assertEquals(3,edge1.getTag());
        assertEquals(0,edge2.getTag());
        assertEquals(3,edge3.getTag());
    }

    @Test
    void setTag() {
        edge1.setTag(7);
        assertEquals(7,edge1.getTag());
    }
}
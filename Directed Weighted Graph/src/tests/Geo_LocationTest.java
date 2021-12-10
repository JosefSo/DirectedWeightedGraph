package tests;

import implementation.Geo_Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Geo_LocationTest {
    Geo_Location pos1;
    Geo_Location pos2;

    @BeforeEach
    void setUp() {
        pos1 = new Geo_Location(1,2,3);
        pos2 = new Geo_Location(pos1);
    }

    @Test
    void x() {
        assertEquals(1,pos1.x());
        assertEquals(1,pos2.x());
    }

    @Test
    void y() {
        assertEquals(2,pos1.y());
        assertEquals(2,pos2.y());
    }

    @Test
    void z() {
        assertEquals(3,pos1.z());
        assertEquals(3,pos2.z());
    }

    @Test
    void distance() {
        Geo_Location pos3=new Geo_Location(0,0,0);
        assertEquals(0,pos1.distance(pos2));
        assertEquals(0,pos1.distance(pos1));
        assertEquals(3.7416573867739413,pos2.distance(pos3));
    }
}
package implementation;
import api.GeoLocation;
public class Geo_Location implements GeoLocation {
    private double x,y,z;
    public Geo_Location(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public Geo_Location(GeoLocation L){
        this.x=L.x();
        this.y= L.y();
        this.z= L.z();
    }
    @Override
    public double x()
    {
        return this.x;
    }
    @Override
    public double y()
    {
        return this.y;
    }
    @Override
    public double z()
    {
        return this.z;
    }
    @Override
    public double distance(GeoLocation g)
    {
        double dx=g.x()-this.x();
        double dy=g.y()-this.y();
        double dz=g.z()-this.z();
        return  Math.sqrt( Math.pow(dx,2) + Math.pow(dy,2) + Math.pow(dz,2) );
    }

    @Override
    public String toString() {
        return x+","+y+","+z;
    }
}

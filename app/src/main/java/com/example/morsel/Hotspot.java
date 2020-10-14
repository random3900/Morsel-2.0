package com.example.morsel;

public class Hotspot implements Comparable {
    private String Id;
    private int avgnum;
    private double lat;
    private double lon;
    private String name;
    private double dist;

    public int getPackets() {
        return packets;
    }

    public void setPackets(int packets) {
        this.packets = packets;
    }

    private int packets;
    public Hotspot(){
    }

    public Hotspot(int avgnum, double lat, double lon, String name){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.avgnum = avgnum;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getAvgnum() {
        return avgnum;
    }

    public void setAvgnum(int avgnum) {
        this.avgnum = avgnum;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double d){this.dist=d;}



    @Override
    public int compareTo(Object h) {
        double d,d1;
        d1=((Hotspot)h).getDist();
        d=getDist();
        if(d1==d)
            return 0;
        else if(d1>d)
            return 1;
        else
            return -1;
    }
}

package com.example.morsel;

public class Hotspot {
    private String Id;
    private long avgnum;
    private double lat;
    private double lon;
    private String name;


    public Hotspot(){
    }

    public Hotspot(long avgnum, double lat, double lon, String name){
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

    public long getAvgnum() {
        return avgnum;
    }

    public void setAvgnum(long avgnum) {
        this.avgnum = avgnum;
    }
}

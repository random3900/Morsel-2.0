package com.example.morsel;

public class Vol {
    private int vol_id;
    private String vol_loc;
    private int vol_weight;

    public int getVol_id() {
        return vol_id;
    }

    public void setVol_id(int vol_id) {
        this.vol_id = vol_id;
    }

    public String getVol_loc() {
        return vol_loc;
    }

    public void setVol_loc(String vol_loc) {
        this.vol_loc = vol_loc;
    }

    public int getVol_weight() {
        return vol_weight;
    }

    public void setVol_weight(int vol_weight) {
        this.vol_weight = vol_weight;
    }

    public Vol(){
    }
    public Vol(int vol_id,String vol_loc,int vol_weight){
        this.vol_id=vol_id;
        this.vol_loc=vol_loc;
        this.vol_weight=vol_weight;
    }
}

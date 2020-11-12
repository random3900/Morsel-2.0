package com.example.morsel;

public class User {
    private String uName;
    private String Id;
    private Boolean isMod;

    public User(){}

    public User(String uName, Boolean isMod) {
        this.uName = uName;
        this.isMod = isMod;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Boolean getMod() {
        return isMod;
    }

    public void setMod(Boolean mod) {
        isMod = mod;
    }
}

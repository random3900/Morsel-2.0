package com.example.morsel;

public class VolunteerByPlace {
    private String Id;
    private String Name;
    private String Phone;

    public VolunteerByPlace(String name, String phone) {
        Name = name;
        Phone = phone;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

package com.api.dietiestates25.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdModel {
    private int id;
    private double price;
    private String nation;
    private String county;
    private String city;
    private String zipcode;
    private String address;
    private String agent;
    private int nRooms;
    private int nBathrooms;
    private int floor;
    private boolean lift;
    private String energyClass;
    private String description;
    private String type;
    private String adType;
    private int dimentions;
    private String coordinates;

    public AdModel() { }

}

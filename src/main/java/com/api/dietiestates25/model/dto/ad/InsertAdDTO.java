package com.api.dietiestates25.model.dto.ad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertAdDTO {
    private double price;
    private String city;
    private String address;
    private int nRooms;
    private int nBathrooms;
    private int floor;
    private boolean lift;
    private String energyClass;
    private String description;
    private String type;
    private int dimentions;
    private String coordinates;
    private boolean publicTransport350m;
    private boolean school350m;
    private boolean leisurePark350m;
    private boolean privateGarage;
    private boolean condominiumParking;
    private boolean doormanService;
    private boolean airConditioning;
}

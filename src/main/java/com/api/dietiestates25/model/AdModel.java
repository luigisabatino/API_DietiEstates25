package com.api.dietiestates25.model;

import com.api.dietiestates25.model.response.GeoapifyResponse;
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
    private boolean publicTransport350m;
    private boolean school350m;
    private boolean leisurePark350m;

    public AdModel() { }
    public void setPlacesInterest(GeoapifyResponse geoResponse) {
        for(var feature : geoResponse.getFeatures()) {
            if(feature.getProperties()!=null) {
                for(var category : feature.getProperties().getCategories()) {
                    switch(category.trim()){
                        case "education.school":
                            school350m=true;
                            break;
                        case "leisure.park":
                            leisurePark350m=true;
                            break;
                        case "public_transport":
                            publicTransport350m=true;
                            break;
                    }
                    if(school350m&&leisurePark350m&&publicTransport350m)
                        return;
                }
            }
        }
    }

}

package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.Ad.InsertAdDTO;
import com.api.dietiestates25.model.response.GeoapifyResponse;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
@Setter
public class AdModel {
    private int id;
    private double price;
    private String city;
    private String address;
    private String agent;
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
    private String cityName;
    private String region;
    private String province;

    public AdModel() { }
    public AdModel(InsertAdDTO insertAdDTO) {
        price = insertAdDTO.getPrice();
        city = insertAdDTO.getCity();
        address = insertAdDTO.getAddress();
        nRooms = insertAdDTO.getNRooms();
        nBathrooms = insertAdDTO.getNBathrooms();
        floor = insertAdDTO.getFloor();
        lift = insertAdDTO.isLift();
        energyClass = insertAdDTO.getEnergyClass();
        description = insertAdDTO.getDescription();
        type = insertAdDTO.getType();
        dimentions = insertAdDTO.getDimentions();
        coordinates = insertAdDTO.getCoordinates();
        publicTransport350m = insertAdDTO.isPublicTransport350m();
        school350m = insertAdDTO.isSchool350m();
        leisurePark350m = insertAdDTO.isLeisurePark350m();
        privateGarage = insertAdDTO.isPrivateGarage();
        condominiumParking = insertAdDTO.isCondominiumParking();
        doormanService = insertAdDTO.isDoormanService();
        airConditioning = insertAdDTO.isAirConditioning();
    }
    public AdModel(ResultSet rs) {
        try {
            id = rs.getInt("id_ad");
            price = rs.getDouble("price");
            city = rs.getString("city");
            address = rs.getString("address");
            agent = rs.getString("agent");
            nRooms = rs.getInt("n_rooms");
            nBathrooms = rs.getInt("n_bathrooms");
            floor = rs.getInt("floor");
            lift = rs.getBoolean("lift");
            energyClass = rs.getString("energy_class");
            description = rs.getString("description");
            type = rs.getString("ad_type");
            dimentions = rs.getInt("dimensions");
            coordinates = rs.getString("coordinates");
            publicTransport350m = rs.getBoolean("publictransport_proximity");
            school350m = rs.getBoolean("school_proximity");
            leisurePark350m = rs.getBoolean("leisurepark_proximity");
            privateGarage = rs.getBoolean("private_car_garage");
            condominiumParking = rs.getBoolean("condominium_parking");
            doormanService = rs.getBoolean("doorman_service");
            airConditioning = rs.getBoolean("air_conditioning");
            cityName = rs.getString("city_name");
            region = rs.getString("region");
            province = rs.getString("province");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
    public void valorizePlacesInterest(GeoapifyResponse geoResponse) {
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

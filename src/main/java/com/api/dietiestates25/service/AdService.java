package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdService {

    public int insertAd(JdbcTemplate jdbcTemplate, String sessionId, AdModel ad) {
        requiredValuesForAdOperations(ad, AdService.Operation.InsertAd);
        String query = "SELECT * FROM INSERT_AD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, ad.getPrice(), ad.getNation(), ad.getCounty(), ad.getCity(), ad.getZipcode(), ad.getAddress(), ad.getNRooms(), ad.getNBathrooms(), ad.getFloor(), ad.isLift(), ad.getEnergyClass(), ad.getDescription(), ad.getAdType(),ad.getDimentions(),ad.getCoordinates())) );
    }

    public static void requiredValuesForAdOperations(AdModel ad, AdService.Operation operation) {
        if((operation == Operation.InsertAd) && (ad.getPrice()==0))
            throw new RequiredParameterException("price");
        if((operation == Operation.InsertAd) && (ad.getNation().isBlank()||ad.getNation()==null))
            throw new RequiredParameterException("nation");
        if((operation == Operation.InsertAd)&&(ad.getCounty().isBlank()||ad.getCounty()==null))
            throw new RequiredParameterException("county");
        if((operation == Operation.InsertAd)&&(ad.getCity().isBlank()||ad.getCity()==null))
            throw new RequiredParameterException("city");
        if((operation == Operation.InsertAd)&&(ad.getZipcode().isBlank()||ad.getZipcode()==null))
            throw new RequiredParameterException("zip code");
        if((operation == Operation.InsertAd)&&(ad.getAddress().isBlank()||ad.getAddress()==null))
            throw new RequiredParameterException("address");
        if((operation == Operation.InsertAd)&&(ad.getNRooms()==0))
            throw new RequiredParameterException("number of rooms");
        if((operation == Operation.InsertAd)&&(ad.getNBathrooms()==0))
            throw new RequiredParameterException("number of bathrooms");
        if((operation == Operation.InsertAd)&&(ad.getNBathrooms()==0))
            throw new RequiredParameterException("number of bathrooms");
        if((operation == Operation.InsertAd)&&(ad.getAdType().isBlank()||ad.getAdType()==null))
            throw new RequiredParameterException("ad type");
        if((operation == Operation.InsertAd)&&(ad.getDimentions()==0))
            throw new RequiredParameterException("dimentions");
        if((operation == Operation.InsertAd)&&(ad.getCoordinates().isBlank() || ad.getCoordinates()==null))
            throw new RequiredParameterException("coordinates");
    }
    public enum Operation {
        InsertAd;
    }
}

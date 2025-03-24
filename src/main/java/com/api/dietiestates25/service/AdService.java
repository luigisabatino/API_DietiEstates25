package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.request.SearchAdRequest;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdService {

    public int insertAd(JdbcTemplate jdbcTemplate, String sessionId, AdModel ad) {
        requiredValuesForAdOperations(ad, AdService.Operation.InsertAd);
        String query = "SELECT * FROM INSERT_AD(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, ad.getPrice(), ad.getNation(), ad.getCounty(), ad.getCity(), ad.getZipcode(), ad.getAddress(), ad.getNRooms(), ad.getNBathrooms(), ad.getFloor(), ad.isLift(), ad.getEnergyClass(), ad.getDescription(), ad.getType(),ad.getDimentions(),ad.getCoordinates())) );
    }
    public CodeEntitiesResponse<AdModel> searchAd(JdbcTemplate jdbcTemplate, SearchAdRequest ad) {
        var response = new CodeEntitiesResponse<AdModel>();
        if(ad.getId() > 0) {
            response.addInEntities(getAdById(jdbcTemplate, ad.getId()));
            return response;
        }
        requiredValuesForAdOperations(ad, AdService.Operation.SearchAd);
        String query = "SELECT * FROM ADS WHERE AGENT LIKE ? AND PRICE >= ? AND PRICE <= ? AND NATION LIKE ? AND COUNTY LIKE ? AND CITY LIKE ? AND ZIPCODE LIKE ? AND ADDRESS LIKE ? AND N_ROOMS >= ? AND N_BATHROOMS >= ? AND AD_TYPE = ?";
        response.setEntities(jdbcTemplate.query(query, new Object[]{"%" + ad.getAgent() + "%", ad.getPrice(), ad.getMaxPrice(), "%" + ad.getNation() + "%", "%" + ad.getCounty() + "%", "%" + ad.getCity() + "%", "%" + ad.getZipcode() + "%", "%" + ad.getAddress() + "%", ad.getNRooms(), ad.getNBathrooms(), ad.getType()}, (rs, rowNum) -> {
            return new AdModel(rs);
        }));
        return response;
    }
    public AdModel getAdById(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT * FROM ADS WHERE ID_AD = ?";
        return jdbcTemplate.queryForObject(query, (rs, _) -> {
            return new AdModel(rs);
        }, id);
    }
    public int deleteAd(JdbcTemplate jdbcTemplate, String sessionId, int id) {
        String query = "SELECT * FROM INSERT_AD(?,?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, id)));
    }
    public static void requiredValuesForAdOperations(AdModel ad, AdService.Operation operation) {
        if((operation == Operation.InsertAd) && (ad.getPrice()==0))
            throw new RequiredParameterException("price");
        if((operation == Operation.InsertAd) && (ad.getNation()==null || ad.getNation().isBlank()))
            throw new RequiredParameterException("nation");
        if((operation == Operation.InsertAd)&&(ad.getCounty()==null||ad.getCounty().isBlank()))
            throw new RequiredParameterException("county");
        if((operation == Operation.InsertAd)&&(ad.getCity()==null||ad.getCity().isBlank()))
            throw new RequiredParameterException("city");
        if((operation == Operation.InsertAd)&&(ad.getZipcode()==null||ad.getZipcode().isBlank()))
            throw new RequiredParameterException("zip code");
        if((operation == Operation.InsertAd)&&(ad.getAddress()==null||ad.getAddress().isBlank()))
            throw new RequiredParameterException("address");
        if((operation == Operation.InsertAd)&&(ad.getNRooms()==0))
            throw new RequiredParameterException("number of rooms");
        if((operation == Operation.InsertAd)&&(ad.getNBathrooms()==0))
            throw new RequiredParameterException("number of bathrooms");
        if((operation == Operation.InsertAd)&&(ad.getNBathrooms()==0))
            throw new RequiredParameterException("number of bathrooms");
        if((operation == Operation.InsertAd || operation == Operation.SearchAd)&&(ad.getType()==null||ad.getType().isBlank()))
            throw new RequiredParameterException("ad type");
        if((operation == Operation.InsertAd)&&(ad.getDimentions()==0))
            throw new RequiredParameterException("dimentions");
        if((operation == Operation.InsertAd)&&(ad.getCoordinates()==null||ad.getCoordinates().isBlank()))
            throw new RequiredParameterException("coordinates");
    }
    public enum Operation {
        InsertAd,
        SearchAd;
    }
}

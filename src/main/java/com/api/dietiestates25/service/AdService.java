package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdService {

    public int insertAd(JdbcTemplate jdbcTemplate, String sessionId, AdModel ad) {
        requiredValuesForAdOperations(ad, AdService.Operation.InsertAd);
        String query = "SELECT * FROM INSERT_AD(?,?::numeric,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, ad.getPrice(), ad.getCity(), ad.getAddress(), ad.getNRooms(), ad.getNBathrooms(), ad.getFloor(), ad.isLift(), ad.getEnergyClass(), ad.getDescription(), ad.getType(), ad.getDimentions(), ad.getCoordinates(), ad.isSchool350m(), ad.isPublicTransport350m(), ad.isLeisurePark350m(), ad.isPrivateGarage(), ad.isCondominiumParking(), ad.isDoormanService(), ad.isAirConditioning()
        )) );
    }
    public List<AdModel> searchAd(JdbcTemplate jdbcTemplate, SearchAdRequest ad) {
        if(ad.getId() > 0) {
            var response = new ArrayList<AdModel>();
            response.add(getAdById(jdbcTemplate, ad.getId()));
            return response;
        }
        requiredValuesForAdOperations(ad, AdService.Operation.SearchAd);
        String query = "SELECT * FROM ADS_WITH_GEO_DATA WHERE EMAIL LIKE ? AND PRICE BETWEEN ? AND ? AND ADDRESS LIKE ? AND N_ROOMS >= ? AND N_BATHROOMS >= ? AND AD_TYPE = ? AND PROVINCE LIKE ? AND REGION LIKE ? AND CITY LIKE ?;";
        return (jdbcTemplate.query(query, new Object[]{
                "%" + ad.getAgentEmail() + "%",
                ad.getPrice(),
                ad.getMaxPrice(),
                "%" + ad.getAddress() + "%",
                ad.getNRooms(),
                ad.getNBathrooms(),
                ad.getType(),
                "%" + ad.getProvince() + "%",
                "%" + ad.getRegion() + "%",
                "%" + ad.getCity() + "%" }, (rs, rowNum) -> new AdModel(rs)));
    }
    public AdModel getAdById(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT * FROM ADS_WITH_GEO_DATA WHERE ID_AD = ?";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> new AdModel(rs), id);
    }
    public int deleteAd(JdbcTemplate jdbcTemplate, String sessionId, int id) {
        String query = "SELECT * FROM DELETE_AD(?,?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, id)));
    }
    public static void requiredValuesForAdOperations(AdModel ad, AdService.Operation operation) {
        if((operation == Operation.InsertAd) && (ad.getPrice()==0))
            throw new RequiredParameterException("price");
        if((operation == Operation.InsertAd)&&(ad.getCity()==null||ad.getCity().isBlank()))
            throw new RequiredParameterException("city");
        if((operation == Operation.InsertAd)&&(ad.getAddress()==null||ad.getAddress().isBlank()))
            throw new RequiredParameterException("address");
        if((operation == Operation.InsertAd)&&(ad.getNRooms()==0))
            throw new RequiredParameterException("number of rooms");
        if((operation == Operation.InsertAd)&&(ad.getNBathrooms()==0))
            throw new RequiredParameterException("number of bathrooms");
        if((operation == Operation.InsertAd || operation == Operation.SearchAd)&&(ad.getType()==null||ad.getType().isBlank()))
            throw new RequiredParameterException("ad type");
        if((operation == Operation.InsertAd)&&(ad.getDimentions()==0))
            throw new RequiredParameterException("dimentions");
        /*if((operation == Operation.InsertAd)&&(ad.getCoordinates()==null||ad.getCoordinates().isBlank()))
            throw new RequiredParameterException("coordinates");*/
    }
    public enum Operation {
        InsertAd,
        SearchAd
    }
}

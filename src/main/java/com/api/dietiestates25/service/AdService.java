package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdService {

    public int insertAd(JdbcTemplate jdbcTemplate, String sessionId, AdModel ad) {
        requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
        String query = "SELECT * FROM INSERT_AD(?,?::numeric,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, ad.getPrice(), ad.getCity(), ad.getAddress(), ad.getNRooms(), ad.getNBathrooms(), ad.getFloor(), ad.isLift(), ad.getEnergyClass(), ad.getDescription(), ad.getType(), ad.getDimentions(), ad.getCoordinates(), ad.isSchool350m(), ad.isPublicTransport350m(), ad.isLeisurePark350m(), ad.isPrivateGarage(), ad.isCondominiumParking(), ad.isDoormanService(), ad.isAirConditioning()
        ));
    }
    public List<AdWithGeoDataModel> searchAd(JdbcTemplate jdbcTemplate, SearchAdRequest ad) {
        if(ad.getId() >= 0 && ad.areOtherFieldsEmpty()) {
            var response = new ArrayList<AdWithGeoDataModel>();
            response.add(getAdById(jdbcTemplate, ad.getId()));
            return response;
        }

        requiredValuesForAdOperations(ad, AdService.Operation.SEARCH_AD);
        String query = """
            SELECT * FROM ADS_WITH_GEO_DATA
            WHERE EMAIL LIKE ?
            AND (PRICE >= ? AND (? IS NULL OR ? = 0 OR PRICE <= ?))
            AND N_ROOMS >= ?
            AND N_BATHROOMS >= ?
            AND AD_TYPE LIKE ?
        """;

        if (!ad.getLocationAny().isBlank()) {
            query += """
                AND (
                   ADDRESS LIKE ?
                    OR PROVINCE LIKE ?
                    OR REGION LIKE ?
                    OR city_name LIKE ?
                )
            """;

            ad.setAddress(ad.getLocationAny());
            ad.setProvince(ad.getLocationAny());
            ad.setRegion(ad.getLocationAny());
            ad.setCity(ad.getLocationAny());
        }
        else
            query += "AND ADDRESS LIKE ? AND PROVINCE LIKE ? AND REGION LIKE ? AND CITY LIKE ?";

        return jdbcTemplate.query(
                query,
                ps -> {
                    ps.setString(1, "%" + ad.getAgent() + "%");
                    ps.setDouble(2, ad.getPrice());
                    ps.setDouble(3, ad.getMaxPrice());
                    ps.setDouble(4, ad.getMaxPrice());
                    ps.setDouble(5, ad.getMaxPrice());
                    ps.setInt(6, ad.getNRooms());
                    ps.setInt(7, ad.getNBathrooms());
                    ps.setString(8, "%" + ad.getType() + "%");
                    ps.setString(9, "%" + ad.getAddress() + "%");
                    ps.setString(10, "%" + ad.getProvince() + "%");
                    ps.setString(11, "%" + ad.getRegion() + "%");
                    ps.setString(12, "%" + ad.getRegion() + "%");
                    ps.setString(13, "%" + ad.getCity() + "%");
                },
                (rs, rowNum) -> new AdWithGeoDataModel(rs)
        );
    }
    public AdWithGeoDataModel getAdById(JdbcTemplate jdbcTemplate, int id) {
        if (id == 0) {
            String query = "SELECT * FROM ads_with_geo_data ORDER BY id_ad DESC LIMIT 1";
            return jdbcTemplate.queryForObject(query, (rs, ignored) -> new AdWithGeoDataModel(rs));
        }
        
        String query = "SELECT * FROM ADS_WITH_GEO_DATA WHERE ID_AD = ?";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> new AdWithGeoDataModel(rs), id);
    }
    public Integer deleteAd(JdbcTemplate jdbcTemplate, String sessionId, int id) {
        String query = "SELECT * FROM DELETE_AD(?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, id));
    }
    public static void requiredValuesForAdOperations(AdModel ad, AdService.Operation operation) {
        if(operation == Operation.INSERT_AD) {
            if(ad.getPrice()==0)
                throw new RequiredParameterException("price");
            if(ad.getCity()==null||ad.getCity().isBlank())
                throw new RequiredParameterException("city");
            if(ad.getAddress()==null||ad.getAddress().isBlank())
                throw new RequiredParameterException("address");
            if(ad.getNRooms()==0)
                throw new RequiredParameterException("number of rooms");
            if(ad.getNBathrooms()==0)
                throw new RequiredParameterException("number of bathrooms");
            if(ad.getType()==null||ad.getType().isBlank())
                throw new RequiredParameterException("ad type");
            if(ad.getDimentions()==0)
                throw new RequiredParameterException("dimentions");
        }
    }
    public enum Operation {
        INSERT_AD,
        SEARCH_AD
    }
}

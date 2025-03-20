package com.api.dietiestates25.service;

import com.api.dietiestates25.model.BidModel;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CityService {

    public List<String> getItalianRegions(JdbcTemplate jdbcTemplate) {
        String query = "SELECT DISTINCT REGION FROM ITALIAN_CITIES ORDER BY REGION ASC";
        return (jdbcTemplate.query(query, new Object[]{}, (rs, rowNum) -> {
            return rs.getString("region");
        }));
    }

    public List<String> getItalianProvincesByRegion(JdbcTemplate jdbcTemplate, String region) {
        String query = "SELECT DISTINCT PROVINCE FROM ITALIAN_CITIES WHERE REGION = ? ORDER BY PROVINCE ASC";
        return (jdbcTemplate.query(query, new Object[]{region}, (rs, rowNum) -> {
            return rs.getString("province");
        }));
    }

    public List<String> getItalianCitiesByProvince(JdbcTemplate jdbcTemplate, String province) {
        String query = "SELECT DISTINCT NAME FROM ITALIAN_CITIES WHERE PROVINCE = ? ORDER BY NAME ASC";
        return (jdbcTemplate.query(query, new Object[]{province}, (rs, rowNum) -> {
            return rs.getString("name").replaceAll("£","'");
        }));
    }


}

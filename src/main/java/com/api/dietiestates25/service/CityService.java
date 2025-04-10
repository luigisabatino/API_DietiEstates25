package com.api.dietiestates25.service;

import com.api.dietiestates25.model.CityModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
    public List<CityModel> getItalianCitiesByProvince(JdbcTemplate jdbcTemplate, String province) {
        String query = "SELECT DISTINCT NAME, ISTAT_CODE FROM ITALIAN_CITIES WHERE PROVINCE = ? ORDER BY NAME ASC";
        return (jdbcTemplate.query(query, new Object[]{province}, (rs, rowNum) -> {
            return new CityModel(rs);
        }));
    }
}

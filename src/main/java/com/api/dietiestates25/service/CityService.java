package com.api.dietiestates25.service;

import com.api.dietiestates25.model.CityModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityService {

    public List<String> getItalianRegions(JdbcTemplate jdbcTemplate) {
        String query = "SELECT DISTINCT REGION FROM ITALIAN_CITIES ORDER BY REGION ASC";
        return jdbcTemplate.query(
                query,
                ps -> {},
                (rs, rowNum) -> rs.getString("region")
        );
    }
    public List<String> getItalianProvincesByRegion(JdbcTemplate jdbcTemplate, String region) {
        String query = "SELECT DISTINCT PROVINCE FROM ITALIAN_CITIES WHERE REGION = ? ORDER BY PROVINCE ASC";
        return jdbcTemplate.query(
                query,
                ps -> ps.setString(1, region),
                (rs, rowNum) -> rs.getString("province")
        );
    }
    public List<CityModel> getItalianCitiesByProvince(JdbcTemplate jdbcTemplate, String province) {
        String query = "SELECT DISTINCT NAME, ISTAT_CODE FROM ITALIAN_CITIES WHERE PROVINCE = ? ORDER BY NAME ASC";
        return jdbcTemplate.query(
                query,
                ps -> ps.setString(1, province),
                (rs, rowNum) -> new CityModel(rs)
        );
    }
    public String getCityNameByCode(JdbcTemplate jdbcTemplate, String code) {
        String query = "SELECT NAME FROM ITALIAN_CITIES WHERE ISTAT_CODE = ?";
        return jdbcTemplate.queryForObject(query, String.class,
                code);
    }
}

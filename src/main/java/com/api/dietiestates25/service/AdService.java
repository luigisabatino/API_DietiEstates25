package com.api.dietiestates25.service;

import com.api.dietiestates25.model.request.AdRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import com.api.dietiestates25.model.AdModel;

public class AdService {
    public Integer insertAd(JdbcTemplate jdbcTemplate, AdRequest ad) {
        String query = "SELECT * FROM INSERT_AD(?, ?, ?, ?, ?, ?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                ad.getSessionId(), ad.getPrice(), ad.getNation(),ad.getCounty(),ad.getCity(),ad.getZipcode(),ad.getAddress()));
    }
}

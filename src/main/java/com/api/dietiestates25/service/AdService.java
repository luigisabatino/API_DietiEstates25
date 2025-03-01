package com.api.dietiestates25.service;

import com.api.dietiestates25.model.request.AdRequest;
import com.api.dietiestates25.model.response.CodeResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import com.api.dietiestates25.model.AdModel;

public class AdService {

    public CodeResponse insertAd(JdbcTemplate jdbcTemplate, AdRequest ad) {
        String query = "SELECT * FROM INSERT_AD(?, ?, ?, ?, ?, ?, ?)";
        var response = new CodeResponse();
        response.setCode( (jdbcTemplate.queryForObject(query, Integer.class,
                ad.getSessionId(), ad.getPrice(), ad.getNation(),ad.getCounty(),ad.getCity(),ad.getZipcode(),ad.getAddress())) );
        response.setMessage(response.getCode() == 0 ? "Ad inserted with success"
                : (response.getCode() == -1 ? "Error: User cannot insert an Ad"
                : "Error: session not valid" ));
        return response;
    }
}

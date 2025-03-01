package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.request.EntityRequest;
import com.api.dietiestates25.model.response.CodeResponse;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdService {

    public CodeResponse insertAd(JdbcTemplate jdbcTemplate, String sessionId, AdModel ad) {
        String query = "SELECT * FROM INSERT_AD(?, ?, ?, ?, ?, ?, ?)";
        var response = new CodeResponse();
        response.setCode( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, ad.getPrice(), ad.getNation(), ad.getCounty(), ad.getCity(), ad.getZipcode(), ad.getAddress())) );
        response.setMessage(response.getCode() == 0 ? "Ad inserted with success"
                : (response.getCode() == -1 ? "Error: session not valid"
                : "Error: User cannot insert an Ad" ));
        return response;
    }
}

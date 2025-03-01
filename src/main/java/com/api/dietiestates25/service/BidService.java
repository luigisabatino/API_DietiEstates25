package com.api.dietiestates25.service;

import com.api.dietiestates25.model.request.EntityRequest;
import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.response.CodeResponse;
import org.springframework.jdbc.core.JdbcTemplate;

public class BidService {

    public CodeResponse insertBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        String query = "SELECT * FROM INSERT_BID(?, ?, ?, ?)";
        var response = new CodeResponse();
        response.setCode( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getAd(), bid.getAmount(), bid.getMessage() )));
        response.setMessage(response.getCode() == 0 ? "Bid inserted with success"
                : (response.getCode() == -1 ? "Error: User cannot insert a Bid"
                : "Error: session not valid" ));
        return response;
    }

    public CodeResponse cancelBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM CANCEL_BID(?, ?)";
        var response = new CodeResponse();
        response.setCode( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId )));
        response.setMessage(response.getCode() == 0 ? "Bid inserted with success"
                : (response.getCode() == -1 ? "Error: session not valid"
                : "Error: User cannot cancel this Bid" ));
        return response;
    }

    public CodeResponse refuseBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM REFUSE_BID(?, ?)";
        var response = new CodeResponse();
        response.setCode( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId )));
        response.setMessage(response.getCode() == 0 ? "Bid inserted with success"
                : (response.getCode() == -1 ? "Error: session not valid"
                : "Error: User cannot refuse this Bid" ));
        return response;
    }
}

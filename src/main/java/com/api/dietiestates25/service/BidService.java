package com.api.dietiestates25.service;

import com.api.dietiestates25.model.request.EntityRequest;
import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.response.CodeResponse;
import org.springframework.jdbc.core.JdbcTemplate;

public class BidService {

    public int insertBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        String query = "SELECT * FROM INSERT_BID(?, ?, ?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getAd(), bid.getAmount(), bid.getMessage() )));
    }

    public int cancelBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM CANCEL_BID(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId )));
    }

    public int refuseBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM REFUSE_BID(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId )));
    }
}

package com.api.dietiestates25.service;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BidService {
    public int insertBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        requiredValuesForBidOperations(bid, Operation.InsertBid);
        String query = "SELECT * FROM INSERT_BID(?, ?, ?, ?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getAd(), bid.getAmount(), bid.getOffererMessage(), bid.getAgentMessage() )));
    }
    public int cancelBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM CANCEL_BID(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId )));
    }
    public int refuseBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        requiredValuesForBidOperations(bid, Operation.RefuseBid);
        String query = "SELECT * FROM REFUSE_BID(?, ?,?,?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getId(), bid.getAgentMessage(), bid.getAmount() )));
    }
    public CodeEntitiesResponse<BidModel> getBids(JdbcTemplate jdbcTemplate, BidsKey key, String value) {
        var response = new CodeEntitiesResponse<BidModel>();
        switch(key) {
            case BidsKey.ad :
                response.setEntities(getBidsFromAd(jdbcTemplate, Integer.parseInt(value)));
                break;
            case BidsKey.bid_id :
                response.addInEntities(getBidFromId(jdbcTemplate, Integer.parseInt(value)));
                break;
            case BidsKey.offerer :
                response.setEntities(getBidsFromOfferer(jdbcTemplate, value));
                break;
        }
        return response;
    }
    private List<BidModel> getBidsFromAd(JdbcTemplate jdbcTemplate, int ad) {
        String query = "SELECT * FROM BIDS WHERE AD = ?";
        return (jdbcTemplate.query(query, new Object[]{ad}, (rs, rowNum) -> {
            return new BidModel(rs);
        }));
    }
    private List<BidModel> getBidsFromOfferer(JdbcTemplate jdbcTemplate, String offerer) {
        String query = "SELECT * FROM BIDS WHERE OFFERER = ?";
        return (jdbcTemplate.query(query, new Object[]{offerer}, (rs, rowNum) -> {
            return new BidModel(rs);
        }));
    }
    public BidModel getBidFromId(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT * FROM BIDS WHERE BID_ID = ?";
        return jdbcTemplate.queryForObject(query, (rs, _) -> {
            return new BidModel(rs);
        }, id);
    }
    public static void requiredValuesForBidOperations(BidModel bid, BidService.Operation operation) {
        if(operation == Operation.RefuseBid && (bid.getId()==0))
            throw new RequiredParameterException("bid_id");
        if((operation == Operation.RefuseBid || operation == Operation.InsertBid) && (bid.getAgentMessage()==null))
            bid.setAgentMessage("");
        if((operation == Operation.RefuseBid || operation == Operation.InsertBid) && (bid.getOffererMessage()==null))
            bid.setOffererMessage("");
        if((operation == Operation.InsertBid) && ((bid.getAd()==0)))
            throw new RequiredParameterException("ad");
        if((operation == Operation.InsertBid) && ((bid.getAmount()==0)))
            throw new RequiredParameterException("amount");
    }
    public enum Operation {
        RefuseBid,
        InsertBid;
    }
    public enum BidsKey {
        ad,
        bid_id,
        offerer;
    }
}

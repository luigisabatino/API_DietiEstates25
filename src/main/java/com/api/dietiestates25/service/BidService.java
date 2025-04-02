package com.api.dietiestates25.service;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.BidWithCounterofferModel;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
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
    public int acceptOrRefuseBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        requiredValuesForBidOperations(bid, Operation.AcceptOrRefuseBid);
        String query = "SELECT * FROM ACCEPT_REFUSE_BID(?,?,?,?,?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getId(), bid.getAgentMessage(), bid.getAmount(), bid.getStatus() )));
    }
    public int cancelCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, int coId) {
        String query = "SELECT * FROM CANCEL_Counteroffer(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, coId )));
    }
    public int refuseCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, int coId) {
        String query = "SELECT * FROM REFUSE_Counteroffer(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, coId )));
    }
    public List<BidWithCounterofferModel> getBids(JdbcTemplate jdbcTemplate, BidsKey key, String value) {
        List<BidWithCounterofferModel> response = new ArrayList<BidWithCounterofferModel>();
        switch(key) {
            case BidsKey.ad :
                response = getBidsFromAd(jdbcTemplate, Integer.parseInt(value));
                break;
            case BidsKey.bid_id :
                response.add(getBidFromId(jdbcTemplate, Integer.parseInt(value)));
                break;
            case BidsKey.offerer :
                response = getBidsFromOfferer(jdbcTemplate, value);
                break;
        }
        return response;
    }
    private List<BidWithCounterofferModel> getBidsFromAd(JdbcTemplate jdbcTemplate, int ad) {
        String query = "SELECT B.*, C.co_id, C.parent_bid,C.amount as co_amount, C.status as co_status FROM BIDS B LEFT JOIN COUNTEROFFER C ON B.BID_ID = C.PARENT_BID WHERE B.AD = ?";
        return (jdbcTemplate.query(query, new Object[]{ad}, (rs, rowNum) -> {
            return new BidWithCounterofferModel(rs);
        }));
    }
    private List<BidWithCounterofferModel> getBidsFromOfferer(JdbcTemplate jdbcTemplate, String offerer) {
        String query = "SELECT B.*, C.co_id, C.parent_bid,C.amount as co_amount, C.status as co_status FROM BIDS B LEFT JOIN COUNTEROFFER C ON B.BID_ID = C.PARENT_BID WHERE B.OFFERER = ?";
        return (jdbcTemplate.query(query, new Object[]{offerer}, (rs, rowNum) -> {
            return new BidWithCounterofferModel(rs);
        }));
    }
    public BidWithCounterofferModel getBidFromId(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT B.*, C.co_id, C.parent_bid,C.amount as co_amount, C.status as co_status FROM BIDS B LEFT JOIN COUNTEROFFER C ON B.BID_ID = C.PARENT_BID WHERE B.BID_ID = ?";
        return jdbcTemplate.queryForObject(query, (rs, _) -> {
            return new BidWithCounterofferModel(rs);
        }, id);
    }
    public static void requiredValuesForBidOperations(BidModel bid, BidService.Operation operation) {
        if(operation == Operation.AcceptOrRefuseBid && (bid.getId()==0))
            throw new RequiredParameterException("bid_id");
        if((operation == Operation.AcceptOrRefuseBid || operation == Operation.InsertBid) && (bid.getAgentMessage()==null))
            bid.setAgentMessage("");
        if((operation == Operation.AcceptOrRefuseBid || operation == Operation.InsertBid) && (bid.getOffererMessage()==null))
            bid.setOffererMessage("");
        if((operation == Operation.InsertBid) && ((bid.getAd()==0)))
            throw new RequiredParameterException("ad");
        if((operation == Operation.InsertBid) && ((bid.getAmount()==0)))
            throw new RequiredParameterException("amount");
        if((operation == Operation.AcceptOrRefuseBid) && (bid.getStatus().isBlank() || bid.getStatus().isBlank()))
            throw new RequiredParameterException("status");
    }
    public enum Operation {
        AcceptOrRefuseBid,
        InsertBid;
    }
    public enum BidsKey {
        ad,
        bid_id,
        offerer;
    }
}

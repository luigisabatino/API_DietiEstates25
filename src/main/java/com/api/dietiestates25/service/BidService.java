package com.api.dietiestates25.service;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.CounterOfferModel;
import com.api.dietiestates25.model.extention.BidWithCounterofferModel;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BidService {
    public int insertBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        requiredValuesForBidOperations(bid, Operation.InsertBid);
        String query = "SELECT insert_bid(?::VARCHAR, ?::INTEGER, ?::NUMERIC, ?::VARCHAR, ?::VARCHAR);";
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
        String query = "SELECT accept_refuse_bid(?::VARCHAR, ?::INTEGER, ?::VARCHAR, ?::NUMERIC, ?::VARCHAR)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getId(), bid.getAgentMessage(), bid.getAmount(), bid.getStatus() )));
    }
    public int cancelCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, int coId) {
        String query = "SELECT * FROM CANCEL_Counteroffer(?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, coId )));
    }
    public int acceptOrRefuseCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, CounterOfferModel co) {
        requiredValuesForBidOperations(new BidModel(co), Operation.AcceptOrRefuseCounteroffer);
        String query = "SELECT accept_refuse_counteroffer(?, ?, ?)";
        return ( (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, co.getId(), co.getStatus() )));
    }

    public List<BidWithCounterofferModel> getBids(JdbcTemplate jdbcTemplate, BidsKey key, String value) {
        List<BidWithCounterofferModel> response = new ArrayList<BidWithCounterofferModel>();
        switch(key) {
            case ad :
                response = getBidsFromAd(jdbcTemplate, Integer.parseInt(value));
                break;
            case bid_id :
                response.add(getBidFromId(jdbcTemplate, Integer.parseInt(value)));
                break;
            case offerer :
                response = getBidsFromOfferer(jdbcTemplate, value);
                break;
        }
        return response;
    }
    private List<BidWithCounterofferModel> getBidsFromAd(JdbcTemplate jdbcTemplate, int ad) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE B.ad = ?";
        return (jdbcTemplate.query(query, new Object[]{ad}, (rs, rowNum) -> {
            return new BidWithCounterofferModel(rs);
        }));
    }
    private List<BidWithCounterofferModel> getBidsFromOfferer(JdbcTemplate jdbcTemplate, String offerer) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE B.offerer = ?";
        return (jdbcTemplate.query(query, new Object[]{offerer}, (rs, rowNum) -> {
            return new BidWithCounterofferModel(rs);
        }));
    }
    public BidWithCounterofferModel getBidFromId(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE B.bid_id = ?";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> {
            return new BidWithCounterofferModel(rs);
        }, id);
    }
    public static void requiredValuesForBidOperations(BidModel bid, BidService.Operation operation) {
        if((operation == Operation.AcceptOrRefuseBid||operation == Operation.AcceptOrRefuseCounteroffer) && (bid.getId()==0))
            throw new RequiredParameterException("id");
        if((operation == Operation.AcceptOrRefuseBid || operation == Operation.InsertBid) && (bid.getAgentMessage()==null))
            bid.setAgentMessage("");
        if((operation == Operation.AcceptOrRefuseBid || operation == Operation.InsertBid) && (bid.getOffererMessage()==null))
            bid.setOffererMessage("");
        if((operation == Operation.InsertBid) && ((bid.getAd()==0)))
            throw new RequiredParameterException("ad");
        if((operation == Operation.InsertBid) && ((bid.getAmount()==0)))
            throw new RequiredParameterException("amount");
        if((operation == Operation.AcceptOrRefuseBid || operation == Operation.AcceptOrRefuseCounteroffer) && (bid.getStatus().isBlank()))
            throw new RequiredParameterException("status");
    }
    public enum Operation {
        AcceptOrRefuseBid,
        InsertBid,
        AcceptOrRefuseCounteroffer
    }
    public enum BidsKey {
        ad,
        bid_id,
        offerer
    }
}

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
        requiredValuesForBidOperations(bid, Operation.INSERT_BID);
        String query = "SELECT insert_bid(?::VARCHAR, ?::INTEGER, ?::NUMERIC, ?::VARCHAR, ?::VARCHAR);";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getAd(), bid.getAmount(), bid.getOffererMessage(), bid.getAgentMessage() ));
    }
    public Integer cancelBid(JdbcTemplate jdbcTemplate, String sessionId, int bidId) {
        String query = "SELECT * FROM CANCEL_BID(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bidId ));
    }
    public int acceptOrRefuseBid(JdbcTemplate jdbcTemplate, String sessionId, BidModel bid) {
        requiredValuesForBidOperations(bid, Operation.ACCEPT_OR_REFUSE_BID);
        String query = "SELECT accept_refuse_bid(?::VARCHAR, ?::INTEGER, ?::VARCHAR, ?::NUMERIC, ?::VARCHAR)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, bid.getId(), bid.getAgentMessage(), bid.getAmount(), bid.getStatus() ));
    }
    public Integer cancelCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, int coId) {
        String query = "SELECT * FROM CANCEL_Counteroffer(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, coId ));
    }
    public int acceptOrRefuseCounteroffer(JdbcTemplate jdbcTemplate, String sessionId, CounterOfferModel co) {
        requiredValuesForBidOperations(new BidModel(co), Operation.ACCEPT_OR_REFUSE_COUNTEROFFER);
        String query = "SELECT accept_refuse_counteroffer(?, ?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId, co.getId(), co.getStatus() ));
    }

    public List<BidWithCounterofferModel> getBids(JdbcTemplate jdbcTemplate, BidsKey key, String value) {
        List<BidWithCounterofferModel> response = new ArrayList<>();
        switch(key) {
            case AD:
                response = getBidsFromAd(jdbcTemplate, Integer.parseInt(value));
                break;
            case BID_ID:
                response.add(getBidFromId(jdbcTemplate, Integer.parseInt(value)));
                break;
            case OFFERER:
                response = getBidsFromOfferer(jdbcTemplate, value);
                break;
        }
        return response;
    }
    private List<BidWithCounterofferModel> getBidsFromAd(JdbcTemplate jdbcTemplate, int ad) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE ad = ?";
        return jdbcTemplate.query(
                query,
                ps -> ps.setInt(1, ad),
                (rs, rowNum) -> new BidWithCounterofferModel(rs)
        );
    }

    private List<BidWithCounterofferModel> getBidsFromOfferer(JdbcTemplate jdbcTemplate, String offerer) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE offerer = ?";
        return jdbcTemplate.query(
                query,
                ps -> ps.setString(1, offerer),
                (rs, rowNum) -> new BidWithCounterofferModel(rs)
        );
    }

    public BidWithCounterofferModel getBidFromId(JdbcTemplate jdbcTemplate, int id) {
        String query = "SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE bid_id = ?";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> new BidWithCounterofferModel(rs)
        , id);
    }
    public static void requiredValuesForBidOperations(BidModel bid, BidService.Operation operation) {
        if(operation == Operation.ACCEPT_OR_REFUSE_BID) {
            if(bid.getId()==0)
                throw new RequiredParameterException("id");
            if(bid.getAgentMessage()==null)
                bid.setAgentMessage("");
            if(bid.getOffererMessage()==null)
                bid.setOffererMessage("");
            if(bid.getStatus().isBlank())
                throw new RequiredParameterException("status");
        }
        else if(operation == Operation.ACCEPT_OR_REFUSE_COUNTEROFFER) {
            if(bid.getId()==0)
                throw new RequiredParameterException("id");
            if(bid.getStatus().isBlank())
                throw new RequiredParameterException("status");
        }
        else if(operation == Operation.INSERT_BID) {
            if(bid.getAgentMessage()==null)
                bid.setAgentMessage("");
            if(bid.getOffererMessage()==null)
                bid.setOffererMessage("");
            if(bid.getAd()==0)
                throw new RequiredParameterException("ad");
            if(bid.getAmount()==0)
                throw new RequiredParameterException("amount");
        }
    }
    public enum Operation {
        ACCEPT_OR_REFUSE_BID,
        INSERT_BID,
        ACCEPT_OR_REFUSE_COUNTEROFFER
    }
    public enum BidsKey {
        AD,
        BID_ID,
        OFFERER
    }
}

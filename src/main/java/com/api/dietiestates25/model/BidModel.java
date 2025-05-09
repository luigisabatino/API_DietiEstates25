package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.bid.AcceptOrRefuseBidDTO;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class BidModel {
    private int ad;
    private double amount;
    private String agentMessage;
    private String offerer;
    private String offererMessage;
    private String firstName;
    private String lastName;
    private int id;
    private String status;
    private String timestamp;

    public BidModel() {}

    public BidModel(ResultSet rs) {
        try {
            id = rs.getInt("bid_id");
            agentMessage = rs.getString("agent_message");
            offerer = rs.getString("offerer");
            offererMessage = rs.getString("offerer_message");
            ad = rs.getInt("ad");
            amount = rs.getDouble("amount");
            status = rs.getString("status");
            timestamp = rs.getString("creation_time");
            firstName = rs.getString("firstname");
            lastName = rs.getString("lastname");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
    public BidModel(InsertBidDTO insertDto) {
        agentMessage = insertDto.getAgentMessage();
        offererMessage = insertDto.getOffererMessage();
        ad = insertDto.getAd();
        amount = insertDto.getAmount();
    }
    public BidModel(AcceptOrRefuseBidDTO arDto) {
        agentMessage = arDto.getAgentMessage();
        offererMessage = arDto.getOffererMessage();
        id = arDto.getId();
        amount = arDto.getAmount();
        status = arDto.getStatus();
    }
    public BidModel(CounterOfferModel co) {
        id = co.getId();
        status = co.getStatus();
        amount = co.getAmount();
    }
}
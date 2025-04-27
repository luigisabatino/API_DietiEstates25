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
    private String offerer;
    private String agentMessage;
    private String timestamp;
    private String offererMessage;
    private int id;
    private String status;
    private String firstname, lastname;
    public BidModel() { }
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
            firstname = rs.getString("firstname");
            lastname = rs.getString("lastname");
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
}
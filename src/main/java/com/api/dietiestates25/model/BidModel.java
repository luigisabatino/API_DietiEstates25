package com.api.dietiestates25.model;

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
    private String offererMessage;
    private int id;
    private String status;
    public BidModel() { }
    public BidModel(ResultSet rs) {
        try {
            id = rs.getInt("bid_id");
            agentMessage = rs.getString("agent_message");
            offererMessage = rs.getString("offerer_message");
            ad = rs.getInt("ad");
            amount = rs.getDouble("amount");
            status = rs.getString("status");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
}

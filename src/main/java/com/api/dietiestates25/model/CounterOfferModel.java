package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.counteroffer.AcceptOrRefuseCounterofferDTO;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class CounterOfferModel{
    private int parentBid;
    private double amount;
    private int id;
    private String status;
    public CounterOfferModel() { }
    public CounterOfferModel(ResultSet rs, boolean isWithBid) {
        try {
            id = rs.getInt("co_id");
            parentBid = rs.getInt("parent_bid");
            amount = rs.getDouble( ((isWithBid) ? "co_amount" : "amount") );
            status = rs.getString( ((isWithBid) ? "co_status" : "status") );
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
    public CounterOfferModel(AcceptOrRefuseCounterofferDTO arDto) {
        status = arDto.getStatus();
        id = arDto.getId();
    }


}
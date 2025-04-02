package com.api.dietiestates25.model;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class BidWithCounterofferModel extends BidModel {
    private CounterOfferModel counterOffer;
    public BidWithCounterofferModel() {
        super();
        counterOffer = new CounterOfferModel();
    }
    public BidWithCounterofferModel(ResultSet rs) {
        super(rs);
        try {
            if(rs.getInt("co_id")!=0)
                counterOffer = new CounterOfferModel(rs,true);
            else
                counterOffer = null;
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
}

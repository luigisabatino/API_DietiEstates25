package com.api.dietiestates25.model.extention;
import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.CounterOfferModel;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class BidWithCounterofferModel extends BidModel {
    private CounterOfferModel counterOffer;
    private String firstname;
    private String lastname;
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
            firstname = rs.getString("firstname");
            lastname = rs.getString("lastname");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
}
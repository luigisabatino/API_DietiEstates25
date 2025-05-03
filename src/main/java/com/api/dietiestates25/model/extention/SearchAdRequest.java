package com.api.dietiestates25.model.extention;

import com.api.dietiestates25.model.AdModel;

import lombok.Setter;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class SearchAdRequest extends AdModel {
    private double maxPrice;
    private String region;
    private String province;
    public SearchAdRequest() {}
    public SearchAdRequest(ResultSet rs) {
        super(rs);
        try {
            region = rs.getString("region");
            province = rs.getString("province");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }

    public double getMaxPrice() {
        return ((maxPrice==0) ? Double.MAX_VALUE : maxPrice);
    }
    public String getAgent() {
        return ((super.getAgent()==null) ? "" : super.getAgent());
    }
    public String getProvince() {
        return ((province==null) ? "" : province);
    }
    public String getRegion() {
        return ((region==null) ? "" : region);
    }
    public String getCity() {
        return ((super.getCity()==null) ? "" : super.getCity());
    }
    public String getAddress() {
        return ((super.getAddress()==null) ? "" : super.getAddress());
    }

    public boolean areOtherFieldsEmpty() {
        return maxPrice == 0 && super.getAgent() == null && province == null && region == null && super.getCity() == null && super.getAddress() == null && super.getNBathrooms() <= 0 && super.getNRooms() <= 0;
    }
}

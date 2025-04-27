package com.api.dietiestates25.model.extention;

import com.api.dietiestates25.model.AdModel;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class SearchAdRequest extends AdModel {
    private double maxPrice;
    private String agentEmail;

    public double getMaxPrice() {
        return ((maxPrice==0) ? Double.MAX_VALUE : maxPrice);
    }
    public String getAgentEmail() { return agentEmail == null ? "" : agentEmail; }
    public String getProvince() {
        return ((super.getProvince()==null) ? "" : super.getProvince());
    }
    public String getRegion() {
        return ((super.getRegion()==null) ? "" : super.getRegion());
    }
    public String getCity() {
        return ((super.getCity()==null) ? "" : super.getCity());
    }
    public String getAddress() {
        return ((super.getAddress()==null) ? "" : super.getAddress());
    }
}

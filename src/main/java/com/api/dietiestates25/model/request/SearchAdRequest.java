package com.api.dietiestates25.model.request;

import com.api.dietiestates25.model.AdModel;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class SearchAdRequest extends AdModel {
    private double maxPrice;
    public double getMaxPrice() {
        return ((maxPrice==0) ? Double.MAX_VALUE : maxPrice);
    }

    public String getAgent() {
        return ((super.getAgent()==null) ? "" : super.getAgent());
    }

    public String getNation() {
        return ((super.getNation()==null) ? "" : super.getNation());
    }

    public String getCounty() {
        return ((super.getCounty()==null) ? "" : super.getCounty());
    }

    public String getCity() {
        return ((super.getCity()==null) ? "" : super.getCity());
    }

    public String getAddress() {
        return ((super.getAddress()==null) ? "" : super.getAddress());
    }

    public String getZipcode() {
        return ((super.getZipcode()==null) ? "" : super.getZipcode());
    }

}

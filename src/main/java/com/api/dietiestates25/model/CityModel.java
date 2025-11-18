package com.api.dietiestates25.model;

import lombok.Setter;
import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class CityModel {
    private String name;
    private String code;

    public CityModel() { }
    public CityModel(ResultSet rs) {
        try {
            name = rs.getString("name").replace("£","'");
            code = rs.getString("istat_code");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
}

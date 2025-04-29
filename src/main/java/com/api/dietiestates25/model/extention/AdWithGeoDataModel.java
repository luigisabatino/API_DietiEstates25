package com.api.dietiestates25.model.extention;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.UserModel;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class AdWithGeoDataModel extends SearchAdRequest {
    private UserModel agentDetail;
    private String cityName;
    public AdWithGeoDataModel() {}
    public AdWithGeoDataModel(ResultSet rs) {
        super(rs);
        try {
            agentDetail = new UserModel(rs);
            cityName = rs.getString("city_name");
        }
        catch(SQLException ex) {
            //TO DO
        }
    }
}

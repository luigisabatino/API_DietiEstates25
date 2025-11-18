package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.ad.InsertAdDTO;
import com.api.dietiestates25.model.response.GeoapifyResponse;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdModelTest {

    @Test
    void testConstructorInsertDto() {
        InsertAdDTO dto = new InsertAdDTO();
        dto.setPrice(100000);
        AdModel ad = new AdModel(dto);
        assertEquals(100000, ad.getPrice());
    }
    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getInt("id_ad")).thenReturn(1);
        when(rsMock.getDouble("price")).thenReturn(456.21);
        when(rsMock.getString("city")).thenReturn("Test");
        when(rsMock.getString("address")).thenReturn("via Test");
        when(rsMock.getInt("n_rooms")).thenReturn(2);
        when(rsMock.getInt("n_bathrooms")).thenReturn(1);
        when(rsMock.getInt("floor")).thenReturn(1);
        when(rsMock.getBoolean("lift")).thenReturn(true);
        when(rsMock.getString("energy_class")).thenReturn("E");
        when(rsMock.getString("description")).thenReturn("descrcizione di test");
        when(rsMock.getString("coordinates")).thenReturn("");
        when(rsMock.getBoolean("publictransport_proximity")).thenReturn(true);
        when(rsMock.getBoolean("school_proximity")).thenReturn(false);
        when(rsMock.getBoolean("leisurepark_proximity")).thenReturn(false);
        when(rsMock.getBoolean("private_car_garage")).thenReturn(true);
        when(rsMock.getBoolean("doorman_service")).thenReturn(false);
        when(rsMock.getBoolean("private_car_garage")).thenReturn(true);
        when(rsMock.getBoolean("air_conditioning")).thenReturn(false);
        AdModel ad = new AdModel(rsMock);
        assertEquals(1, ad.getFloor());
    }
    @Test
    void testValorizePlacesInterest() {
        GeoapifyResponse geoResponse = new GeoapifyResponse();
        List<GeoapifyResponse.Feature> features = new ArrayList<>();
        var f = new GeoapifyResponse.Feature();
        GeoapifyResponse.Property p = new GeoapifyResponse.Property();
        var listCategories = new ArrayList<String>();
        listCategories.add("education.school");
        listCategories.add("leisure.park");
        listCategories.add("public_transport");
        p.setCategories(listCategories);
        f.setProperties(p);
        features.add(f);
        geoResponse.setFeatures(features);
        AdModel ad = new AdModel();
        ad.valorizePlacesInterest(geoResponse);
        assertEquals(true, ad.isLeisurePark350m());
    }

}

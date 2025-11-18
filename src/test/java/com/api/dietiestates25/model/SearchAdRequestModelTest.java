package com.api.dietiestates25.model;

import com.api.dietiestates25.model.extention.SearchAdRequest;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SearchAdRequestModelTest {

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
        when(rsMock.getString("region")).thenReturn("Campania");
        when(rsMock.getString("province")).thenReturn("Napoli");
        SearchAdRequest ad = new SearchAdRequest(rsMock);
        assertEquals(1, ad.getFloor());
    }

    @Test
    void testGets() {
        SearchAdRequest ad = new SearchAdRequest();
        assertEquals(Double.MAX_VALUE, ad.getMaxPrice());
        assertEquals("", ad.getAgent());
        assertEquals("", ad.getProvince());
        assertEquals("", ad.getRegion());
        assertEquals("", ad.getCity());
        assertEquals("", ad.getAddress());
        assertEquals("", ad.getType());
        assertEquals("", ad.getLocationAny());
        assertTrue(ad.areOtherFieldsEmpty());
    }
}
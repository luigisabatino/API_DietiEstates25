package com.api.dietiestates25.model;

import com.api.dietiestates25.model.extention.BidWithCounterofferModel;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidWithCounterofferModelTest {

    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getInt("co_id")).thenReturn(1);
        when(rsMock.getString("firstname")).thenReturn("Luigi");
        when(rsMock.getString("lastname")).thenReturn("Sabatino");
        when(rsMock.getInt("parent_bid")).thenReturn(2);
        when(rsMock.getDouble("co_amount")).thenReturn(123.50);
        when(rsMock.getString("co_status")).thenReturn("A");
        BidWithCounterofferModel bidco = new BidWithCounterofferModel(rsMock);
        assertEquals("Luigi", bidco.getFirstName());
    }

    @Test
    void testConstructor() {
        BidWithCounterofferModel bidco = new BidWithCounterofferModel();
        assertNotNull(bidco);
    }
}

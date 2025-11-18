package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.bid.AcceptOrRefuseBidDTO;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BidModelTest {

    @Test
    void testConstructorInsertDto() {
        InsertBidDTO dto = new InsertBidDTO();
        dto.setAd(1);
        BidModel bid = new BidModel(dto);
        assertEquals(1, bid.getAd());
    }
    @Test
    void testConstructorAcceptOrRefuseDto() {
        AcceptOrRefuseBidDTO dto = new AcceptOrRefuseBidDTO();
        dto.setId(1);
        BidModel bid = new BidModel(dto);
        assertEquals(1, bid.getId());
    }
    @Test
    void testConstructorCounterOffer() {
        CounterOfferModel co = new CounterOfferModel();
        co.setId(1);
        BidModel bid = new BidModel(co);
        assertEquals(1, bid.getId());
    }
    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getInt("id_ad")).thenReturn(1);
        when(rsMock.getInt("bid_id")).thenReturn(2);
        when(rsMock.getString("agent_message")).thenReturn("agent");
        when(rsMock.getString("offerer")).thenReturn("offerer");
        when(rsMock.getString("offerer_message")).thenReturn("offerer");
        when(rsMock.getInt("ad")).thenReturn(1);
        when(rsMock.getDouble("amount")).thenReturn(2.0);
        when(rsMock.getString("status")).thenReturn("accepted");
        when(rsMock.getString("creation_time")).thenReturn(null);
        when(rsMock.getString("firstname")).thenReturn("firstname");
        when(rsMock.getString("lastname")).thenReturn("lastname");
        BidModel bid = new BidModel(rsMock);
        assertEquals(2, bid.getId());
    }
}

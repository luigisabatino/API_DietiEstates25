package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.counteroffer.AcceptOrRefuseCounterofferDTO;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CounterOfferModelTest {

    @Test
    void testConstructorAcceptOrRefuseDto() {
        AcceptOrRefuseCounterofferDTO dto = new AcceptOrRefuseCounterofferDTO();
        dto.setId(1);
        CounterOfferModel co = new CounterOfferModel(dto);
        assertEquals(1, co.getId());
    }
    @Test
    void testConstructorRs() throws SQLException {
        ResultSet rsMock = mock(ResultSet.class);
        when(rsMock.getInt("co_id")).thenReturn(1);
        when(rsMock.getInt("parent_bid")).thenReturn(2);
        when(rsMock.getDouble("amount")).thenReturn(123.50);
        when(rsMock.getString("status")).thenReturn("A");
        CounterOfferModel co = new CounterOfferModel(rsMock, false);
        assertEquals(1, co.getId());
    }
}

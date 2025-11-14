package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.CounterOfferModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.BidWithCounterofferModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private BidModel bid;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bid = new BidModel();
        bid.setId(1);
        bid.setAd(1);
        bid.setAmount(50000);
    }

    @Test
    public void TestInsertBidSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT insert_bid(?::VARCHAR, ?::INTEGER, ?::NUMERIC, ?::VARCHAR, ?::VARCHAR);"),eq(Integer.class),
                any(), any(),any(), any(), any()))
                .thenReturn(0);
        var response = bidService.insertBid(jdbcTemplate, "SESSIONID123", bid);
        assertEquals(0, response);
    }
    @Test
    public void TestCancelBidSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CANCEL_BID(?, ?)"),eq(Integer.class),
                any(), any()))
                .thenReturn(0);
        var response = bidService.cancelBid(jdbcTemplate, "SESSIONID123", bid.getId());
        assertEquals(0, response);
    }
    @Test
    public void TestAcceptOrRefuseBidSuccess() {
        bid.setStatus("A");
        when(jdbcTemplate.queryForObject(eq("SELECT accept_refuse_bid(?::VARCHAR, ?::INTEGER, ?::VARCHAR, ?::NUMERIC, ?::VARCHAR)"), eq(Integer.class),
                any(), any(), any(), any(), any()))
                .thenReturn(0);
        var response = bidService.acceptOrRefuseBid(jdbcTemplate, "SESSIONID123", bid);
        assertEquals(0, response);
    }
    @Test
    public void TestCancelCounterofferSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM CANCEL_Counteroffer(?, ?)"),eq(Integer.class),
                any(), any()))
                .thenReturn(0);
        var response = bidService.cancelCounteroffer(jdbcTemplate, "SESSIONID123", 23);
        assertEquals(0, response);
    }
    @Test
    public void TestAcceptOrRefuseCounterofferSuccess() {
        CounterOfferModel co = new CounterOfferModel();
        co.setId(1);
        co.setAmount(45000);
        co.setStatus("A");
        co.setParentBid(1);
        when(jdbcTemplate.queryForObject(eq("SELECT accept_refuse_counteroffer(?, ?, ?)"), eq(Integer.class),
                any(), any(), any()))
        .thenReturn(0);
        var response = bidService.acceptOrRefuseCounteroffer(jdbcTemplate, "SESSIONID123", co);
        assertEquals(0, response);
    }
    @Test
    public void TestGetBidsByAdSuccess() {
        var bidCo = new BidWithCounterofferModel();
        when(jdbcTemplate.query(
                eq("SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE ad = ?"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(List.of(bidCo));
        var response = bidService.getBids(jdbcTemplate, BidService.BidsKey.ad, "1");
        assertEquals(1, response.size());
    }
    @Test
    public void TestGetBidsByBidIdSuccess() {
        var bidCo = new BidWithCounterofferModel();
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE B.bid_id = ?"),
                any(RowMapper.class),
                eq(1)
        )).thenReturn(bidCo);
        var response = bidService.getBids(jdbcTemplate, BidService.BidsKey.bid_id, "1");
        assertEquals(1, response.size());
    }
    @Test
    public void TestGetBidsByOffererSuccess() {
        var bidCo = new BidWithCounterofferModel();
        when(jdbcTemplate.query(
                eq("SELECT * FROM BIDS_WITH_COUNTEROFFER WHERE offerer = ?"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(List.of(bidCo));
        var response = bidService.getBids(jdbcTemplate, BidService.BidsKey.offerer, "test@example.com");
        assertEquals(1, response.size());
    }


}

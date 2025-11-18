package com.api.dietiestates25.controller.BidTest;

import com.api.dietiestates25.controller.BidController;
import com.api.dietiestates25.model.extention.BidWithCounterofferModel;
import com.api.dietiestates25.service.BidService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GetBidsTest {

    private MockMvc mockMvc;
    @Mock
    private BidService bidService;
    @InjectMocks
    private BidController bidController;

    @Test
    void testGetBidsByAd_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        BidWithCounterofferModel bidElement = new BidWithCounterofferModel();
        List<BidWithCounterofferModel> bids = new ArrayList<BidWithCounterofferModel>();
        bids.add(bidElement);
        when(bidService.getBids(any(), eq(BidService.BidsKey.AD), any())).thenReturn(bids);
        mockMvc.perform(get("/getBids")
                        .param("key", BidService.BidsKey.AD.name())
                        .param("value", "123"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetBidsById_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        BidWithCounterofferModel bidElement = new BidWithCounterofferModel();
        List<BidWithCounterofferModel> bids = new ArrayList<BidWithCounterofferModel>();
        bids.add(bidElement);
        when(bidService.getBids(any(), eq(BidService.BidsKey.BID_ID), any())).thenReturn(bids);
        mockMvc.perform(get("/getBids")
                        .param("key", BidService.BidsKey.BID_ID.name())
                        .param("value", "123"))
                .andExpect(status().isOk());
    }
    @Test
    void testGetBidsByOfferer_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        BidWithCounterofferModel bidElement = new BidWithCounterofferModel();
        List<BidWithCounterofferModel> bids = new ArrayList<BidWithCounterofferModel>();
        bids.add(bidElement);
        when(bidService.getBids(any(), eq(BidService.BidsKey.OFFERER), any())).thenReturn(bids);
        mockMvc.perform(get("/getBids")
                        .param("key", BidService.BidsKey.OFFERER.name())
                        .param("value", "test"))
                .andExpect(status().isOk());
    }
}
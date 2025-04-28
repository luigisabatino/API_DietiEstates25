package com.api.dietiestates25.service.BidTest;

import com.api.dietiestates25.controller.BidController;
import com.api.dietiestates25.model.dto.ad.InsertAdDTO;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import com.api.dietiestates25.model.response.GeoapifyResponse;
import com.api.dietiestates25.model.response.OpenstreetResponse;
import com.api.dietiestates25.service.BidService;
import com.api.dietiestates25.service.CityService;
import com.api.dietiestates25.service.ExternalApiService;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InsertBidTest {

    private MockMvc mockMvc;
    @Mock
    private BidService bidService;
    @InjectMocks
    private BidController bidController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInsertBid_Success() throws Exception {
        InsertBidDTO dto = new InsertBidDTO();
        dto.setAmount(100000);
        dto.setAd(123);
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        when(bidService.insertBid(any(), any(),any())).thenReturn(0);
        mockMvc.perform(post("/insertBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
    @Test
    void testInsertBid_RequiredParameterExceptionAmount0() throws Exception {
        InsertBidDTO dto = new InsertBidDTO();
        dto.setAmount(0);
        dto.setAd(123);
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        when(bidService.insertBid(any(), any(),any())).thenThrow(new RequiredParameterException("amount"));
        mockMvc.perform(post("/insertBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testInsertBid_RequiredParameterExceptionAd0() throws Exception {
        InsertBidDTO dto = new InsertBidDTO();
        dto.setAmount(100000);
        dto.setAd(0);
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        when(bidService.insertBid(any(), any(),any())).thenThrow(new RequiredParameterException("ad"));
        mockMvc.perform(post("/insertBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }

}
package com.api.dietiestates25.controller.BidTest;

import com.api.dietiestates25.controller.BidController;
import com.api.dietiestates25.model.dto.bid.AcceptOrRefuseBidDTO;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import com.api.dietiestates25.service.BidService;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AcceptOrRefuseBidTest {

    private MockMvc mockMvc;
    @Mock
    private BidService bidService;
    @InjectMocks
    private BidController bidController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAcceptOrRefuseBid_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        AcceptOrRefuseBidDTO dto = new AcceptOrRefuseBidDTO();
        dto.setId(123);
        dto.setStatus("R");
        when(bidService.acceptOrRefuseBid(any(), any(),any())).thenReturn(0);
        mockMvc.perform(put("/acceptOrRefuseBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
    @Test
    void testAcceptOrRefuseBid_RequiredParameterExceptionId0() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        AcceptOrRefuseBidDTO dto = new AcceptOrRefuseBidDTO();
        dto.setId(0);
        dto.setStatus("R");
        when(bidService.acceptOrRefuseBid(any(), any(),any())).thenThrow(new RequiredParameterException("bid_id"));
        mockMvc.perform(put("/acceptOrRefuseBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testAcceptOrRefuseBid_RequiredParameterExceptionStatusNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        AcceptOrRefuseBidDTO dto = new AcceptOrRefuseBidDTO();
        dto.setStatus(null);
        when(bidService.acceptOrRefuseBid(any(), any(),any())).thenThrow(new RequiredParameterException("status"));
        mockMvc.perform(put("/acceptOrRefuseBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testAcceptOrRefuseBid_RequiredParameterExceptionStatusEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        AcceptOrRefuseBidDTO dto = new AcceptOrRefuseBidDTO();
        dto.setId(123);
        dto.setStatus("");
        when(bidService.acceptOrRefuseBid(any(), any(),any())).thenThrow(new RequiredParameterException("status"));
        mockMvc.perform(put("/acceptOrRefuseBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
}
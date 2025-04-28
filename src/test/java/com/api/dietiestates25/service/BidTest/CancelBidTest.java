package com.api.dietiestates25.service.BidTest;

import com.api.dietiestates25.controller.BidController;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import com.api.dietiestates25.service.BidService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CancelBidTest {

    private MockMvc mockMvc;
    @Mock
    private BidService bidService;
    @InjectMocks
    private BidController bidController;

    @Test
    void testCancelBid_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();
        when(bidService.cancelBid(any(), any(),anyInt())).thenReturn(0);
        mockMvc.perform(put("/cancelBid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("bidId","123")
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
}
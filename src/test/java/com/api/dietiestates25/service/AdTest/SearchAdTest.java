package com.api.dietiestates25.service.AdTest;

import com.api.dietiestates25.controller.AdController;
import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.service.AdService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SearchAdTest {

    private MockMvc mockMvc;
    @Mock
    private AdService adService;
    @InjectMocks
    private AdController adController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInsertAd_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        SearchAdRequest dto = new SearchAdRequest();
        dto.setType("S");
        AdWithGeoDataModel adElement = new AdWithGeoDataModel();
        List<AdWithGeoDataModel> ads = new ArrayList<AdWithGeoDataModel>();
        ads.add(adElement);
        when(adService.searchAd(any(), any())).thenReturn(ads);
        mockMvc.perform(put("/searchAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
    @Test
    void testInsertAd_RequiredParameterExceptionTypeNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        SearchAdRequest dto = new SearchAdRequest();
        dto.setType(null);
        AdModel adElement = new AdModel();
        List<AdModel> ads = new ArrayList<AdModel>();
        ads.add(adElement);
        when(adService.searchAd(any(), any())).thenThrow(new RequiredParameterException("ad type"));
        mockMvc.perform(put("/searchAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testInsertAd_RequiredParameterExceptionTypeEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        SearchAdRequest dto = new SearchAdRequest();
        dto.setType("");
        AdModel adElement = new AdModel();
        List<AdModel> ads = new ArrayList<AdModel>();
        ads.add(adElement);
        when(adService.searchAd(any(), any())).thenThrow(new RequiredParameterException("ad type"));
        mockMvc.perform(put("/searchAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
}
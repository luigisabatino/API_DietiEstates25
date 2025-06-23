package com.api.dietiestates25.controller.AdTest;

import com.api.dietiestates25.controller.AdController;
import com.api.dietiestates25.model.dto.ad.InsertAdDTO;
import com.api.dietiestates25.model.response.GeoapifyResponse;
import com.api.dietiestates25.model.response.OpenstreetResponse;
import com.api.dietiestates25.service.*;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InsertAdTest {

    private MockMvc mockMvc;
    @Mock
    private AdService adService;
    @Mock
    private CityService cityService;
    @Mock
    private ExternalApiService apiService;
    @InjectMocks
    private AdController adController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInsertAd_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        InsertAdDTO dto = new InsertAdDTO();
        dto.setAddress("via Test");
        dto.setCity("123456");
        dto.setDimentions(1000);
        dto.setDescription("test");
        dto.setPrice(123456);
        dto.setEnergyClass("A4");
        dto.setNRooms(3);
        dto.setNBathrooms(1);
        List<OpenstreetResponse> osRet = new ArrayList<>();
        osRet.add(new OpenstreetResponse());
        GeoapifyResponse geoRet = new GeoapifyResponse();
        geoRet.setFeatures(new ArrayList<>());
        var feature = new GeoapifyResponse.Feature();
        var property = new GeoapifyResponse.Property();
        var category = new ArrayList<String>();
        category.add("test");
        property.setCategories(category);
        feature.setProperties(property);
        geoRet.getFeatures().add(feature);
        when(apiService.coordinatesFromAddress(any())).thenReturn(osRet);
        when(apiService.placesInterestNearby(any())).thenReturn(geoRet);
        when(cityService.getCityNameByCode(any(),any())).thenReturn("cityName");
        when(adService.insertAd(any(), any(),any())).thenReturn(0);
        mockMvc.perform(post("/insertAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
    @Test
    void testInsertAd_RequiredParameterExceptionInCoordinatesFromAddress() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        InsertAdDTO dto = new InsertAdDTO();
        when(apiService.coordinatesFromAddress(any())).thenThrow(new RequiredParameterException("address"));
        mockMvc.perform(post("/insertAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testInsertAd_RequiredParameterExceptionInInsertAd() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        InsertAdDTO dto = new InsertAdDTO();
        dto.setAddress("via Test");
        dto.setCity(null);
        dto.setDimentions(1000);
        dto.setDescription("test");
        dto.setPrice(123456);
        dto.setEnergyClass("A4");
        dto.setNRooms(3);
        dto.setNBathrooms(1);
        List<OpenstreetResponse> osRet = new ArrayList<>();
        osRet.add(new OpenstreetResponse());
        GeoapifyResponse geoRet = new GeoapifyResponse();
        geoRet.setFeatures(new ArrayList<>());
        var feature = new GeoapifyResponse.Feature();
        var property = new GeoapifyResponse.Property();
        var category = new ArrayList<String>();
        category.add("test");
        property.setCategories(category);
        feature.setProperties(property);
        geoRet.getFeatures().add(feature);
        when(apiService.coordinatesFromAddress(any())).thenReturn(osRet);
        when(apiService.placesInterestNearby(any())).thenReturn(geoRet);
        when(cityService.getCityNameByCode(any(),any())).thenReturn("cityName");
        when(adService.insertAd(any(), any(),any())).thenThrow(new RequiredParameterException("city"));
        mockMvc.perform(post("/insertAd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isBadRequest());
    }
}
package com.api.dietiestates25.service.CityTest;

import com.api.dietiestates25.controller.CityController;
import com.api.dietiestates25.model.CityModel;
import com.api.dietiestates25.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GetCitiesTest {
    private MockMvc mockMvc;
    @Mock
    private CityService cityService;
    @InjectMocks
    private CityController cityController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetCities_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
        List<CityModel> citiesRet = new ArrayList<>();
        citiesRet.add(new CityModel());
        when(cityService.getItalianCitiesByProvince(any(), any()))
                .thenReturn(citiesRet);
        mockMvc.perform(get("/getCities")
                        .param("province","testProvince"))
                .andExpect(status().isOk());
    }
}
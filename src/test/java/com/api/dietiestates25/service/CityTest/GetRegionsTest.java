package com.api.dietiestates25.service.CityTest;

import com.api.dietiestates25.controller.CityController;
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
class GetRegionsTest {
    private MockMvc mockMvc;
    @Mock
    private CityService cityService;
    @InjectMocks
    private CityController cityController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetRegions_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
        List<String> regionsRet = new ArrayList<>();
        regionsRet.add("");
        when(cityService.getItalianRegions(any()))
                .thenReturn(regionsRet);
        mockMvc.perform(get("/getRegions"))
                .andExpect(status().isOk());
    }
}
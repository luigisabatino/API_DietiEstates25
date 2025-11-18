package com.api.dietiestates25.controller.CityTest;

import com.api.dietiestates25.controller.CityController;
import com.api.dietiestates25.service.CityService;
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
class GetProvincesTest {
    private MockMvc mockMvc;
    @Mock
    private CityService cityService;
    @InjectMocks
    private CityController cityController;

    @Test
    void testGetProvinces_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
        List<String> provincesRet = new ArrayList<>();
        provincesRet.add("");
        when(cityService.getItalianProvincesByRegion(any(), any()))
                .thenReturn(provincesRet);
        mockMvc.perform(get("/getProvinces")
                        .param("region","testRegion"))
                .andExpect(status().isOk());
    }
}
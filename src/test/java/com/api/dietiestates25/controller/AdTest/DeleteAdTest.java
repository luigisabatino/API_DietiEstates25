package com.api.dietiestates25.controller.AdTest;

import com.api.dietiestates25.controller.AdController;
import com.api.dietiestates25.service.AdService;
import com.api.dietiestates25.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.junit.jupiter.MockitoSettings;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeleteAdTest {

    private MockMvc mockMvc;
    @Mock
    private ImageService imageService;
    @Mock
    private AdService adService;
    @InjectMocks
    private AdController adController;

    @Test
    void testDeleteAd_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(adController).build();
        when(adService.deleteAd(any(),any(),anyInt())).thenReturn(0);
        when(imageService.deleteImagesByPrefix(any())).thenReturn(true);
        mockMvc.perform(delete("/deleteAd")
                        .param("id", "123")
                        .header("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
}
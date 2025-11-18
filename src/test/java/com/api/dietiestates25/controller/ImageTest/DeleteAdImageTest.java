package com.api.dietiestates25.controller.ImageTest;

import com.api.dietiestates25.controller.ImageController;
import com.api.dietiestates25.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DeleteAdImageTest {
    private MockMvc mockMvc;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private ImageController imageController;

    @Test
    void testDeleteAdImage_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        when(imageService.deleteImagesByPrefix(any()))
                .thenReturn(true);
        mockMvc.perform(delete("/delete")
                        .param("idAd","3"))
                .andExpect(status().isOk());
    }
    @Test
    void testDeleteAdImage_Fail() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        when(imageService.deleteImagesByPrefix(any()))
                .thenReturn(false);
        mockMvc.perform(delete("/delete")
                        .param("idAd","3"))
                .andExpect(status().isInternalServerError());
    }
}
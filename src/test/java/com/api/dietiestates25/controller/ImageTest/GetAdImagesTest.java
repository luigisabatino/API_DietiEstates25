package com.api.dietiestates25.controller.ImageTest;

import com.api.dietiestates25.controller.ImageController;
import com.api.dietiestates25.model.dto.ImageDTO;
import com.api.dietiestates25.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GetAdImagesTest {
    private MockMvc mockMvc;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private ImageController imageController;

    @Test
    void testGetAdImages_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        List<ImageDTO> images = new ArrayList<>();
        images.add(new ImageDTO("",""));
        when(imageService.getImagesByPrefix(any()))
                .thenReturn(images);
        mockMvc.perform(get("/getImagesByAd")
                        .param("idAd","3"))
                .andExpect(status().isOk());
    }
}
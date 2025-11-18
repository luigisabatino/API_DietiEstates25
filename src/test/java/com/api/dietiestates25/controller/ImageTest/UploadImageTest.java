package com.api.dietiestates25.controller.ImageTest;

import com.api.dietiestates25.controller.ImageController;
import com.api.dietiestates25.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UploadImageTest {
    private MockMvc mockMvc;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private ImageController imageController;

    @Test
    void testUploadImage_IllegalArgumentException() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        doThrow(new IllegalArgumentException("test")).when(imageService).uploadImage(any());
        mockMvc.perform(post("/uploadImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"test.jpg\",\"base64Image\":\"1234\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testUploadImage_Exception() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        doThrow(new RuntimeException("Upload error")).when(imageService).uploadImage(any());
        mockMvc.perform(post("/uploadImage")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fileName\":\"test.jpg\",\"base64Image\":\"1234\"}"))
                .andExpect(status().isInternalServerError());
    }
}
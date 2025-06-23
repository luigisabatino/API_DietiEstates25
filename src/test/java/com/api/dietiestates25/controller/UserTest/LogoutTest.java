package com.api.dietiestates25.controller.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LogoutTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogout_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(userService.logout(any(), any()))
                .thenReturn(true);
        mockMvc.perform(get("/logout")
                        .param("sessionId", "sessionIdTest"))
                .andExpect(status().isOk());
    }
}
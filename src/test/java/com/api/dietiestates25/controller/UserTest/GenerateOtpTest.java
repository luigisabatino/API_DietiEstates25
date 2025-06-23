package com.api.dietiestates25.controller.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Objects;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GenerateOtpTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGenerateOtp_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        var email = "test@mail.it";
        EmailService.OtpKey key = EmailService.OtpKey.ChangePwd;
        when(userService.insertOtp(any(), any())).thenReturn(true);
        when(emailService.sendOtpEmail(
                argThat(user -> user != null && Objects.equals(user.getEmail(), email)),
                eq(key))
        ).thenReturn(true);
        mockMvc.perform(get("/generateOtp")
                        .param("email", email)
                        .param("key",key.name()))
                .andExpect(status().isOk());
    }
}
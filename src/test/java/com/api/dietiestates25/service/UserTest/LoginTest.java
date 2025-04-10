package com.api.dietiestates25.service.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.UserService;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLogin_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        CodeResponse response = new CodeResponse();
        response.setCode(0);
        response.setMessage("Success");
        when(userService.login(any(), any())).thenReturn(response);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
    @Test
    void testLogin_InvalidCredentials() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail("test@mail.it");
        user.setPwd("wrongpwd");
        CodeResponse response = new CodeResponse();
        response.setCode(-2);
        when(userService.login(any(), any())).thenReturn(response);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void testLogin_RequiredParameterExceptionPwdNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail("test@mail.it");
        user.setPwd(null);
        when(userService.login(any(), any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testLogin_RequiredParameterExceptionPwdEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail("test@mail.it");
        user.setPwd("");
        when(userService.login(any(), any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testLogin_RequiredParameterExceptionEmailNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail(null);
        user.setPwd("testpwd");
        when(userService.login(any(), any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testLogin_RequiredParameterExceptionEmailEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        UserModel user = new UserModel();
        user.setEmail("");
        user.setPwd("testpassword");
        when(userService.login(any(), any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}

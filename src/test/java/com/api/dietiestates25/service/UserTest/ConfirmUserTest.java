package com.api.dietiestates25.service.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.model.dto.user.ConfirmUserDTO;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.UserService;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ConfirmUserTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testConfirmUser_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setTemporaryPwd("123456");
        when(userService.confirmUser(any(),any())).thenReturn(0);
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","false"))
                .andExpect(status().isOk());
    }
    @Test
    void testConfirmUser_RequiredParameterExceptionEmailNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail(null);
        user.setTemporaryPwd("123456");
        when(userService.confirmUser(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","false"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmUser_RequiredParameterExceptionEmailEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("");
        user.setTemporaryPwd("123456");
        when(userService.confirmUser(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","false"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmUser_RequiredParameterExceptionOtpNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setTemporaryPwd(null);
        when(userService.confirmUser(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","false"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmUser_RequiredParameterExceptionOtpEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setTemporaryPwd("");
        when(userService.confirmUser(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","false"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testPwd");
        user.setTemporaryPwd("testTP");
        when(userService.confirmManagerOrAgent(any(),any())).thenReturn(0);
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isOk());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionEmailNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail(null);
        user.setPwd("testPwd");
        user.setTemporaryPwd("testTP");
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionEmailEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("");
        user.setPwd("testPwd");
        user.setTemporaryPwd("testTP");
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionPwdNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd(null);
        user.setTemporaryPwd("testTP");
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionPwdEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("");
        user.setTemporaryPwd("testTP");
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionOtpNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setTemporaryPwd(null);
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testConfirmManagerOrAgent_RequiredParameterExceptionOtpEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setTemporaryPwd("");
        when(userService.confirmManagerOrAgent(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/confirmUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .param("isManagerOrAgent","true"))
                .andExpect(status().isBadRequest());
    }
}
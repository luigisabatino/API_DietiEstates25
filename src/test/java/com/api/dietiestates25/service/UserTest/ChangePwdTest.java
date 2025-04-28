package com.api.dietiestates25.service.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.model.dto.user.ConfirmUserDTO;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ChangePwdTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testChangePwd_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setTemporaryPwd("123456");
        when(userService.changePwd(any(),any())).thenReturn(1);
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionEmailNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail(null);
        user.setPwd("testpwd");
        user.setTemporaryPwd("123456");
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionEmailEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("");
        user.setPwd("testpwd");
        user.setTemporaryPwd("123456");
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionPwdNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd(null);
        user.setTemporaryPwd("123456");
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionPwdEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("");
        user.setTemporaryPwd("123456");
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionOtpNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setTemporaryPwd(null);
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("otp"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testChangePwd_RequiredParameterExceptionOtpEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        ConfirmUserDTO user = new ConfirmUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setTemporaryPwd("");
        when(userService.changePwd(any(),any())).thenThrow(new RequiredParameterException("otp"));
        mockMvc.perform(put("/changePwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
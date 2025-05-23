package com.api.dietiestates25.service.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.model.dto.user.CreateUserDTO;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateUser_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setFirstName("fname");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenReturn(0);
        when(emailService.sendConfirmAccountEmail(any(), eq('U'))).thenReturn(true);
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionEmailEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("");
        user.setPwd("testpwd");
        user.setFirstName("fname");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionEmailNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail(null);
        user.setPwd("testpwd");
        user.setFirstName("fname");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("email"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionPwdEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("");
        user.setFirstName("fname");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionPwdNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd(null);
        user.setFirstName("fname");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("pwd"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionFirstNameEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setFirstName("");
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("firstName"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionFirstNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setFirstName(null);
        user.setLastName("lname");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("firstName"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionLastNameEmpty() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setFirstName("fname");
        user.setLastName("");
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("lastName"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testCreateUser_RequiredParameterExceptionLastNameNull() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        CreateUserDTO user = new CreateUserDTO();
        user.setEmail("test@mail.it");
        user.setPwd("testpwd");
        user.setFirstName("fname");
        user.setLastName(null);
        when(userService.createUser(any(), any())).thenThrow(new RequiredParameterException("lastName"));
        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}
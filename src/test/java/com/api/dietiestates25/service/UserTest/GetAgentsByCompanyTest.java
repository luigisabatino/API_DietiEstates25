package com.api.dietiestates25.service.UserTest;

import com.api.dietiestates25.controller.UserController;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class GetAgentsByCompanyTest {
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAgentsByCompany_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        List<UserModel> usersRet = new ArrayList<>();
        usersRet.add(new UserModel());
        when(userService.getAgentsByCompany(any(), eq("testCompany")))
                .thenReturn(usersRet);
        mockMvc.perform(get("/getAgentsByCompany")
                        .param("company", "testCompany"))
                .andExpect(status().isOk());
    }
}
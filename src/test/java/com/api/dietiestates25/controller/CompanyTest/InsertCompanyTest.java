package com.api.dietiestates25.controller.CompanyTest;

import com.api.dietiestates25.controller.CompanyController;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.extention.InsertCompanyRequest;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.ExternalApiService;
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
class InsertCompanyTest {

    private MockMvc mockMvc;
    @Mock
    private CompanyService companyService;
    @Mock
    private EmailService emailService;
    @Mock
    private ExternalApiService apiService;
    @InjectMocks
    private CompanyController companyController;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testInsertCompany_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
        InsertCompanyRequest request = new InsertCompanyRequest();
        request.setCompanyName("testName");
        request.setVatNumber("IT1234567890");
        UserModel manager = new UserModel();
        manager.setEmail("test@mail.it");
        manager.setFirstName("fname");
        manager.setLastName("lname");
        request.setManager(manager);
        CodeResponse response = new CodeResponse();
        response.setCode(0);
        response.setMessage("Success");
        when(apiService.verifyVatNumber(any())).thenReturn(0);
        when(companyService.insertCompany(any(), any())).thenReturn(response);
        when(emailService.sendConfirmAccountEmail(any(), eq(Character.valueOf('M')))).thenReturn(true);
        mockMvc.perform(post("/insertCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
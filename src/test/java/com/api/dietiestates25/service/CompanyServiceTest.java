package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.CompanyModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.extention.InsertCompanyRequest;
import org.apache.catalina.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestInsertCompanySuccess() {
        InsertCompanyRequest request = new InsertCompanyRequest();
        request.setCompanyName("Test Srl");
        request.setVatNumber("IT1234567890");
        request.setManager(new UserModel());
        var manager = new UserModel();
        manager.setEmail("test@mail.it");
        manager.setPwd("testpwd");
        manager.setFirstName("Mario");
        manager.setLastName("Rossi");
        manager.setCompany("IT1234567890");
        request.setManager(manager);

        Map<String, Object> mockResult = Map.of(
                "response", 0,
                "message", "Company created successfully"
        );
        when(jdbcTemplate.queryForMap(eq("SELECT * FROM CREATE_COMPANY(?,?,?,?,?,?)"),
                any(),any(),any(),any(),any(),any()))
                .thenReturn(mockResult);
        var response = companyService.insertCompany(jdbcTemplate, request);
        assertEquals(response.getCode(), 0);
        assertEquals(response.getMessage(), "Company created successfully");
    }

}

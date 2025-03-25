package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.model.request.InsertCompanyRequest;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ExternalApiService apiService;

    public CompanyController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/insertCompany")
    public ResponseEntity<String> insertCompany(@RequestBody InsertCompanyRequest request)
    {
        var response = new CodeResponse();
        try {
            response.setCode(apiService.verifyVatNumber(request.getVatNumber()));
            if(response.getCode() != 0)
                return response.toHttpMessageResponse();
            var companyService = new CompanyService();
            response = companyService.insertCompany(jdbcTemplate, request);
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(request.getManager(), 'M');
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
}

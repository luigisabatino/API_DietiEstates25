package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.model.extention.InsertCompanyRequest;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.ExternalApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    private final JdbcTemplate jdbcTemplate;
    private final EmailService emailService;
    private final ExternalApiService apiService;
    private final CompanyService companyService;
    public CompanyController(JdbcTemplate jdbcTemplate, EmailService emailService, ExternalApiService apiService, CompanyService companyService)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
        this.apiService = apiService;
        this.companyService = companyService;
    }

    @PostMapping("/insertCompany")
    public ResponseEntity<String> insertCompany(@RequestBody InsertCompanyRequest request)
    {
        var response = new CodeResponse();
        try {
            response.setCode(apiService.verifyVatNumber(request.getVatNumber()));
            if(response.getCode() != 0)
                return response.toHttpMessageResponse();
            response = companyService.insertCompany(jdbcTemplate, request);
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(request.getManager(), 'M');
            return response.toHttpMessageResponse();
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return response.toHttpMessageResponse(ie);
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
}

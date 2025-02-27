package com.api.dietiestates25.controller;

import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.model.request.InsertCompanyRequest;
import com.api.dietiestates25.service.ApiLayerService;
import com.api.dietiestates25.service.EmailService;
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
    private ApiLayerService apiLayerService;

    @Autowired
    private EmailService emailService;


    public CompanyController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/insertCompany")
    public ResponseEntity<String> insertCompany(@RequestBody InsertCompanyRequest request)
    {
        try {
            if(!apiLayerService.verifyVatNumber(request.getVatNumber()))
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: VAT Number not valid");
            request.manager.setOtp();
            var companyService = new CompanyService();
            var response = companyService.insertCompany(jdbcTemplate, request);
            if(response.getCode() != 0 )
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getMessage());
            emailService.sendEmail(request.manager.getEmail(), "Confirm Account", "Hi,\nInsert this otp code in DietiEstates for activate your account and insert your company:\n\n" + request.manager.getOtp() + "\n\nThanks.");
            return ResponseEntity.ok(response.getMessage());
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
    }

}

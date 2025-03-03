package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.model.request.InsertCompanyRequest;
import com.api.dietiestates25.service.ApiLayerService;
import com.api.dietiestates25.service.EmailService;
import org.aspectj.apache.bcel.classfile.Code;
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
    public ResponseEntity<CodeResponse> insertCompany(@RequestBody InsertCompanyRequest request)
    {
        var response = new CodeResponse();
        try {
            if(!apiLayerService.verifyVatNumber(request.getVatNumber())) {
                response.setCode(-100);
                response.setMessage("Error: VAT Number not valid");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            var companyService = new CompanyService();
            response = companyService.insertCompany(jdbcTemplate, request);
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            response.setCode(99);
            response.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

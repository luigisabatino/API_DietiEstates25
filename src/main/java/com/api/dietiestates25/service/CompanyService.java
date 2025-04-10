package com.api.dietiestates25.service;

import com.api.dietiestates25.model.CompanyModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.extention.InsertCompanyRequest;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CompanyService {
    public CodeResponse insertCompany(JdbcTemplate jdbcTemplate, InsertCompanyRequest request) {
        requiredValuesForCompanyOperations(request);
        var manager = request.getManager();
        manager.setCompany(request.getCompanyName());
        manager.setPwd();
        manager.setOtp(manager.getPwd());
        manager.encodePwd();
        UserService.requiredValuesForUserOperations(manager, UserService.Operation.CreateAgent);
        String query = "SELECT * FROM CREATE_COMPANY(?,?,?,?,?,?)";
        Map<String, Object> result = jdbcTemplate.queryForMap(query,
                request.getVatNumber(), request.getCompanyName(), manager.getEmail(),  manager.getFirstName(), manager.getLastName(), manager.getPwd()
        );
        CodeResponse response = new CodeResponse();
        response.setCode((Integer) result.get("response"));
        response.setMessage((String) result.get("message"));
        return response;
    }

    private static void requiredValuesForCompanyOperations(CompanyModel company) {
        if(company.getVatNumber()==null || company.getVatNumber().isBlank())
            throw new RequiredParameterException("vatNumber");
        if(company.getCompanyName()==null || company.getCompanyName().isBlank())
            throw new RequiredParameterException("CompanyName");
    }
}

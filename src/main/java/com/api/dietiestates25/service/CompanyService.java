package com.api.dietiestates25.service;

import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.request.InsertCompanyRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyService {
    public CodeResponse insertCompany(JdbcTemplate jdbcTemplate, InsertCompanyRequest request) {
        var manager = request.getManager();
        manager.setPwd();
        var response = new CodeResponse();
        String query = "SELECT * FROM CREATE_COMPANY(?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(query, new RowMapper<CodeResponse>() {
            @Override
            public CodeResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                response.setCode(rs.getInt("response"));
                response.setMessage(rs.getString("message"));
                return response;
            }
        }, request.getVatNumber(), request.getCompanyName(), manager.getEmail(), manager.getFirstName(), manager.getLastName(), manager.getPwd());
    }

}

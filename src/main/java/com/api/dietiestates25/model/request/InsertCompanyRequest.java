package com.api.dietiestates25.model.request;

import com.api.dietiestates25.model.CompanyModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.SessionResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertCompanyRequest extends CompanyModel {
    public UserModel manager;

    public CodeResponse insertCompany(JdbcTemplate jdbcTemplate) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        manager.setPwd(encoder.encode(manager.getPwd()));
        var response = new CodeResponse();
        String query = "SELECT * FROM CREATE_COMPANY(?,?,?,?,?,?,?)";
        return jdbcTemplate.queryForObject(query, new RowMapper<CodeResponse>() {
            @Override
            public CodeResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                response.setCode(rs.getInt("response"));
                response.setMessage(rs.getString("message"));
                return response;
            }
        }, getVatNumber(), getCompanyName(), manager.getEmail(), manager.getFirstName(), manager.getLastName(), manager.getOtp(), manager.getPwd());
    }

}

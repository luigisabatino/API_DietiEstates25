package com.api.dietiestates25.model;

import com.api.dietiestates25.model.response.SessionResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
@Getter
@Setter
public class CompanyModel {
    private String vatNumber;
    private String companyName;

    public CompanyModel(){}
}

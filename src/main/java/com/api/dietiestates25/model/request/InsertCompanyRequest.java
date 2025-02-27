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
}

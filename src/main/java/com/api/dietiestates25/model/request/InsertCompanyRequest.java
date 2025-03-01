package com.api.dietiestates25.model.request;

import com.api.dietiestates25.model.CompanyModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.SessionResponse;
import lombok.Setter;
import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
@Setter
public class InsertCompanyRequest extends CompanyModel {
    private UserModel manager;
}

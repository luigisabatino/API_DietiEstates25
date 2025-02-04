package com.api.dietiestates25.model;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    private String email;
    private String pwd;
    private String firstName;
    private String lastName;
    private String company;
    private String otp;

    public UserModel() { }

    public String getEmail() { return email; }
    public void setEmail(String _email) { email = _email; }

    public String getPwd() { return pwd; }
    public void setPwd(String _pwd) { pwd = _pwd; }

    public String getFirstName(){return firstName;}
    public void setFirstName(String _firstname){firstName = _firstname;}

    public String getLastName(){return lastName;}
    public void setLastName(String _lastName){lastName=_lastName;}

    public String getCompany(){return company;}
    public void setCompany(String _company){company=_company;}

    public String getOtp(){return otp;}
    public void setOtp(String _otp){otp=_otp;}

    public LoginResponseModel login(JdbcTemplate jdbcTemplate) {
        String query = "SELECT PWD FROM USERS WHERE email = ?";
        String pwdInDB = "";
        try {
            pwdInDB = jdbcTemplate.queryForObject(query, String.class, email);
        } catch (EmptyResultDataAccessException e) {
            var response = new LoginResponseModel();
            response.setMessage("Error: not valid credentials");
            return response;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(this.pwd,pwdInDB))
        {
            var response = new LoginResponseModel();
            response.setMessage("Error: not valid credentials");
            return response;
        }
        query = "SELECT * FROM LOGIN(?, ?)";
        return jdbcTemplate.queryForObject(query, new RowMapper<LoginResponseModel>() {
            @Override
            public LoginResponseModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                LoginResponseModel response = new LoginResponseModel();
                response.setSessionid(rs.getString("session_id")); // Nome colonna
                response.setMessage(rs.getString("message")); // Nome colonna
                return response;
            }
        }, email, pwdInDB);
    }

    public Integer create_user(JdbcTemplate jdbcTemplate) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setPwd(encoder.encode(pwd));
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                email, pwd,"U",firstName,lastName,null,"123456"));
    }

    public static class LoginResponseModel {
        private String sessionid;
        private String message;

        public LoginResponseModel() { }

        public String getSessionid() { return sessionid; }
        public void setSessionid(String _sessionid) { sessionid = _sessionid; }
        public String getMessage() { return message; }
        public void setMessage(String _message) { message = _message; }
    }
}

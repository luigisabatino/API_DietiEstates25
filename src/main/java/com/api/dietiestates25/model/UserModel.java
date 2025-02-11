package com.api.dietiestates25.model;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
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
        var response = new LoginResponseModel();
        String pwdInDB = checkPwd(jdbcTemplate);
        if(pwdInDB == null) {
            response.setMessage("Error: not valid credentials");
            return response;
        }
        String query = "SELECT * FROM LOGIN(?, ?)";
        return jdbcTemplate.queryForObject(query, new RowMapper<LoginResponseModel>() {
            @Override
            public LoginResponseModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                response.setSessionid(rs.getString("session_id"));
                response.setMessage(rs.getString("message"));
                return response;
            }
        }, email, pwdInDB);
    }

    private String checkPwd(JdbcTemplate jdbcTemplate) {
        String pwdInDB = "";
        try {
            String query = "SELECT PWD FROM USERS WHERE email = ?";
            pwdInDB = jdbcTemplate.queryForObject(query, String.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(this.pwd,pwdInDB))
        {
            return null;
        }
        return pwdInDB;
    }

    public int createUser(JdbcTemplate jdbcTemplate) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setPwd(encoder.encode(pwd));
        int otp = generateOTP();
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?,?,?)";
        int returnCode = (jdbcTemplate.queryForObject(query, Integer.class,
                email, pwd,((company==null) ? "U":"A"),firstName,lastName,company,String.valueOf(otp)));
        if(returnCode == 0)
            return otp;
        return returnCode;
    }

    private int generateOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return otp;
    }

    public Integer confirmUser(JdbcTemplate jdbcTemplate) {
        String query = "SELECT * FROM CONFIRM_USER(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class, email, otp));
    }

}

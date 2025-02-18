package com.api.dietiestates25.model;

import com.api.dietiestates25.model.response.SessionResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;

@Getter
@Setter
public class UserModel {
    private String email;
    private String pwd;
    private String firstName;
    private String lastName;
    private String company;
    private String otp;

    public UserModel() { }

    public void setOtp(){
        SecureRandom random = new SecureRandom();
        int _otp = 100000 + random.nextInt(900000);
        otp = String.valueOf(_otp);
    }

    public SessionResponse login(JdbcTemplate jdbcTemplate) {
        SessionResponse response = new SessionResponse();

        String pwdInDB = checkPwd(jdbcTemplate);
        if (pwdInDB == null) {
            response.setMessage("Invalid credentials.");
            return response;
        }

        String query = "SELECT * FROM LOGIN(?, ?)";
        try {
            return jdbcTemplate.queryForObject(query, (rs, _) -> {
                String sessionId = rs.getString("session_id");
                String message = rs.getString("message");
                // Debugging, to-do: logging
                System.out.println("Session ID: " + sessionId);
                System.out.println("Message: " + message);

                response.setSessionid(sessionId);
                response.setMessage(message);
                return response;
            }, email, pwdInDB);

        } catch (EmptyResultDataAccessException e) {
            response.setMessage("Error: Unable to create session");
            return response;
        } catch (DataAccessException e) {
            System.out.println("Database error details: " + e.getMessage());
            response.setMessage("Error: Database error occurred");
            return response;
        }
    }

    private String checkPwd(JdbcTemplate jdbcTemplate) {
        String pwdInDB;
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
        pwd = encoder.encode(pwd);
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                email, pwd,((company==null) ? "U":"A"),firstName,lastName,company,String.valueOf(otp)));
    }

    public Integer confirmUser(JdbcTemplate jdbcTemplate) {
        String query = "SELECT * FROM CONFIRM_USER(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class, email, otp));
    }

}

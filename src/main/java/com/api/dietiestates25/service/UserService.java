package com.api.dietiestates25.service;

import com.api.dietiestates25.model.response.SessionResponse;
import com.api.dietiestates25.model.UserModel;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    public UserService() {}

    public SessionResponse login(JdbcTemplate jdbcTemplate, UserModel user) {
        SessionResponse response = new SessionResponse();

        String pwdInDB = checkPwd(jdbcTemplate, user);
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
            }, user.getEmail(), pwdInDB);

        } catch (EmptyResultDataAccessException e) {
            response.setMessage("Error: Unable to create session");
            return response;
        } catch (DataAccessException e) {
            System.out.println("Database error details: " + e.getMessage());
            response.setMessage("Error: Database error occurred");
            return response;
        }
    }

    private String checkPwd(JdbcTemplate jdbcTemplate, UserModel user) {
        String pwdInDB;
        try {
            String query = "SELECT PWD FROM USERS WHERE email = ?";
            pwdInDB = jdbcTemplate.queryForObject(query, String.class, user);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(user.getPwd(),pwdInDB))
            return null;
        return pwdInDB;
    }

    public int createUser(JdbcTemplate jdbcTemplate, UserModel user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPwd(encoder.encode(user.getPwd()));
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getPwd(),((user.getCompany()==null) ? "U":"A"),user.getFirstName(),user.getLastName(),user.getCompany(),String.valueOf(user.getOtp())));
    }

    public Integer confirmUser(JdbcTemplate jdbcTemplate, UserModel user) {
        String query = "SELECT * FROM CONFIRM_USER(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class, user.getEmail(), user.getOtp()));
    }
}

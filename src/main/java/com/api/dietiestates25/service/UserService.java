package com.api.dietiestates25.service;

import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.SessionResponse;
import com.api.dietiestates25.model.UserModel;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                response.setSessionid(rs.getString("session_id"));
                response.setMessage(rs.getString("message"));
                return response;
            }, user.getEmail(), pwdInDB);

        } catch (EmptyResultDataAccessException e) {
            response.setMessage("Error: Unable to create session");
        } catch (DataAccessException e) {
            response.setMessage("Error: Database error occurred");
        }
        finally {
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
        user.setOtp();
        user.encodePwd();
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getPwd(),user.getFirstName(),user.getLastName(),String.valueOf(user.getOtp())));
    }

    public int confirmUser(JdbcTemplate jdbcTemplate, UserModel user) {
        String query = "SELECT * FROM CONFIRM_USER(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getOtp()));
    }

    public int confirmManagerOrAgent(JdbcTemplate jdbcTemplate, UserModel user) {
        user.encodePwd();
        String query = "SELECT * FROM CONFIRM_MANAGERORAGENT(?, ?, ?)";
        var response = new CodeResponse();
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getPwd(), user.getOtp()));
    }

    public int createAgent(JdbcTemplate jdbcTemplate, UserModel user, String sessionId) {
        user.encodePwd();
        String query = "SELECT CREATE_TEMP_AGENT(?,?,?,?,?)";
        var response = new CodeResponse();
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId,user.getEmail(), user.getPwd(),user.getFirstName(),user.getLastName()));
    }

}

package com.api.dietiestates25.service;

import com.api.dietiestates25.throwable.*;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.UserModel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    public UserService() {}
    public CodeResponse login(JdbcTemplate jdbcTemplate, UserModel user) {
        requiredValuesForUserOperations(user, Operation.Login);
        CodeResponse response = new CodeResponse();
        String pwdInDB = checkPwd(jdbcTemplate, user);
        if (pwdInDB == null) {
            response.setMessage("Invalid credentials.");
            response.setCode(-2);
            return response;
        }
        String query = "SELECT * FROM LOGIN(?, ?)";
        return jdbcTemplate.queryForObject(query, (rs, _) -> {
                response.setMessage(rs.getString("session_id"));
                response.setCode(rs.getInt("code"));
                return response;
                }, user.getEmail(), pwdInDB);
    }
    private String checkPwd(JdbcTemplate jdbcTemplate, UserModel user) {
        String pwdInDB;
        try {
            String query = "SELECT PWD FROM USERS WHERE email = ?";
            pwdInDB = jdbcTemplate.queryForObject(query, String.class, user.getEmail());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(user.getPwd(),pwdInDB))
            return null;
        return pwdInDB;
    }
    public int createUser(JdbcTemplate jdbcTemplate, UserModel user) {
        requiredValuesForUserOperations(user, Operation.CreateUser);
        user.setOtp();
        user.encodePwd();
        String query = "SELECT CREATE_TEMP_USER(?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getPwd(),user.getFirstName(),user.getLastName(),String.valueOf(user.getOtp())));
    }
    public int confirmUser(JdbcTemplate jdbcTemplate, UserModel user) {
        requiredValuesForUserOperations(user, Operation.ConfirmUser);
        String query = "SELECT * FROM CONFIRM_USER(?, ?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getOtp()));
    }
    public int confirmManagerOrAgent(JdbcTemplate jdbcTemplate, UserModel user) {
        requiredValuesForUserOperations(user, Operation.ConfirmManagerOrAgent);
        user.encodePwd();
        String query = "SELECT * FROM CONFIRM_MANAGERORAGENT(?, ?, ?)";
        var response = new CodeResponse();
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), user.getPwd(), user.getOtp()));
    }
    public int createAgent(JdbcTemplate jdbcTemplate, UserModel user, String sessionId) {
        requiredValuesForUserOperations(user, Operation.CreateManager);
        user.encodePwd();
        String query = "SELECT CREATE_TEMP_AGENT(?,?,?,?,?)";
        var response = new CodeResponse();
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId,user.getEmail(), user.getPwd(),user.getFirstName(),user.getLastName()));
    }
    public CodeEntitiesResponse<UserModel> getAgentsByCompany(JdbcTemplate jdbcTemplate, String company) {
        var response = new CodeEntitiesResponse<UserModel>();
        String query = "SELECT * FROM USERS WHERE COMPANY = ?";
        response.setEntities(jdbcTemplate.query(query, new Object[]{company}, (rs, rowNum) -> {
            return new UserModel(rs);
        }));
        return response;
    }
    public UserModel getUserByEmail(JdbcTemplate jdbcTemplate, String email) {
        String query = "SELECT * FROM USERS WHERE EMAIL = ?";
        return jdbcTemplate.queryForObject(query, (rs, _) -> {
            return new UserModel(rs);
        }, email);
    }
    public static void requiredValuesForUserOperations(UserModel user, Operation operation) {
        if(operation != Operation.AgentsByCompany && (user.getEmail() == null || user.getEmail().isBlank()))
            throw new RequiredParameterException("email");
        if(operation != Operation.ConfirmUser && (user.getPwd() == null || user.getPwd().isBlank()))
            throw new RequiredParameterException("pwd");
        if((operation == Operation.ConfirmUser || operation == Operation.ConfirmManagerOrAgent) && (user.getOtp() == null || user.getOtp().isBlank()))
            throw new RequiredParameterException("otp");
        if((operation == Operation.CreateUser || operation == Operation.CreateAgent) && (user.getFirstName() == null || user.getFirstName().isBlank()))
            throw new RequiredParameterException("firstName");
        if((operation == Operation.CreateUser || operation == Operation.CreateAgent) && (user.getLastName() == null || user.getLastName().isBlank()))
            throw new RequiredParameterException("lastName");
        if((operation == Operation.CreateAgent || operation == Operation.AgentsByCompany) && (user.getCompany() == null || user.getCompany().isBlank()))
            throw new RequiredParameterException("company");
    }
    public enum Operation {
        CreateUser,
        CreateAgent,
        CreateManager,
        Login,
        ConfirmManagerOrAgent,
        ConfirmUser,
        AgentsByCompany;
    }
}

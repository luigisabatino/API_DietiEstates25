package com.api.dietiestates25.service;

import com.api.dietiestates25.throwable.*;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.UserModel;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {
    public UserService() {}
    public  CodeResponse login(JdbcTemplate jdbcTemplate, UserModel user) {
        return login(jdbcTemplate, user,false);
    }
    private CodeResponse login(JdbcTemplate jdbcTemplate, UserModel user, boolean is3part) {
        requiredValuesForUserOperations(user, Operation.Login);
        CodeResponse response = new CodeResponse();
        String pwdInDB = checkPwd(jdbcTemplate, user, is3part);
        String query = "SELECT * FROM LOGIN(?, ?)";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> {
                response.setMessage(rs.getString("session_id"));
                response.setCode(rs.getInt("code"));
                return response;
                }, user.getEmail(), pwdInDB);
    }
    private String checkPwd(JdbcTemplate jdbcTemplate, UserModel user, boolean is3part) {
        String pwdInDB;
        try {
            String query = "SELECT PWD FROM USERS WHERE email = ?";
            pwdInDB = jdbcTemplate.queryForObject(query, String.class, user.getEmail());
        } catch (EmptyResultDataAccessException e) {
            throw new NoMatchCredentialsException();
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if((!encoder.matches(user.getPwd(),pwdInDB))&&(!is3part))
            throw new NoMatchCredentialsException();
        return pwdInDB;
    }
    private String checkPwd(JdbcTemplate jdbcTemplate, UserModel user) {
        return checkPwd(jdbcTemplate, user, false);
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
        user.encodeOtp();
        var pwdInDB = checkPwd(jdbcTemplate, user);
        String query = "SELECT * FROM CONFIRM_MANAGERORAGENT(?, ?, ?)";
        var response = new CodeResponse();
        return (jdbcTemplate.queryForObject(query, Integer.class,
                user.getEmail(), pwdInDB, user.getOtp()));
    }
    public int createAgent(JdbcTemplate jdbcTemplate, UserModel user, String sessionId) {
        requiredValuesForUserOperations(user, Operation.CreateAgent);
        user.setPwd();
        user.setOtp(user.getPwd());
        user.encodePwd();
        String query = "SELECT * FROM CREATE_TEMP_AGENT(?,?,?,?,?)";
        return (jdbcTemplate.queryForObject(query, Integer.class,
                sessionId,user.getEmail(), user.getPwd(),user.getFirstName(),user.getLastName()));
    }
    public List<UserModel> getAgentsByCompany(JdbcTemplate jdbcTemplate, String company) {
        var response = new ArrayList<UserModel>();
        String query = "USER_COMPANY WHERE U.COMPANY = ? AND CONFIRMED = TRUE";
        return (jdbcTemplate.query(query, new Object[]{company}, (rs, rowNum) -> {
            return new UserModel(rs);
        }));
    }
    public UserModel getUserByEmail(JdbcTemplate jdbcTemplate, String email) {
        String query = "SELECT * FROM USER_COMPANY WHERE EMAIL = ?";
        return jdbcTemplate.queryForObject(query, (rs, ignored) -> {
            return new UserModel(rs);
        }, email);
    }
    public boolean logout(JdbcTemplate jdbcTemplate, String sessionId) {
        var query = "DELETE FROM SESSIONS WHERE SESSIONID = ?";
        return (jdbcTemplate.update(query, sessionId) > 0);
    }
    public int changePwd(JdbcTemplate jdbcTemplate, UserModel user) {
        requiredValuesForUserOperations(user, Operation.ChangePwd);
        user.encodePwd();
        var query = "UPDATE USERS SET PWD = ? WHERE EMAIL = ? AND OTP = ?";
        return jdbcTemplate.update(query, user.getPwd(), user.getEmail(), user.getOtp());
    }
    public boolean insertOtp(JdbcTemplate jdbcTemplate, UserModel user) {
        user.setOtp();
        var query = "UPDATE USERS SET OTP = ? WHERE EMAIL = ?";
        return (jdbcTemplate.update(query, user.getOtp(), user.getEmail()) > 0);
    }
    public CodeResponse load3partUser(JdbcTemplate jdbcTemplate, UserModel user)  {
        user.setPwd();
        user.encodePwd();
        try {
            getUserByEmail(jdbcTemplate, user.getEmail());
        }
        catch (EmptyResultDataAccessException ex) {
            user.setOtp();
            if((createUser(jdbcTemplate, user) != 0) || (confirmUser(jdbcTemplate, user) != 0))
                throw new InternalException("Internal Error");
        }
        return login(jdbcTemplate, user, true);
    }
    public static void requiredValuesForUserOperations(UserModel user, Operation operation) {
        if((operation != Operation.AgentsByCompany) && (user.getEmail() == null || user.getEmail().isBlank()))
            throw new RequiredParameterException("email");
        if((operation == Operation.ConfirmManagerOrAgent || operation == Operation.ChangePwd) && (user.getPwd() == null || user.getPwd().isBlank()))
            throw new RequiredParameterException("pwd");
        if((operation == Operation.ConfirmUser || operation == Operation.ChangePwd) && (user.getOtp() == null || user.getOtp().isBlank()))
            throw new RequiredParameterException("otp");
        if((operation == Operation.CreateUser || operation == Operation.CreateAgent) && (user.getFirstName() == null || user.getFirstName().isBlank()))
            throw new RequiredParameterException("firstName");
        if((operation == Operation.CreateUser || operation == Operation.CreateAgent) && (user.getLastName() == null || user.getLastName().isBlank()))
            throw new RequiredParameterException("lastName");
        if((operation == Operation.AgentsByCompany) && (user.getCompany() == null || user.getCompany().isBlank()))
            throw new RequiredParameterException("company");
    }
    public enum Operation {
        CreateUser,
        CreateAgent,
        Login,
        ConfirmManagerOrAgent,
        ConfirmUser,
        AgentsByCompany,
        ChangePwd;
    }
}

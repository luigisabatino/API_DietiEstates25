package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<CodeResponse> login(@RequestBody UserModel user) {
        var loginResponse = new CodeResponse();
        try {
            var userService = new UserService();
            loginResponse = userService.login(jdbcTemplate, user);
            return loginResponse.toHttpResponse();
        } catch (Exception ex) {
            return loginResponse.toHttpResponse(ex);
        }
    }


    @PostMapping("/createUser")
    public ResponseEntity<CodeResponse> createUser(@RequestBody UserModel user) {
        var response = new CodeResponse();
        try {
            var userService = new UserService();
            response.setCode(userService.createUser(jdbcTemplate, user));
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(user, 'U');
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @GetMapping("/confirmUser")
    public ResponseEntity<CodeResponse> confirmUser(boolean isManagerOrAgent, @RequestBody UserModel user) {
        CodeResponse response = new CodeResponse();
        try {
            var userService = new UserService();
            if(isManagerOrAgent)
                response.setCode(userService.confirmUser(jdbcTemplate, user));
            else
                response.setCode(userService.confirmManagerOrAgent(jdbcTemplate, user));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @PostMapping("/createAgent")
    public ResponseEntity<CodeResponse> createAgent(@RequestBody UserModel user, @RequestHeader String sessionId) {
        CodeResponse response = new CodeResponse();
        try {
            user.setPwd();
            var userService = new UserService();
            response.setCode(userService.createAgent(jdbcTemplate, user, sessionId));
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(user, 'U');
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @GetMapping("/getAgentsByCompany")
    public ResponseEntity<CodeEntitiesResponse<UserModel>> getAgentsByCompany(@RequestParam String company)
    {
        try {
            var userService = new UserService();
            return ResponseEntity.ok(userService.getAgentsByCompany(jdbcTemplate, company));
        }
        catch(Exception ex)
        {
            return null;
        }
    }

}

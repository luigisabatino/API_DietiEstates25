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
    public ResponseEntity<CodeEntitiesResponse<UserModel>> login(@RequestBody UserModel user) {
        var loginResponse = new CodeEntitiesResponse<UserModel>();
        try {
            var userService = new UserService();
            loginResponse = (CodeEntitiesResponse<UserModel>) userService.login(jdbcTemplate, user);
            if(loginResponse.getCode() > 0) {
                loginResponse.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            }
            return ResponseEntity.ok(loginResponse);//loginResponse.toHttpResponse();
        } catch (Exception ex) {
            return null; //return loginResponse.toHttpResponse(ex);
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
    @PostMapping("/confirmUser")
    public ResponseEntity<CodeEntitiesResponse<UserModel>> confirmUser(boolean isManagerOrAgent, @RequestBody UserModel user) {
        CodeEntitiesResponse<UserModel> response = new CodeEntitiesResponse<UserModel>();
        try {
            var userService = new UserService();
            if(!isManagerOrAgent)
                response.setCode(userService.confirmUser(jdbcTemplate, user));
            else
                response.setCode(userService.confirmManagerOrAgent(jdbcTemplate, user));
            if(response.getCode()>0)
                response.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            return ResponseEntity.ok(response);
            //return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return null; //response.toHttpResponse(ex);
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

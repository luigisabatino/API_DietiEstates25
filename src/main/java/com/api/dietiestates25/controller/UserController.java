package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.response.DetailEntityResponse;
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
    public ResponseEntity<DetailEntityResponse<UserModel>> login(@RequestBody UserModel user) {
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            var userService = new UserService();
            response = (CodeEntitiesResponse<UserModel>) userService.login(jdbcTemplate, user);
            if(response.getCode() > 0) {
                response.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            }
            return response.toHttpEntitiesResponse();
        } catch (Exception ex) {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        var response = new CodeResponse();
        try {
            var userService = new UserService();
            response.setCode(userService.createUser(jdbcTemplate, user));
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(user, 'U');
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PostMapping("/confirmUser")
    public ResponseEntity<DetailEntityResponse<UserModel>> confirmUser(boolean isManagerOrAgent, @RequestBody UserModel user) {
        CodeEntitiesResponse<UserModel> response = new CodeEntitiesResponse<UserModel>();
        try {
            var userService = new UserService();
            if(isManagerOrAgent)
                response.setCode(userService.confirmManagerOrAgent(jdbcTemplate, user));
            else
                response.setCode(userService.confirmUser(jdbcTemplate, user));
            if(response.getCode()>0)
                response.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }

    @PostMapping("/createAgent")
    public ResponseEntity<String> createAgent(@RequestBody UserModel user, @RequestHeader String sessionId) {
        CodeResponse response = new CodeResponse();
        try {
            user.setPwd();
            var userService = new UserService();
            response.setCode(userService.createAgent(jdbcTemplate, user, sessionId));
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(user, 'U');
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }

    @GetMapping("/getAgentsByCompany")
    public ResponseEntity<DetailEntityResponse<UserModel>> getAgentsByCompany(@RequestParam String company)
    {
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            var userService = new UserService();
            response = userService.getAgentsByCompany(jdbcTemplate, company);
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
}

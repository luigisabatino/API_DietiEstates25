package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.dto.user.CreateUserDTO;
import com.api.dietiestates25.model.dto.user.LoginDTO;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.dto.DetailEntityDTO;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final EmailService emailService;
    public UserController(JdbcTemplate jdbcTemplate,UserService userService, EmailService emailService) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<DetailEntityDTO<UserModel>> login(@RequestBody LoginDTO dto) {
        UserModel user = new UserModel(dto);
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            response = new CodeEntitiesResponse<UserModel>(userService.login(jdbcTemplate, user));
            if(response.getCode() == 0) {
                response.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            }
            return response.toHttpEntitiesResponse();
        } catch (Exception ex) {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO dto) {
        UserModel user = new UserModel(dto);
        var response = new CodeResponse();
        try {
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
    public ResponseEntity<String> confirmUser(boolean isManagerOrAgent, @RequestBody CreateUserDTO dto) {
        UserModel user = new UserModel(dto);
        CodeResponse response = new CodeResponse();
        try {
            if(isManagerOrAgent)
                response.setCode(userService.confirmManagerOrAgent(jdbcTemplate, user));
            else
                response.setCode(userService.confirmUser(jdbcTemplate, user));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PostMapping("/createAgent")
    public ResponseEntity<String> createAgent(@RequestBody CreateUserDTO dto, @RequestHeader String sessionId) {
        UserModel user = new UserModel(dto);
        CodeResponse response = new CodeResponse();
        try {
            user.setPwd();
            response.setCode(userService.createAgent(jdbcTemplate, user, sessionId));
            if(response.getCode()==0)
                emailService.sendConfirmAccountEmail(user, 'A');
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @GetMapping("/getAgentsByCompany")
    public ResponseEntity<DetailEntityDTO<UserModel>> getAgentsByCompany(@RequestParam String company)  {
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            response.setEntities(userService.getAgentsByCompany(jdbcTemplate, company));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String sessionId) {
        var response = new CodeResponse();
        try {
            response.setCode( (userService.logout(jdbcTemplate, sessionId) ? 0 : -1) );
            return response.toHttpMessageResponse();
        }
        catch(Exception ex) {
            return response.toHttpMessageResponse(ex);
        }
    }
}
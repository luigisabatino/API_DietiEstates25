package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.dto.user.ConfirmUserDTO;
import com.api.dietiestates25.model.dto.user.CreateUserDTO;
import com.api.dietiestates25.model.dto.user.LoginDTO;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.dto.DetailEntityDTO;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.OAuthService;
import com.api.dietiestates25.service.UserService;
import com.api.dietiestates25.throwable.NoMatchCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final EmailService emailService;
    private final OAuthService oAuthService;
    public UserController(JdbcTemplate jdbcTemplate,UserService userService, EmailService emailService, OAuthService oAuthService) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailService = emailService;
        this.userService = userService;
        this.oAuthService = oAuthService;
    }
    @PostMapping("/login")
    public ResponseEntity<DetailEntityDTO<UserModel>> login(@RequestBody LoginDTO dto) {
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            UserModel user = new UserModel(dto);
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
        var response = new CodeResponse();
        try {
            UserModel user = new UserModel(dto);
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
    public ResponseEntity<String> confirmUser(boolean isManagerOrAgent, @RequestBody ConfirmUserDTO dto) {
        CodeResponse response = new CodeResponse();
        try {
            UserModel user = new UserModel(dto);
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
        CodeResponse response = new CodeResponse();
        try {
            UserModel user = new UserModel(dto);
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
    @PutMapping("changePwd")
    public ResponseEntity<String> changePwd(@RequestBody ConfirmUserDTO dto) {
        var response = new CodeResponse();
        try {
            UserModel user = new UserModel(dto);
            var rowsAffected = userService.changePwd(jdbcTemplate, user);
            response.setCode(rowsAffected == 0
                    ? -6
                    : rowsAffected
            );
            return response.toHttpMessageResponse();
        }
        catch(Exception ex) {
            return response.toHttpMessageResponse(ex);
        }
    }
    @GetMapping("generateOtp")
    public ResponseEntity<String> generateOtp(@RequestParam EmailService.OtpKey key, @RequestParam String email) {
        var response = new CodeResponse();
        try {
            UserModel user = new UserModel(email);
            if(userService.insertOtp(jdbcTemplate, user))
                emailService.sendOtpEmail(user, key);
            response.setCode(0);
            return response.toHttpMessageResponse();
        }
        catch(Exception ex) {
            return response.toHttpMessageResponse(ex);
        }
    }
    @GetMapping("/login3part")
    public ResponseEntity<DetailEntityDTO<UserModel>> login3part(@RequestParam String code) {
        var response = new CodeEntitiesResponse<UserModel>();
        try {
            UserModel user = oAuthService.getUserInfoFromCode(code);
            if(user == null) {
                response.setCode(-6);
                return response.toHttpEntitiesResponse();
            }
            response = new CodeEntitiesResponse<UserModel>(userService.load3partUser(jdbcTemplate, user));
            if(response.getCode() == 0) {
                response.addInEntities(userService.getUserByEmail(jdbcTemplate, user.getEmail()));
            }
            return response.toHttpEntitiesResponse();
        } catch (Exception ex) {
            return response.toHttpEntitiesResponse(ex);
        }
    }
}
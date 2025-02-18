package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.SessionResponse;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SessionResponse> login(@RequestBody UserModel user) {
        try {
            var loginResponse = user.login(jdbcTemplate);

            if (loginResponse.getSessionid() != null) {
                return ResponseEntity.ok(loginResponse);
            } else if (loginResponse.getMessage().contains("Invalid credentials.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginResponse);
            }
        } catch (Exception ex) {
            var errorResponse = new SessionResponse();
            errorResponse.setMessage("An error occurred during login.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        try {
            user.setOtp();
            int responseCode = user.createUser(jdbcTemplate);
            if(responseCode != 0)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email address already registered.");
            emailService.sendEmail(
                    user.getEmail(),
                    "DietiEstates Account Verification",
                    emailService.activationEmailFromTemplateBody(user.getFirstName(), user.getOtp())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Account Created.");
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
    }

    @GetMapping("/confirmUser")
    public ResponseEntity<String> confirmUser(String email, String otp) {
        try {
            UserModel user = new UserModel();
            user.setEmail(email);
            user.setOtp(otp);
            return ((user.confirmUser(jdbcTemplate) == 0) ? ResponseEntity.ok("Account successfully created!") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP code."));
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
    }

}

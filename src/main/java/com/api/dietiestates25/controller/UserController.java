package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.LoginResponseModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponseModel> login(String email, String pwd) {
        try {
            UserModel user = new UserModel();
            user.setEmail(email);
            user.setPwd(pwd);
            var loginResponse = user.login(jdbcTemplate);
            return (loginResponse.getSessionid()!=null) ? ResponseEntity.ok(loginResponse) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginResponse);
            }
        catch(Exception ex)
        {
            var exFormat = new LoginResponseModel();
            exFormat.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exFormat);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<String> createUser(@RequestBody UserModel user) {
        try {
            int responseCode = user.createUser(jdbcTemplate);
            if(responseCode < 10)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account already exist in our systems");
            emailService.sendEmail(user.getEmail(), "Confirm Account", "Hi,\nInsert this otp code in DietiEstates for activate your account:\n\n" + responseCode + "\n\nThanks.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account Created.");
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
            return ((user.confirmUser(jdbcTemplate) == 0) ? ResponseEntity.ok("User confermated") : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Input not valid."));
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
    }


}

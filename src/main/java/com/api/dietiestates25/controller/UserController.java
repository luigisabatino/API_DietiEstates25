package com.api.dietiestates25.controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api.dietiestates25.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {

    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel.LoginResponseModel> login(@RequestBody com.api.dietiestates25.model.UserModel user) {
        try {
            var loginResponse = user.login(jdbcTemplate);
            return (loginResponse.getSessionid()!=null) ? ResponseEntity.ok(loginResponse) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(loginResponse);
            }
        catch(Exception ex)
        {
            var exFormat = new UserModel.LoginResponseModel();
            exFormat.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exFormat);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<Integer> createUser(@RequestBody com.api.dietiestates25.model.UserModel user) {
        try {
            return ResponseEntity.ok(user.create_user(jdbcTemplate));
        }
        catch(Exception ex)
        {
            var exFormat = new UserModel.LoginResponseModel();
            exFormat.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
        }
    }

}

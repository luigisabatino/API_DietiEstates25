package com.api.dietiestates25.controller;

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
    @Autowired
    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody com.api.dietiestates25.model.UserModel user) {
        try {
            if (user.existUserInDB(jdbcTemplate))
                return ResponseEntity.ok("Login OK!");
            else
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data with this email/pwd combination.");
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

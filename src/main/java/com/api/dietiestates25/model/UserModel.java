package com.api.dietiestates25.model;

import com.api.dietiestates25.model.response.SessionResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;

@Getter
@Setter
public class UserModel {
    private String email;
    private String pwd;
    private String firstName;
    private String lastName;
    private String company;
    private String otp;

    public UserModel() { }

    public void setOtp() {
        SecureRandom random = new SecureRandom();
        int _otp = 100000 + random.nextInt(900000);
        otp = String.valueOf(_otp);
    }

}

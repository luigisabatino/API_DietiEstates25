package com.api.dietiestates25.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public UserModel(ResultSet rs) throws SQLException {
        email = rs.getString("email");
        pwd = rs.getString("pwd");
        firstName = rs.getString("firstName");
        lastName = rs.getString("lastName");
        company = rs.getString("company");
    }

    public void setPwd() {
        String alphabet = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!&$_<>";
        SecureRandom random = new SecureRandom();
        String pwdRandom = "";
        for(int i = 0; i < 10; i++){
            pwdRandom += alphabet.charAt(random.nextInt(alphabet.length()));
        }
        pwd = pwdRandom;
    }

    public void setOtp() {
        SecureRandom random = new SecureRandom();
        int _otp = 100000 + random.nextInt(900000);
        otp = String.valueOf(_otp);
    }

    public void encodePwd() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setPwd(encoder.encode(pwd));
    }
}

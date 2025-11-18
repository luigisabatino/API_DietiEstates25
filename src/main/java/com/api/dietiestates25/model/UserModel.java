package com.api.dietiestates25.model;

import com.api.dietiestates25.model.dto.user.ConfirmUserDTO;
import com.api.dietiestates25.model.dto.user.CreateUserDTO;
import com.api.dietiestates25.model.dto.user.LoginDTO;
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
    private String userType;
    private boolean confirmed;
    private String companyName;

    public UserModel() { }
    public UserModel(ResultSet rs) throws SQLException {
        email = rs.getString("email");
        firstName = rs.getString("firstName");
        lastName = rs.getString("lastName");
        company = rs.getString("company");
        userType = rs.getString("usertype");
        confirmed = rs.getBoolean("confirmed");
        companyName = rs.getString("companyname");
    }
    public UserModel(LoginDTO loginDTO) {
        email = loginDTO.getEmail();
        pwd = loginDTO.getPwd();
    }
    public UserModel(CreateUserDTO createUserDTO) {
        email = createUserDTO.getEmail();
        pwd = createUserDTO.getPwd();
        firstName = createUserDTO.getFirstName();
        lastName = createUserDTO.getLastName();
        otp = createUserDTO.getOtp();
    }
    public UserModel(ConfirmUserDTO confirmUserDTO) {
        email = confirmUserDTO.getEmail();
        pwd = confirmUserDTO.getPwd();
        otp = confirmUserDTO.getTemporaryPwd();
    }
    public UserModel(String email) {
        this.email = email;
    }
    public void setPwd() {
        String alphabet = "1234567890QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm!&$_";
        SecureRandom random = new SecureRandom();
        StringBuilder pwdRandom = new StringBuilder(10);

        for (int i = 0; i < 10; i++)
            pwdRandom.append(alphabet.charAt(random.nextInt(alphabet.length())));

        pwd = pwdRandom.toString();
    }
    public void setOtp() {
        SecureRandom random = new SecureRandom();
        int tmpOtp = 100000 + random.nextInt(900000);
        otp = String.valueOf(tmpOtp);
    }
    public void encodePwd() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setPwd(encoder.encode(pwd));
    }
    public void encodeOtp() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        setOtp(encoder.encode(otp));
    }
}

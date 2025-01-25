package com.api.dietiestates25.model;
import org.springframework.jdbc.core.JdbcTemplate;


public class UserModel {
    private String email;
    private String pwd;
    public UserModel() { }
    public String getEmail() {
        return email;
    }
    public void setEmail(String _email) {
        email = _email;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String _pwd) {
        pwd = _pwd;
    }

    public boolean existUserInDB(JdbcTemplate jdbcTemplate) {
        String query = "SELECT COUNT(*) FROM utente WHERE email = ? AND pwd = ?";
        return ( (jdbcTemplate.queryForObject(query, Integer.class, email, pwd)) > 0);

    }
}

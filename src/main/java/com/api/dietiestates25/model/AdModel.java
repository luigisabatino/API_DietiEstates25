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
public class AdModel {
    private double price;
    private String nation;
    private String county;
    private String city;
    private String zipcode;
    private String address;
    private String agent;

    public AdModel() { }

}

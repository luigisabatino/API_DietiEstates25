package com.api.dietiestates25.model;

import com.api.dietiestates25.model.response.SessionResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyModel {
    private String vatNumber;
    private String companyName;

    public void setVatNumber(String _vatNumber) {vatNumber = _vatNumber;}
    public String getVatNumber() {return vatNumber;}

    public void setCompanyName(String _companyName) {companyName = _companyName;}
    public String getCompanyName() {return companyName;}

}

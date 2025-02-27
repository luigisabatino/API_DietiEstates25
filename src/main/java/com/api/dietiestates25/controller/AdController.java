package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.request.AdRequest;
import com.api.dietiestates25.service.AdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdController {

    private final JdbcTemplate jdbcTemplate;

    public AdController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/insertAd")
    public ResponseEntity<String> insertAd(@RequestBody AdRequest ad)
    {
        try {
            var adService = new AdService();
            if(adService.insertAd(jdbcTemplate, ad) == 0) {
                return ResponseEntity.ok("");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
        }
    }
}

package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.request.EntityRequest;
import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.AdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdController {

    private final JdbcTemplate jdbcTemplate;

    public AdController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostMapping("/insertAd")
    public ResponseEntity<CodeResponse> insertAd(@RequestHeader String sessionId, @RequestBody AdModel ad)
    {
        try {
            var adService = new AdService();
            CodeResponse response = adService.insertAd(jdbcTemplate, sessionId, ad);
            if(response.getCode() == 0)
                return ResponseEntity.ok(response);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception ex)
        {
            var errorResponse = new CodeResponse();
            errorResponse.setMessage("An error occurred during insert ad.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

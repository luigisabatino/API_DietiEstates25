package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.request.EntityRequest;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BidController {

    private final JdbcTemplate jdbcTemplate;

    public BidController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/insertBid")
    public ResponseEntity<CodeResponse> insertBid(@RequestHeader String sessionId, @RequestBody BidModel bid)
    {
        try {
            var bidService = new BidService();
            CodeResponse response = bidService.insertBid(jdbcTemplate, sessionId, bid);
            if(response.getCode() == 0)
                return ResponseEntity.ok(response);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception ex)
        {
            var errorResponse = new CodeResponse();
            errorResponse.setMessage("An error occurred during insert bid.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/cancelBid")
    public ResponseEntity<CodeResponse> cancelBid(@RequestHeader String sessionId, @RequestParam int bidId)
    {
        try {
            var bidService = new BidService();
            CodeResponse response = bidService.cancelBid(jdbcTemplate, sessionId, bidId);
            if(response.getCode() == 0)
                return ResponseEntity.ok(response);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch(Exception ex)
        {
            var errorResponse = new CodeResponse();
            errorResponse.setMessage("An error occurred during cancel bid.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}

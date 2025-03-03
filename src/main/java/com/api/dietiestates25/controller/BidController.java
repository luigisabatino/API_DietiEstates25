package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
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

@RestController
public class BidController {

    private final JdbcTemplate jdbcTemplate;

    public BidController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/insertBid")
    public ResponseEntity<CodeResponse> insertBid(@RequestHeader String sessionId, @RequestBody BidModel bid)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode(bidService.insertBid(jdbcTemplate, sessionId, bid));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            response.setCode(-99);
            response.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/cancelBid")
    public ResponseEntity<CodeResponse> cancelBid(@RequestHeader String sessionId, int bidId)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode(bidService.cancelBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            response.setCode(-99);
            response.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/refuseBid")
    public ResponseEntity<CodeResponse> refuseBid(@RequestHeader String sessionId, int bidId)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode(bidService.refuseBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            response.setCode(-99);
            response.setMessage(ex.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

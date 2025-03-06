package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.service.BidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
            return response.toHttpResponse(ex);
        }
    }

    @PutMapping("/cancelBid")
    public ResponseEntity<CodeResponse> cancelBid(@RequestHeader String sessionId, @RequestParam int bidId)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode(bidService.cancelBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @PutMapping("/refuseBid")
    public ResponseEntity<CodeResponse> refuseBid(@RequestHeader String sessionId, @RequestBody BidModel bid)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode(bidService.refuseBid(jdbcTemplate, sessionId, bid));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @GetMapping("/getBids")
    public ResponseEntity<CodeEntitiesResponse<BidModel>> getBids(@RequestParam BidService.BidsKey key, @RequestParam String value)
    {
        try {
            var bidService = new BidService();
            return ResponseEntity.ok(bidService.getBids(jdbcTemplate, key, value));
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}

package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.service.BidService;
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
    public ResponseEntity<CodeResponse> cancelBid(@RequestHeader String sessionId, @RequestParam int bidId, boolean isCounteroffer)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode((isCounteroffer)? bidService.cancelCounteroffer(jdbcTemplate, sessionId, bidId)
                    : bidService.cancelBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }

    @PutMapping("/acceptOrRefuseBid")
    public ResponseEntity<CodeResponse> acceptOrRefuseBid(@RequestHeader String sessionId, @RequestBody BidModel bid, boolean isCounteroffer)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode((isCounteroffer)? bidService.refuseCounteroffer(jdbcTemplate, sessionId, bid.getId())
                    : bidService.acceptOrRefuseBid(jdbcTemplate, sessionId, bid));
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

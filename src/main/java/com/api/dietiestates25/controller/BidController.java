package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.BidWithCounterofferModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.DetailEntityResponse;
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
    public ResponseEntity<DetailEntityResponse<BidModel>> insertBid(@RequestHeader String sessionId, @RequestBody BidModel bid)
    {
        var response = new CodeEntitiesResponse<BidModel>();
        try {
            var bidService = new BidService();
            response.setCode(bidService.insertBid(jdbcTemplate, sessionId, bid));
            if(response.getCode() > 0)
                response.addInEntities(bidService.getBidFromId(jdbcTemplate, response.getCode()));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @PutMapping("/cancelBid")
    public ResponseEntity<String> cancelBid(@RequestHeader String sessionId, @RequestParam int bidId, boolean isCounteroffer)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode((isCounteroffer)? bidService.cancelCounteroffer(jdbcTemplate, sessionId, bidId)
                    : bidService.cancelBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PutMapping("/acceptOrRefuseBid")
    public ResponseEntity<String> acceptOrRefuseBid(@RequestHeader String sessionId, @RequestBody BidModel bid, boolean isCounteroffer)
    {
        var response = new CodeResponse();
        try {
            var bidService = new BidService();
            response.setCode((isCounteroffer)? bidService.refuseCounteroffer(jdbcTemplate, sessionId, bid.getId())
                    : bidService.acceptOrRefuseBid(jdbcTemplate, sessionId, bid));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }

    @GetMapping("/getBids")
    public ResponseEntity<DetailEntityResponse<BidWithCounterofferModel>> getBids(@RequestParam BidService.BidsKey key, @RequestParam String value)
    {
        CodeEntitiesResponse<BidWithCounterofferModel> response = new CodeEntitiesResponse<BidWithCounterofferModel>();
        try {
            var bidService = new BidService();
            response.setEntities(bidService.getBids(jdbcTemplate, key, value));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
}

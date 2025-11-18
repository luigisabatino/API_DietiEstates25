package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.BidModel;
import com.api.dietiestates25.model.CounterOfferModel;
import com.api.dietiestates25.model.dto.bid.AcceptOrRefuseBidDTO;
import com.api.dietiestates25.model.dto.bid.InsertBidDTO;
import com.api.dietiestates25.model.dto.counteroffer.AcceptOrRefuseCounterofferDTO;
import com.api.dietiestates25.model.extention.BidWithCounterofferModel;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.dto.DetailEntityDTO;
import com.api.dietiestates25.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class BidController {

    private final JdbcTemplate jdbcTemplate;
    private final BidService bidService;

    public BidController(JdbcTemplate jdbcTemplate, BidService bidService) {
        this.jdbcTemplate = jdbcTemplate;
        this.bidService = bidService;
    }

    @PostMapping("/insertBid")
    public ResponseEntity<DetailEntityDTO<BidModel>> insertBid(@RequestHeader String sessionId, @RequestBody InsertBidDTO dto)
    {
        var response = new CodeEntitiesResponse<BidModel>();
        try {
            BidModel bid = new BidModel(dto);
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
    public ResponseEntity<String> cancelBid(@RequestHeader String sessionId, @RequestParam int bidId)
    {
        var response = new CodeResponse();
        try {
            response.setCode(bidService.cancelBid(jdbcTemplate, sessionId, bidId));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PutMapping("/acceptOrRefuseBid")
    public ResponseEntity<String> acceptOrRefuseBid(@RequestHeader String sessionId, @RequestBody AcceptOrRefuseBidDTO dto)
    {
        var response = new CodeResponse();
        try {
            BidModel bid = new BidModel(dto);
            response.setCode(bidService.acceptOrRefuseBid(jdbcTemplate, sessionId, bid));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PutMapping("/cancelCounteroffer")
    public ResponseEntity<String> cancelCounteroffer(@RequestHeader String sessionId, @RequestParam int coId)
    {
        var response = new CodeResponse();
        try {
            response.setCode(bidService.cancelCounteroffer(jdbcTemplate, sessionId, coId));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @PutMapping("/acceptOrRefuseCounteroffer")
    public ResponseEntity<String> acceptOrRefuseCounteroffer(@RequestHeader String sessionId, @RequestBody AcceptOrRefuseCounterofferDTO dto)
    {
        var response = new CodeResponse();
        try {
            CounterOfferModel co = new CounterOfferModel(dto);
            response.setCode(bidService.acceptOrRefuseCounteroffer(jdbcTemplate, sessionId, co));
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
    @GetMapping("/getBids")
    public ResponseEntity<DetailEntityDTO<BidWithCounterofferModel>> getBids(@RequestParam BidService.BidsKey key, @RequestParam String value)
    {
        CodeEntitiesResponse<BidWithCounterofferModel> response = new CodeEntitiesResponse<>();
        try {
            response.setEntities(bidService.getBids(jdbcTemplate, key, value));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
}

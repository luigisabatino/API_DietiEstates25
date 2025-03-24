package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.request.SearchAdRequest;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.service.AdService;
import com.api.dietiestates25.service.ExternalApiService;
import com.api.dietiestates25.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class AdController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private ExternalApiService apiService;

    public AdController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/insertAd")
    public ResponseEntity<AdModel> insertAd(@RequestHeader String sessionId, @RequestBody AdModel ad)
    {
        var response = new CodeEntitiesResponse<AdModel>();
        try {
            ad.valorizePlacesInterest(apiService.placesInterestNearby(ad.getCoordinates()));
            var adService = new AdService();
            response.setCode(adService.insertAd(jdbcTemplate, sessionId, ad));
            if(response.getCode() > 0)
                response.addInEntities(adService.getAdById(jdbcTemplate, response.getCode()));
            return ResponseEntity.ok(response.getEntities().getFirst());//response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return null; //response.toHttpResponse(ex);
        }
    }

    @PutMapping("/searchAd")
    public ResponseEntity<CodeEntitiesResponse<AdModel>> searchAd(@RequestBody SearchAdRequest request)
    {
        try {
            var adService = new AdService();
            return ResponseEntity.ok(adService.searchAd(jdbcTemplate, request));
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    @DeleteMapping("/deleteAd")
    public ResponseEntity<CodeResponse> deleteAd(@RequestHeader String sessionId, @RequestParam int id)
    {
        var response = new CodeEntitiesResponse<AdModel>();
        try {
            var adService = new AdService();
            response.setCode(adService.deleteAd(jdbcTemplate, sessionId, id));
            if(response.getCode() == 0) {
                ImageService imageService = new ImageService();
                imageService.deleteImagesByPrefix(id + "_");
            }
            return response.toHttpResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpResponse(ex);
        }
    }


}

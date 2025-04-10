package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.dto.ad.InsertAdDTO;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.model.dto.DetailEntityDTO;
import com.api.dietiestates25.service.AdService;
import com.api.dietiestates25.service.ExternalApiService;
import com.api.dietiestates25.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdController {

    private final JdbcTemplate jdbcTemplate;
    private final ExternalApiService apiService;
    private final AdService adService;
    public AdController(JdbcTemplate jdbcTemplate, ExternalApiService apiService, AdService adService) {
        this.jdbcTemplate = jdbcTemplate;
        this.apiService = apiService;
        this.adService = adService;
    }
    @PostMapping("/insertAd")
    public ResponseEntity<DetailEntityDTO<AdModel>> insertAd(@RequestHeader String sessionId, @RequestBody InsertAdDTO dto)
    {
        AdModel ad = new AdModel(dto);
        var response = new CodeEntitiesResponse<AdModel>();
        try {
            ad.valorizePlacesInterest(apiService.placesInterestNearby(ad.getCoordinates()));
            response.setCode(adService.insertAd(jdbcTemplate, sessionId, ad));
            if(response.getCode() > 0)
                response.addInEntities(adService.getAdById(jdbcTemplate, response.getCode()));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @PutMapping("/searchAd")
    public ResponseEntity<DetailEntityDTO<AdModel>> searchAd(@RequestBody SearchAdRequest request)
    {
        var response = new CodeEntitiesResponse<AdModel>();
        try {
            response.setEntities(adService.searchAd(jdbcTemplate, request));
            return response.toHttpEntitiesResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpEntitiesResponse(ex);
        }
    }
    @DeleteMapping("/deleteAd")
    public ResponseEntity<String> deleteAd(@RequestHeader String sessionId, @RequestParam int id)
    {
        var response = new CodeResponse();
        try {
            response.setCode(adService.deleteAd(jdbcTemplate, sessionId, id));
            if(response.getCode() == 0) {
                ImageService imageService = new ImageService();
                imageService.deleteImagesByPrefix(id + "_");
            }
            return response.toHttpMessageResponse();
        }
        catch(Exception ex)
        {
            return response.toHttpMessageResponse(ex);
        }
    }
}
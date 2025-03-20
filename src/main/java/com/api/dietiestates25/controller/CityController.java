package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.response.CodeEntitiesResponse;
import com.api.dietiestates25.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {

    private final JdbcTemplate jdbcTemplate;

    public CityController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/getRegions")
    public ResponseEntity<List<String>> getRegions()
    {
        try {
            CityService cityService = new CityService();
            return ResponseEntity.ok(cityService.getItalianRegions(jdbcTemplate));
        }
        catch(Exception ex) {
            return null;
        }
    }

    @GetMapping("/getProvinces")
    public ResponseEntity<List<String>> getProvinces(String region)
    {
        try {
            CityService cityService = new CityService();
            return ResponseEntity.ok(cityService.getItalianProvincesByRegion(jdbcTemplate, region));
        }
        catch(Exception ex) {
            return null;
        }
    }

    @GetMapping("/getCities")
    public ResponseEntity<List<String>> getCities(String province)
    {
        try {
            CityService cityService = new CityService();
            return ResponseEntity.ok(cityService.getItalianCitiesByProvince(jdbcTemplate,province));
        }
        catch(Exception ex) {
            return null;
        }
    }


}

package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.CityModel;
import com.api.dietiestates25.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CityController {

    private final JdbcTemplate jdbcTemplate;
    private final CityService cityService;
    public CityController(JdbcTemplate jdbcTemplate, CityService cityService) {

        this.jdbcTemplate = jdbcTemplate;
        this.cityService = cityService;
    }

    @GetMapping("/getRegions")
    public ResponseEntity<List<String>> getRegions()
    {
        try {
            return ResponseEntity.ok(cityService.getItalianRegions(jdbcTemplate));
        }
        catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getProvinces")
    public ResponseEntity<List<String>> getProvinces(String region)
    {
        try {
            return ResponseEntity.ok(cityService.getItalianProvincesByRegion(jdbcTemplate, region));
        }
        catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getCities")
    public ResponseEntity<List<CityModel>> getCities(String province)
    {
        try {
            return ResponseEntity.ok(cityService.getItalianCitiesByProvince(jdbcTemplate,province));
        }
        catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

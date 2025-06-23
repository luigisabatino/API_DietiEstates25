package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.CityModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CityServiceTest {

    @InjectMocks
    private CityService cityService;

    @Mock
    private JdbcTemplate jdbcTemplate;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestGetItalianRegionsSuccess() {
        List<String> regionlist = List.of("Campania", "Lazio", "Emilia Romagna");
        when(jdbcTemplate.query(
                eq("SELECT DISTINCT REGION FROM ITALIAN_CITIES ORDER BY REGION ASC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(regionlist);
        var response = cityService.getItalianRegions(jdbcTemplate);
        assertEquals(regionlist, response);
    }
    @Test
    public void TestGetItalianProvincesByRegionSuccess() {
        List<String> provinceList = List.of("Avellino","Benevento","Caserta","Napoli","Salerno");
        when(jdbcTemplate.query(
                eq("SELECT DISTINCT PROVINCE FROM ITALIAN_CITIES WHERE REGION = ? ORDER BY PROVINCE ASC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(provinceList);
        var response = cityService.getItalianProvincesByRegion(jdbcTemplate, "Campania");
        assertEquals(provinceList, response);
    }
    @Test
    public void TestGetItalianCitiesByProvinceSuccess() {
        List<String> cityList = List.of("Pozzuoli","Quarto","Giugliano in Campania");
        when(jdbcTemplate.query(
                eq("SELECT DISTINCT NAME, ISTAT_CODE FROM ITALIAN_CITIES WHERE PROVINCE = ? ORDER BY NAME ASC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(cityList);
        var response = cityService.getItalianCitiesByProvince(jdbcTemplate, "456");
        assertEquals(cityList, response);
    }
    @Test
    public void TestGetCityNameByCode() {
        when(jdbcTemplate.queryForObject(eq("SELECT NAME FROM ITALIAN_CITIES WHERE ISTAT_CODE = ?"),eq(String.class),
                any()))
                .thenReturn("Napoli");
        var response = cityService.getCityNameByCode(jdbcTemplate, "10");
        assertEquals("Napoli", response);
    }
}

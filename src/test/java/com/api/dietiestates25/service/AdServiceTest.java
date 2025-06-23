package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.UserModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.model.response.CodeResponse;
import com.api.dietiestates25.throwable.NoMatchCredentialsException;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class AdServiceTest {

    @InjectMocks
    private AdService adService;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private AdModel ad;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ad = new AdModel();
        ad.setId(1);
        ad.setCity("123");
        ad.setAddress("Via Di Test, 141");
        ad.setDescription("description");
        ad.setAgent("1234");
        ad.setDimentions(100);
        ad.setEnergyClass("A2");
        ad.setFloor(5);
        ad.setPrice(100000);
        ad.setNBathrooms(2);
        ad.setNRooms(4);
        ad.setType("S");
    }

    @Test
    public void TestInsertAdSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM INSERT_AD(?,?::numeric,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"),
                eq(Integer.class),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).
                thenReturn(0);

        var response = adService.insertAd(jdbcTemplate, "SESSIONID123", ad);
        assertEquals(0, response);
    }
    @Test
    public void testSearchAdByIdSuccess() {
        SearchAdRequest request = new SearchAdRequest();
        request.setId(1);
        AdWithGeoDataModel expectedAd = new AdWithGeoDataModel();
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM ADS_WITH_GEO_DATA WHERE ID_AD = ?"),
                any(RowMapper.class),
                eq(1)
        )).thenReturn(expectedAd);
        List<AdWithGeoDataModel> result = adService.searchAd(jdbcTemplate, request);
        assertEquals(1, result.size());
        assertEquals(expectedAd, result.get(0));
    }
    @Test
    public void testSearchAdSuccess() {
        SearchAdRequest request = new SearchAdRequest();
        request.setId(0);
        request.setCity("123");
        request.setAddress("Via Di Test, 141");
        request.setDescription("description");
        request.setAgent("1234");
        request.setDimentions(100);
        request.setEnergyClass("A2");
        request.setFloor(5);
        request.setPrice(100000);
        request.setNBathrooms(2);
        request.setNRooms(4);
        request.setType("S");
        AdWithGeoDataModel ad1 = new AdWithGeoDataModel();
        AdWithGeoDataModel ad2 = new AdWithGeoDataModel();

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(List.of(ad1, ad2));
        List<AdWithGeoDataModel> result = adService.searchAd(jdbcTemplate, request);

        assertEquals(2, result.size());
        assertTrue(result.contains(ad1));
        assertTrue(result.contains(ad2));
    }
    @Test
    public void TestDeleteAdSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM DELETE_AD(?,?)"), eq(Integer.class)
        ,any(),any())).thenReturn(0);
        var response = adService.deleteAd(jdbcTemplate,"SESSIONID123", ad.getId());
        assertEquals(0, response);
    }
}

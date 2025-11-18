package com.api.dietiestates25.service;

import com.api.dietiestates25.model.AdModel;
import com.api.dietiestates25.model.extention.AdWithGeoDataModel;
import com.api.dietiestates25.model.extention.SearchAdRequest;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    void TestInsertAdSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM INSERT_AD(?,?::numeric,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"),
                eq(Integer.class),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any(),any())).
                thenReturn(0);

        var response = adService.insertAd(jdbcTemplate, "SESSIONID123", ad);
        assertEquals(0, response);
    }
    @Test
    void testSearchAdByIdSuccess() {
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
    void TestDeleteAdSuccess() {
        when(jdbcTemplate.queryForObject(eq("SELECT * FROM DELETE_AD(?,?)"), eq(Integer.class)
        ,any(),any())).thenReturn(0);
        var response = adService.deleteAd(jdbcTemplate,"SESSIONID123", ad.getId());
        assertEquals(0, response);
    }
    @Test
    void TestRequiredValuesForAdOperations_Exception() {
        adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
        try {
            ad.setDimentions(0);
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setType("");
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setNBathrooms(0);
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setNRooms(0);
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setAddress("");
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setCity("");
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
        try {
            ad.setPrice(0);
            adService.requiredValuesForAdOperations(ad, AdService.Operation.INSERT_AD);
            assertTrue(false);
        }
        catch(RequiredParameterException rpe) {
            assertTrue(true);
        }
    }
    @Test
    void testGetAdById_Success() {
        AdWithGeoDataModel ads = new AdWithGeoDataModel();
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM ads_with_geo_data ORDER BY id_ad DESC LIMIT 1"),
                any(RowMapper.class)
        )).thenReturn(ads);
        AdWithGeoDataModel result = adService.getAdById(jdbcTemplate, 0);
        assertEquals(ads, result);
    }

    @Test
    void testSearchAdSuccess() throws Exception {
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
        ArgumentCaptor<PreparedStatementSetter> pssCaptor =
                ArgumentCaptor.forClass(PreparedStatementSetter.class);
        @SuppressWarnings("unchecked")
        ArgumentCaptor<RowMapper<AdWithGeoDataModel>> rmCaptor =
                ArgumentCaptor.forClass(RowMapper.class);
        when(jdbcTemplate.query(
                anyString(),
                pssCaptor.capture(),
                rmCaptor.capture()
        )).thenReturn(List.of(ad1, ad2));
        List<AdWithGeoDataModel> result = adService.searchAd(jdbcTemplate, request);
        assertEquals(2, result.size());
        assertTrue(result.contains(ad1));
        assertTrue(result.contains(ad2));
        PreparedStatementSetter setter = pssCaptor.getValue();
        assertNotNull(setter);
        PreparedStatement ps = mock(PreparedStatement.class);
        setter.setValues(ps);
        verify(ps).setString(1, "%1234%");
        verify(ps).setDouble(2, 100000);
        verify(ps).setDouble(3, request.getMaxPrice());
        verify(ps).setDouble(4, request.getMaxPrice());
        verify(ps).setDouble(5, request.getMaxPrice());
        verify(ps).setInt(6, 4);
        verify(ps).setInt(7, 2);
        verify(ps).setString(8, "%S%");
        verify(ps).setString(9, "%Via Di Test, 141%");
        verify(ps).setString(10, "%"+ request.getProvince() +"%");
        verify(ps).setString(11, "%"+ request.getRegion() +"%");
        verify(ps).setString(12, "%"+ request.getRegion() +"%");
        verify(ps).setString(13, "%123%");
    }

}

package com.api.dietiestates25.service;

import com.api.dietiestates25.model.response.GeoapifyResponse;
import com.api.dietiestates25.model.response.OpenstreetResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalApiServiceTest {

    private HttpClient httpClient;
    private ExternalApiService externalApiService;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        externalApiService = new ExternalApiService(httpClient);

        org.springframework.test.util.ReflectionTestUtils.setField(externalApiService, "apilayerUrl", "http://testvat.com?apikey=123");
        org.springframework.test.util.ReflectionTestUtils.setField(externalApiService, "geoUrl", "http://geoapi.com?categories=CTEGORIES&location=COORDINATS&radius=RDIUS");
        org.springframework.test.util.ReflectionTestUtils.setField(externalApiService, "geoCategories", "school");
        org.springframework.test.util.ReflectionTestUtils.setField(externalApiService, "geoRadius", "500");
        org.springframework.test.util.ReflectionTestUtils.setField(externalApiService, "openstreetUrl", "http://openstreet.com/search?");
    }

    @Test
    void testVerifyVatNumber() throws Exception {
        HttpResponse<String> mockedResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedResponse.body()).thenReturn("{\"valid\":true}");
        Mockito.when(httpClient.send(
                        Mockito.any(HttpRequest.class),
                        Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockedResponse);

        int result = externalApiService.verifyVatNumber("IT12345678901");
        assertEquals(0, result);
    }

    @Test
    void testPlacesInterestNearby_Success() throws Exception {
        HttpResponse<String> mockedResponse = mock(HttpResponse.class);
        String jsonResponse = """
        {
            "features": [
                {
                    "properties": {
                        "categories": ["school"]
                    }
                }
            ]
        }
        """;
        when(mockedResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockedResponse);

        GeoapifyResponse response = externalApiService.placesInterestNearby("45.0,9.0");

        assertEquals(1, response.getFeatures().size());
    }

    @Test
    void testCoordinatesFromAddress_Success() throws Exception {
        HttpResponse<String> mockedResponse = mock(HttpResponse.class);
        String jsonResponse = "[{ \"display_name\": \"Test Address\", \"lat\": \"45.0\", \"lon\": \"9.0\" }]";

        when(mockedResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockedResponse);

        List<OpenstreetResponse> response = externalApiService.coordinatesFromAddress("Test Address");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Test Address", response.get(0).getDisplay_name());
    }


}

package com.api.dietiestates25.service;

import com.api.dietiestates25.model.response.GeoapifyResponse;
import com.api.dietiestates25.model.response.OpenstreetResponse;
import com.api.dietiestates25.throwable.RequiredParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
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
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        HttpResponse<String> mockedResponse = Mockito.mock(HttpResponse.class);

        Mockito.when(mockedResponse.body()).thenReturn("{\"valid\":true}");
        Mockito.when(httpClient.send(
                        Mockito.any(HttpRequest.class),
                        Mockito.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(mockedResponse);

        // Inject the mocked httpClient manually or via constructor if you refactor it
        // service.setHttpClient(httpClient);

        int result = externalApiService.verifyVatNumber("IT12345678901");
        assertEquals(0, result);
    }


}

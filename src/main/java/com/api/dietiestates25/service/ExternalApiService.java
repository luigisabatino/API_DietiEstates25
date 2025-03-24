package com.api.dietiestates25.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import com.api.dietiestates25.model.response.GeoapifyResponse;
import com.api.dietiestates25.throwable.RequiredParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.api.dietiestates25.model.response.CodeResponse;

@Component
public class ExternalApiService {
    @Value("${apilayer.url}")
    private String apilayerUrl;

    @Value("${geoapify.url}")
    private String geoUrl;
    @Value("${geoapify.categories}")
    private String geoCategories;
    @Value("${geoapify.radius}")
    private String geoRadius;

    public int verifyVatNumber(String vatNumber) throws java. io. IOException, InterruptedException {
        String requestUrl = apilayerUrl + "&vat_number=" + vatNumber;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.body().contains("\"valid\":true"))
            return 0;
        return -97;
    }
    public GeoapifyResponse placesInterestNearby(String coordinates)  throws java. io. IOException, InterruptedException {
        if(coordinates==null||coordinates.isBlank())
            throw new RequiredParameterException("coordinates");
        String requestUrl =  geoUrl
                .replaceAll("CTEGORIES",geoCategories)
                .replaceAll("COORDINATS", coordinates)
                .replaceAll("RDIUS",geoRadius);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), GeoapifyResponse.class);
    }
}
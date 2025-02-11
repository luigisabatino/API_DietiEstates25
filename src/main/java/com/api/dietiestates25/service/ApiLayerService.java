package com.api.dietiestates25.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiLayerService {

    @Value("${apilayer.url}")
    private String url;

    public boolean verifyVatNumber(String vatNumber) throws java. io. IOException, InterruptedException {
        String requestUrl = url + "&vat_number=" + vatNumber;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().contains("\"valid\":true");
    }


}

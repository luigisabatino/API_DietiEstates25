package com.api.dietiestates25.service;

import com.api.dietiestates25.model.UserModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUri;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUri;
    public String getToken(String code) throws IOException, InterruptedException {
        String requestBody = "code=" + code
                + "&client_id=" + googleClientId
                + "&client_secret=" + googleClientSecret
                + "&redirect_uri=" + googleRedirectUri
                + "&grant_type=authorization_code";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(googleTokenUri))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.body());
            String accessToken = jsonNode.get("access_token").asText();
            return accessToken;
        }
        return null;
    }
    public UserModel getUserInfo(String token) throws IOException, InterruptedException {
        HttpRequest userInfoRequest = HttpRequest.newBuilder()
                .uri(URI.create(googleUserInfoUri))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> userInfoResponse = client.send(userInfoRequest, HttpResponse.BodyHandlers.ofString());
        if (userInfoResponse.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userInfo = mapper.readTree(userInfoResponse.body());
            UserModel user = new UserModel();
            user.setEmail(userInfo.get("email").asText());
            user.setFirstName(userInfo.get("given_name").asText());
            user.setLastName(userInfo.get("family_name").asText());
            return user;
        }
        return null;
    }
    public UserModel getUserInfoFromCode(String code) throws IOException, InterruptedException {
        var token = getToken(code);
        if(token == null)
            return null;
        return getUserInfo(token);
    }
}

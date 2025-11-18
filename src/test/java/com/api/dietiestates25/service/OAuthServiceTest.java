package com.api.dietiestates25.service;

import com.api.dietiestates25.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OAuthServiceTest {

    private OAuthService oAuthService;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> httpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        oAuthService = new OAuthService(httpClient);
        org.springframework.test.util.ReflectionTestUtils.setField(oAuthService, "googleClientId", "client123");
        org.springframework.test.util.ReflectionTestUtils.setField(oAuthService, "googleClientSecret", "secret123");
        org.springframework.test.util.ReflectionTestUtils.setField(oAuthService, "googleRedirectUri", "http://localhost/callback");
        org.springframework.test.util.ReflectionTestUtils.setField(oAuthService, "googleTokenUri", "https://oauth/token");
        org.springframework.test.util.ReflectionTestUtils.setField(oAuthService, "googleUserInfoUri", "https://oauth/userinfo");
    }
    @Test
    void testGetTokenSuccess() throws Exception {
        String json = "{ \"access_token\": \"TOKEN123\" }";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(json);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        String token = oAuthService.getToken("code123");
        assertEquals("TOKEN123", token);
    }
    @Test
    void testGetTokenFailure() throws Exception {
        when(httpResponse.statusCode()).thenReturn(400);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        String token = oAuthService.getToken("code123");
        assertNull(token);
    }
    @Test
    void testGetUserInfoSuccess() throws Exception {
        String json = """
            {
                "email": "test@example.com",
                "given_name": "Mario",
                "family_name": "Rossi"
            }
        """;
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(json);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        UserModel user = oAuthService.getUserInfo("token");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Mario", user.getFirstName());
        assertEquals("Rossi", user.getLastName());
    }
    @Test
    void testGetUserInfoFailure() throws Exception {
        when(httpResponse.statusCode()).thenReturn(403);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);
        UserModel user = oAuthService.getUserInfo("token");
        assertNull(user);
    }
    @Test
    void testGetUserInfoFromCodeSuccess() throws Exception {
        OAuthService spyService = Mockito.spy(oAuthService);
        UserModel mockUser = new UserModel();
        mockUser.setEmail("a@b.com");
        doReturn("TOKEN123").when(spyService).getToken(anyString());
        doReturn(mockUser).when(spyService).getUserInfo("TOKEN123");
        UserModel result = spyService.getUserInfoFromCode("XYZ");
        assertNotNull(result);
        assertEquals("a@b.com", result.getEmail());
    }
    @Test
    void testGetUserInfoFromCodeFailsDueTokenNull() throws Exception {
        OAuthService spyService = Mockito.spy(oAuthService);
        doReturn(null).when(spyService).getToken(anyString());
        UserModel result = spyService.getUserInfoFromCode("XYZ");
        assertNull(result);
    }
}

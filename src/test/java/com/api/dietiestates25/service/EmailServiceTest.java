package com.api.dietiestates25.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.api.dietiestates25.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private AmazonSimpleEmailService emailClient;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailClient = mock(AmazonSimpleEmailService.class);
        emailService = new EmailService(emailClient, "1234", "789654", "eu-west-1", "test@mail.it");
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "awsAccessKey", "access");
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "awsSecretKey", "secret");
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "awsRegion", "eu-west-1");
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "fromAddress", "noreply@test.com");
    }

    @Test
    void testSendConfirmAccountEmail_UserTypeU_Success() {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");
        user.setFirstName("Mario");
        user.setOtp("123456");

        boolean result = emailService.sendConfirmAccountEmail(user, 'U');

        assertTrue(result);

        ArgumentCaptor<SendEmailRequest> requestCaptor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(emailClient, times(1)).sendEmail(requestCaptor.capture());

        SendEmailRequest sentRequest = requestCaptor.getValue();
        assertEquals("noreply@test.com", sentRequest.getSource());
        assertTrue(sentRequest.getDestination().getToAddresses().contains("test@example.com"));
        assertEquals("DietiEstates Account Verification", sentRequest.getMessage().getSubject().getData());
        assertTrue(sentRequest.getMessage().getBody().getHtml().getData().contains("123456")); // Verifica che l'OTP sia nel corpo
    }

    @Test
    void testSendConfirmAccountEmail_UserTypeA_Success() {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");
        user.setFirstName("Mario");
        user.setOtp("tempPwd123");

        boolean result = emailService.sendConfirmAccountEmail(user, 'A');

        assertTrue(result);

        ArgumentCaptor<SendEmailRequest> requestCaptor = ArgumentCaptor.forClass(SendEmailRequest.class);
        verify(emailClient, times(1)).sendEmail(requestCaptor.capture());

        SendEmailRequest sentRequest = requestCaptor.getValue();
        assertTrue(sentRequest.getMessage().getBody().getHtml().getData().contains("tempPwd123"));
        assertTrue(sentRequest.getMessage().getBody().getHtml().getData().contains("Agent")); // Verifica che venga usato il template corretto
    }
}

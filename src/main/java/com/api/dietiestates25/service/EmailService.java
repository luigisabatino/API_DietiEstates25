package com.api.dietiestates25.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.api.dietiestates25.model.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Component
public class EmailService {

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${email.sender}")
    private String fromAddress;

    private String toAddress;
    private String subject;
    private String body;

    private final AmazonSimpleEmailService emailClient;

    public EmailService(AmazonSimpleEmailService _emailClient,
                        @Value("${aws.access.key}") String awsAccessKey,
                        @Value("${aws.secret.key}") String awsSecretKey,
                        @Value("${aws.region}") String awsRegion,
                        @Value("${email.sender}") String fromAddress) {
        this.emailClient = _emailClient;
        this.awsAccessKey = awsAccessKey;
        this.awsSecretKey = awsSecretKey;
        this.awsRegion = awsRegion;
        this.fromAddress = fromAddress;
    }

    private boolean sendEmail(String _toAddress, String _subject, String _body) {
        toAddress = _toAddress;
        subject = _subject;
        body = _body;
        return sendEmail();
    }

    private boolean sendEmail() {
        try {
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey);
        /*AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(this.awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();*/
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(fromAddress)
                    .withDestination(new Destination().withToAddresses(toAddress))
                    .withMessage(new Message()
                            .withSubject(new Content().withData(subject))
                            .withBody(new Body()
                                    .withHtml(new Content().withData(body))
                                    .withText(new Content().withData("Please view this email in a web browser."))  // Se HTML fallisce
                            ));
            emailClient.sendEmail(request);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void sendEmail(List<String> emails) {
        for (String email : emails) {
            sendEmail(email, subject, body);
        }
    }

    public boolean sendConfirmAccountEmail(UserModel user, char usertype) {
        try {
            return sendEmail(
                    user.getEmail(),
                    "DietiEstates Account Verification",
                    (usertype=='U') ?
                            templateOTP(user.getFirstName(), user.getOtp(), OtpKey.CreateUser)
                            : templateActivationEmailTempPwd(user.getFirstName(), user.getOtp(),((usertype=='A')?"Agent":"Manager"))
            );
        } catch (Exception ex) {
            return false;
        }
    }
    public boolean sendOtpEmail(UserModel user, OtpKey key) {
        try {
            return sendEmail(
                    user.getEmail(),
                    "DietiEstates " + (key.equals(OtpKey.ChangePwd) ? "Password Change" : "Account Verification"),
                    templateOTP("", user.getOtp(), key));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    private String templateOTP(String name, String otp, OtpKey key) throws IOException {
        return new String(Objects.requireNonNull(getClass().getResourceAsStream("/emailTemplates/otp.html"))
                .readAllBytes(), StandardCharsets.UTF_8)
                .replace("${name}", key.equals(OtpKey.ChangePwd) ? "" : name)
                .replace("${otp}", otp)
                .replace("${message}", getMessageFromOtpKey(key));
    }
    private String templateActivationEmailTempPwd(String name, String pwd, String usertype) throws IOException {
        return new String(Objects.requireNonNull(getClass().getResourceAsStream("/emailTemplates/tempPwd.html"))
                .readAllBytes(), StandardCharsets.UTF_8)
                .replace("${name}", name)
                .replace("${pwd}", pwd)
                .replace("${usertype}", usertype);
    }
    public enum OtpKey {
        ChangePwd,
        CreateUser
    }
    private String getMessageFromOtpKey(OtpKey key) {
        return switch (key) {
            case ChangePwd -> "Please use this OTP to complete your password change.";
            case CreateUser -> "Thank you for creating your DietiEstates account.";
        };
    }
}
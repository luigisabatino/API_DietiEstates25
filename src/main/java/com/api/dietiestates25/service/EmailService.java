package com.api.dietiestates25.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
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

    public void sendEmail(String _toAddress, String _subject, String _body) {
        toAddress = _toAddress;
        subject = _subject;
        body = _body;
        sendEmail();
    }

    public void sendEmail() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsAccessKey, this.awsSecretKey);
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(this.awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        SendEmailRequest request = new SendEmailRequest()
                .withSource(fromAddress)
                .withDestination(new Destination().withToAddresses(toAddress))
                .withMessage(new Message()
                        .withSubject(new Content().withData(subject))
                        .withBody(new Body()
                                .withHtml(new Content().withData(body))
                                .withText(new Content().withData("Please view this email in a web browser."))  // Se HTML fallisce
                        ));

        client.sendEmail(request);
    }

    public void sendEmail(List<String> emails) {
        for (String email : emails) {
            sendEmail(email, subject, body);
        }
    }

    public String activationEmailFromTemplateBody(String name, String otp) throws IOException {
        return new String(Objects.requireNonNull(getClass().getResourceAsStream("/emailTemplates/otp.html"))
                .readAllBytes(), StandardCharsets.UTF_8)
                .replace("${name}", name)
                .replace("${otp}", otp);
    }

}

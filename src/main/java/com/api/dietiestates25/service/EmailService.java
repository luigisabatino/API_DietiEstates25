package com.api.dietiestates25.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private void setToAddress(String _toAddress){toAddress = _toAddress;}
    private String getToAddress(){return toAddress;}

    private void setSubject(String _subject) {subject = _subject;}
    private String getSubject(){return subject;}

    private void setBody(String _body){body = _body;}
    private String getBody(){return body;}

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
                        .withBody(new Body().withText(new Content().withData(body))));
        client.sendEmail(request);
    }

    public void sendEmail(List<String> emails) {
        for (String email : emails) {
            sendEmail(email,subject,body);
        }
    }

}

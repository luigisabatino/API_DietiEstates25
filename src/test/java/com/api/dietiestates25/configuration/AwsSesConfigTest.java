package com.api.dietiestates25.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AwsSesConfigTest {

    private AwsSesConfig awsSesConfig;

    @BeforeEach
    void setUp() {
        awsSesConfig = new AwsSesConfig();
        ReflectionTestUtils.setField(awsSesConfig, "awsAccessKey", "testAccessKey");
        ReflectionTestUtils.setField(awsSesConfig, "awsSecretKey", "testSecretKey");
        ReflectionTestUtils.setField(awsSesConfig, "awsRegion", "us-east-1");
    }

    @Test
    void amazonSimpleEmailService_Success() {
        var s3 = awsSesConfig.amazonSimpleEmailService();
        assertNotNull(s3);
    }
}

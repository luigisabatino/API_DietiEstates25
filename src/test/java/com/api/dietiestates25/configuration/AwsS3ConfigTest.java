package com.api.dietiestates25.configuration;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class AwsS3ConfigTest {

    private AwsS3Config awsS3Config;

    @BeforeEach
    void setUp() {
        awsS3Config = new AwsS3Config();
        ReflectionTestUtils.setField(awsS3Config, "awsAccessKey", "testAccessKey");
        ReflectionTestUtils.setField(awsS3Config, "awsSecretKey", "testSecretKey");
        ReflectionTestUtils.setField(awsS3Config, "awsRegion", "us-east-1");
    }

    @Test
    void amazonS3_Success() {
        AmazonS3 s3 = awsS3Config.amazonS3();
        assertNotNull(s3);
    }
}

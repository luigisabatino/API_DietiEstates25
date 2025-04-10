package com.api.dietiestates25.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.api.dietiestates25.model.dto.ImageDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageService {
    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.secret.key}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("s3.bucket")
    private String imageBucket;

    AmazonS3 client;

    public ImageService() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        client = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public void uploadImage(ImageDTO request) {
        byte[] imageBytes = Base64.getDecoder().decode(request.getBase64Image());
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        metadata.setContentType("image/jpeg");
        client.putObject(imageBucket, request.getFileName(), inputStream, metadata);
    }

    public List<ImageDTO> getImagesByPrefix(String prefix) throws Exception {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(imageBucket)
                .withPrefix(prefix);
        ListObjectsV2Result result = client.listObjectsV2(request);
        List<ImageDTO> images = new ArrayList<>();
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            S3Object s3Object = client.getObject(imageBucket, objectSummary.getKey());
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            images.add(new ImageDTO(objectSummary.getKey(), base64Image));
        }
        return images;
    }

    public boolean deleteImagesByPrefix(String prefix) {
        try {
            List<ImageDTO> images = getImagesByPrefix(prefix);
            for(var img : images) {
                deleteImg(img.getFileName());
            }
            return true;
        }
        catch(Exception ex) {
            return false;
        }
    }

    private void deleteImg(String fileName) {
        client.deleteObject(new DeleteObjectRequest(imageBucket, fileName));
    }

}

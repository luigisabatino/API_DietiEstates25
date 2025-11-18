package com.api.dietiestates25.service;

import com.amazonaws.services.s3.AmazonS3;
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

@Component
public class ImageService {

    private final AmazonS3 s3Client;

    @Value("${s3.bucket}")
    public String imageBucket;

    public ImageService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadImage(ImageDTO request) {
        byte[] imageBytes = Base64.getDecoder().decode(request.getBase64Image());
        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        metadata.setContentType("image/jpeg");

        s3Client.putObject(imageBucket, request.getFileName(), inputStream, metadata);
    }

    public List<ImageDTO> getImagesByPrefix(String prefix) throws java.io.IOException {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(imageBucket)
                .withPrefix(prefix);
        ListObjectsV2Result result = s3Client.listObjectsV2(request);

        List<ImageDTO> images = new ArrayList<>();
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            S3Object s3Object = s3Client.getObject(imageBucket, objectSummary.getKey());
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
            for (ImageDTO img : images) {
                deleteImg(img.getFileName());
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private void deleteImg(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(imageBucket, fileName));
    }
}

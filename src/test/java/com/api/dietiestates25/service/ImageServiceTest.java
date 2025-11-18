package com.api.dietiestates25.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.api.dietiestates25.model.dto.ImageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceTest {

    private ImageService imageService;
    private AmazonS3 mockS3;

    @BeforeEach
    void setUp() {
        mockS3 = mock(AmazonS3.class);
        imageService = new ImageService(mockS3);
        imageService.imageBucket = "test-bucket";
    }

    @Test
    void testUploadImage_Success() {
        String base64 = Base64.getEncoder().encodeToString("test-image".getBytes());
        ImageDTO dto = new ImageDTO("file.jpg", base64);
        imageService.uploadImage(dto);
        verify(mockS3, times(1))
                .putObject(eq("test-bucket"), eq("file.jpg"), any(), any(ObjectMetadata.class));
    }
    @Test
    void testGetImagesByPrefix_Success() throws Exception {
        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3InputStream = new S3ObjectInputStream(new ByteArrayInputStream("test".getBytes()), null);
        when(s3Object.getObjectContent()).thenReturn(s3InputStream);
        S3ObjectSummary summary = new S3ObjectSummary();
        summary.setKey("file.jpg");
        ListObjectsV2Result result = mock(ListObjectsV2Result.class);
        when(result.getObjectSummaries()).thenReturn(List.of(summary));
        when(mockS3.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(result);
        when(mockS3.getObject("test-bucket", "file.jpg")).thenReturn(s3Object);
        List<ImageDTO> images = imageService.getImagesByPrefix("prefix");
        assertEquals(1, images.size());
    }
    @Test
    void testDeleteImagesByPrefix_Success() throws Exception {
        S3Object s3Object = mock(S3Object.class);
        S3ObjectInputStream s3InputStream = new S3ObjectInputStream(new ByteArrayInputStream("test".getBytes()), null);
        when(s3Object.getObjectContent()).thenReturn(s3InputStream);
        S3ObjectSummary summary = new S3ObjectSummary();
        summary.setKey("test.jpg");
        ListObjectsV2Result result = mock(ListObjectsV2Result.class);
        when(result.getObjectSummaries()).thenReturn(List.of(summary));
        when(mockS3.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(result);
        when(mockS3.getObject("test-bucket", "test.jpg")).thenReturn(s3Object);
        boolean deleted = imageService.deleteImagesByPrefix("prefix");
        verify(mockS3, times(1)).deleteObject(any(DeleteObjectRequest.class));
        assertEquals(true, deleted);
    }

}

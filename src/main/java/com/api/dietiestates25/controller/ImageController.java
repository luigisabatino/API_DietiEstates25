package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.dto.ImageDTO;
import com.api.dietiestates25.service.CompanyService;
import com.api.dietiestates25.service.EmailService;
import com.api.dietiestates25.service.ExternalApiService;
import com.api.dietiestates25.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ImageController {

    private final ImageService imageService;
    public ImageController(ImageService imageService)
    {
        this.imageService = imageService;
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestBody ImageDTO request) {
        try {
            imageService.uploadImage(request);
            return ResponseEntity.ok("Ok");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Base64 format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("File upload failed: " + e.getMessage());
        }
    }
    @GetMapping("/getImagesByAd")
    public ResponseEntity<List<ImageDTO>> getAdImages(@RequestParam int idAd) {
        try {
            return ResponseEntity.ok(imageService.getImagesByPrefix(idAd + "_"));
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAdImage(@RequestParam int idAd) {
        var response = imageService.deleteImagesByPrefix(idAd + "_");
        return ((response)?ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false));
    }
}

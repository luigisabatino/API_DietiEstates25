package com.api.dietiestates25.controller;

import com.api.dietiestates25.model.ImageModel;
import com.api.dietiestates25.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-controller")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestBody ImageModel request) {
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
    public ResponseEntity<List<ImageModel>> getAdImages(@RequestParam int idAd) {
        try {
            return ResponseEntity.ok(imageService.getImagesByPrefix(idAd + "_"));
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteAdImage(@RequestParam int idAd) {
        imageService.deleteImagesByPrefix(idAd + "_");
        return ResponseEntity.ok(true);
    }

}

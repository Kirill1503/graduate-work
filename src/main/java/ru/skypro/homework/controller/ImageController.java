package ru.skypro.homework.controller;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageController {

    // Путь к директории, где хранятся изображения (можно задать в application.properties)
    private final String imagesDir = "images";

    @GetMapping(value = "/images/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get(imagesDir).resolve(filename).normalize();
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Image not found");
            }
            Resource resource = new UrlResource(imagePath.toUri());
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Error while loading image", e);
        } catch (Exception e) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", e);
        }
    }
}

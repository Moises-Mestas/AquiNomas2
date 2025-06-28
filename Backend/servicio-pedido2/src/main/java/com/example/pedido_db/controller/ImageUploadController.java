package com.example.pedido_db.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/upload")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "Frontend/public/";

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            String imageName = image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + imageName);

            // Guardar la imagen en el directorio
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());

            // Devolver la URL de la imagen para mostrarla en el frontend
            return ResponseEntity.ok(new ImageUploadResponse("/public/" + imageName));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen");
        }
    }

    static class ImageUploadResponse {
        private String imageUrl;

        public ImageUploadResponse(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}

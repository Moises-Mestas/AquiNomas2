package com.example.pedido_db.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@RestController
@RequestMapping("/upload")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "uploads/"; // Directorio donde se guardarán las imágenes

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("imagen") MultipartFile image) {
        try {
            // Generar un nombre único para la imagen para evitar conflictos
            String imageName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + imageName);

            // Crear el directorio si no existe
            Files.createDirectories(path.getParent());

            // Guardar la imagen en el directorio
            Files.write(path, image.getBytes());

            // URL relativa para acceder a la imagen desde el frontend
            String imageUrl = "/uploads/" + imageName;

            // Retornar la URL de la imagen para que el frontend pueda mostrarla
            return ResponseEntity.ok(new ImageUploadResponse(imageUrl));
        } catch (IOException e) {
            // En caso de error, se responde con el código de error 500
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
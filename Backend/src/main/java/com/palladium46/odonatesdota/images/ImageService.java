package com.palladium46.odonatesdota.images;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${backend.server}")
    private String backendServerAdress;

    @Value("${images.upload.dir}")
    private String imageDirectory;

    @Value("${default-image.dir}")
    private String DefaultImageDirectory;

    public ResponseEntity<byte[]> getDefaultImageUrl() throws IOException {
        ClassPathResource imgFile = new ClassPathResource("templates/default-image.png");
        byte[] imageData = StreamUtils.copyToByteArray(imgFile.getInputStream());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }

    public String saveImage(MultipartFile imageFile) throws IOException {
        System.out.println("Entrée dans saveImage");
        System.out.println("Nom du fichier reçu : " + imageFile.getOriginalFilename());

        String uniqueFileName = saveImageToStorage(imageDirectory, imageFile);
        System.out.println("Image sauvegardée avec le nom : " + uniqueFileName);

        String imageUrl = backendServerAdress + "/api/images/" + uniqueFileName;
        System.out.println("URL de l'image générée : " + imageUrl);

        return imageUrl;
    }

    public String saveImageToStorage(String uploadDirectory, MultipartFile file) throws IOException {
        System.out.println("Entrée dans saveImageToStorage");
        System.out.println("Répertoire de sauvegarde : " + uploadDirectory);
        System.out.println("Nom du fichier original : " + file.getOriginalFilename());

        String uniqueFileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        System.out.println("Nom de fichier unique généré : " + uniqueFileName);

        Path uploadPath = Paths.get(uploadDirectory).toAbsolutePath().normalize();
        System.out.println("Chemin absolu du répertoire d'upload : " + uploadPath);

        if (!Files.exists(uploadPath)) {
            System.out.println("Répertoire d'upload n'existe pas, création du répertoire...");
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        System.out.println("Chemin complet du fichier : " + filePath);

        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fichier copié avec succès à l'emplacement : " + filePath);
        } catch (IOException ex) {
            System.out.println("Erreur lors de la sauvegarde du fichier : " + ex.getMessage());
            throw new IOException("Could not save image file: " + uniqueFileName, ex);
        }

        return uniqueFileName;
    }

    public ResponseEntity<byte[]> getImageResponse(String imageName) {
        try {
            byte[] imageData = getImage(imageName);
            String fileExtension = StringUtils.getFilenameExtension(imageName).toLowerCase();
            MediaType mediaType = getMediaTypeForFileName(fileExtension);

            return ResponseEntity.ok().contentType(mediaType).body(imageData);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private byte[] getImage(String imageName) throws IOException {
        Path imagePath = Path.of(imageDirectory, imageName);
        if (Files.exists(imagePath)) {
            return Files.readAllBytes(imagePath);
        } else {
            throw new IOException("Image not found");
        }
    }

    public ResponseEntity<String> deleteImage(String imageName) {
        try {
            Path imagePath = Path.of(imageDirectory, imageName);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
                return ResponseEntity.ok().body("Image deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image");
        }
    }

    private MediaType getMediaTypeForFileName(String fileExtension) {
        switch (fileExtension) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpeg":
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }


}

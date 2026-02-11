package com.palladium46.odonatesdota.images;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public  ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) {
        System.out.println(imageName);
        return imageService.getImageResponse(imageName);
    }

    @DeleteMapping("/{imageName}")
    public ResponseEntity<String> deleteImage(@PathVariable String imageName) {
        return imageService.deleteImage(imageName);
    }

    @GetMapping("/default")
    public ResponseEntity<byte[]> getDefaultImage() throws IOException {
        return imageService.getDefaultImageUrl();
    }

}
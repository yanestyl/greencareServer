package com.yanestyl.greencare.controllers;

import com.yanestyl.greencare.dto.PhotoRequest;
import com.yanestyl.greencare.entity.Photo;
import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.repository.PhotoRepository;
import com.yanestyl.greencare.services.PhotoService;
import com.yanestyl.greencare.services.RequestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/photos")
@AllArgsConstructor
@Slf4j
public class PhotoController {

    private PhotoService photoService;
    private PhotoRepository photoRepository;

    @PostMapping("/add")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
                                              @RequestParam("request_id") String requestIdStr,
                                              @RequestParam("is_main") String isMainStr,
                                              @RequestHeader("Authorization") String authorizationHeader) {

        try {
            String jwtToken = authorizationHeader.substring(7);

            PhotoRequest photoRequest = PhotoRequest.builder()
                    .request_id(Long.parseLong(requestIdStr))
                    .isMain("1".equals(isMainStr))
                    .build();

            photoService.savePhotosToDiskAndDatabase(photoRequest, file, jwtToken);

            return ResponseEntity.ok("Изображение успешно загружено");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка загрузки изображения: " + e.getMessage());
        }
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long photoId) throws IOException {
        Optional <Photo> photoOptional = photoRepository.findById(photoId);

        if (photoOptional.isPresent()) {
            Photo photo = photoOptional.get();
            byte[] photoData = photoService.loadPhotoData(photo.getFilepath(), photo.getFilename());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photoData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
package com.yanestyl.greencare.services.impl;


import com.yanestyl.greencare.dto.PhotoRequest;
import com.yanestyl.greencare.entity.Photo;
import com.yanestyl.greencare.entity.Request;
import com.yanestyl.greencare.entity.User;
import com.yanestyl.greencare.repository.PhotoRepository;
import com.yanestyl.greencare.repository.UserRepository;
import com.yanestyl.greencare.services.JWTService;
import com.yanestyl.greencare.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Value("${D:\\diplom\\images}")
    private String uploadDir;


    @Override
    public byte[] loadPhotoData(String filePath, String fileName) throws IOException {
        Path photoPath = Paths.get(filePath, fileName);

        // проверяем существует ли
        if (Files.exists(photoPath)) {
            return  Files.readAllBytes(photoPath);
        } else {
            throw new FileNotFoundException("File not found: " + photoPath);
        }
    }

    @Override
    public void savePhotosToDiskAndDatabase(PhotoRequest photoRequest, MultipartFile photoFile, String jwtToken) {

        // получаем юзера по номеру из jwt
        String userPhoneNumber = jwtService.extractUserName(jwtToken);
        Optional<User> userOptional = userRepository.findByPhoneNumber(userPhoneNumber);

        if (userOptional.isPresent()) {
            // Создаем директорию для текущей даты и заявки
            String currentDateDir = uploadDir + File.separator + LocalDate.now().toString() + File.separator + photoRequest.getRequest_id();
            Path dirPath = Paths.get(currentDateDir);

            // создаем директорию
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                // Обработка ошибок при создании директории
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

            // Сохраняем каждое фото в директорию с уникальным именем
            String uniqueFileName = userOptional.get().getId() + "_" + UUID.randomUUID() + ".jpg";

            // Создаем новую фотографию
            Photo photo = Photo.builder()
                    .request(Request.builder().id(photoRequest.getRequest_id()).build())
                    .filename(uniqueFileName)
                    .filepath(currentDateDir)
                    .isMain(photoRequest.isMain())
                    .build();

            // Сохраняем фото в базу данных
            photoRepository.save(photo);

            Path filePath = Paths.get(currentDateDir, uniqueFileName);
            try {
                // Сохраняем фото на диск
                Files.write(filePath, photoFile.getBytes());
            } catch (IOException e) {
                // Обработка ошибок при сохранении файла
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

    }

}

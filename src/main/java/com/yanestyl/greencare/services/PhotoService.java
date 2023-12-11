package com.yanestyl.greencare.services;

import com.yanestyl.greencare.dto.PhotoRequest;
import com.yanestyl.greencare.entity.Photo;
import com.yanestyl.greencare.entity.Request;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PhotoService {

    byte[] loadPhotoData(String filePath, String fileName) throws IOException;

    void savePhotosToDiskAndDatabase(PhotoRequest photoRequest, MultipartFile photoFiles, String jwtToken);

}

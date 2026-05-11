package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final String uploadDir = "uploads/";

    @Override
    public String uploadFile(MultipartFile file) {

        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace(); // 🔥 important for debugging
            throw new RuntimeException("File upload failed");
        }

        return "http://localhost:8080/uploads/" + fileName;
    }
}

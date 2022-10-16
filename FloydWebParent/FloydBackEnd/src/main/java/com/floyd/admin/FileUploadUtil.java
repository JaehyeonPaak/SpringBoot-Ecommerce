package com.floyd.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static void cleanDir(String uploadDir) {
        Path dirPath = Paths.get(uploadDir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);

                    } catch (IOException e) {
                        LOGGER.error("Could not delete file: " + file);
                    }
                }
            });
        }
        catch (IOException e) {
            LOGGER.error("Could not list directory: " + dirPath);
        }
    }

    public static void removeDir(String uploadDir) {
        cleanDir(uploadDir);
        try {
            Files.delete(Paths.get(uploadDir));

        } catch (IOException e) {
            LOGGER.error("Could not remove directory: " + uploadDir);
        }
    }
}
package com.rayan.restaurant.service.impl;

import com.rayan.restaurant.exception.StorageException;
import com.rayan.restaurant.service.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    @Value("$app.storage.location:uploads")
    private String storageLAction;
    private Path rootLocation;


    @PostConstruct // after class is constructed, this method will be run.
    public void init() throws IOException {
        try {
            rootLocation = Paths.get(storageLAction);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            String extension = StringUtils.getFilenameExtension(filename);
            String finalFileName = filename + "." + extension;

            Path destinationFile = rootLocation
                    .resolve(Paths.get(finalFileName))
                    .normalize()
                    .toAbsolutePath();
            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
                throw new StorageException("Could not store file outside specified directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                return finalFileName;
            }
        } catch (IOException e) {
            throw new StorageException("Could not store file ", e);
        }
    }

    @Override
    public Optional<Resource> loadResource(String id) {
        return Optional.empty();
    }
}

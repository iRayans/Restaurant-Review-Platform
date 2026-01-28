package com.rayan.restaurant.service.impl;

import com.rayan.restaurant.domain.Photo;
import com.rayan.restaurant.service.PhotoService;
import com.rayan.restaurant.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;

    @Override
    public Photo uploadPhoto(MultipartFile file) {
        String randomUUID = UUID.randomUUID().toString();
        String url = storageService.store(file, randomUUID);

        return Photo.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadResource(id);
    }
}

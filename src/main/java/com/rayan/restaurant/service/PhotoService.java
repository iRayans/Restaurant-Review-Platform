package com.rayan.restaurant.service;

import com.rayan.restaurant.domain.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {
    Photo uploadPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(String id);
}

package com.example.wallpapers.service;

import com.example.wallpapers.pojo.ImageMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    void init();

    ImageMetadata save(MultipartFile file, String filename);

    Resource load(String filename);

    void update(MultipartFile newFile, String filename);

    boolean delete(String filename);

    void deleteAll();

    Stream<Path> loadAll();
}

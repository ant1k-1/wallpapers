package com.example.wallpapers.service;

import com.example.wallpapers.pojo.ImageMetadata;
import com.luciad.imageio.webp.WebPWriteParam;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class LowResolutionStorage implements FileStorageService{

    private final Path root = Paths.get("lowres");
    private float compression = 0.2f;
    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }

    }

    @Override
    public ImageMetadata save(MultipartFile file, String filename) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());

            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            //Set lossy compression
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            //Set 80% quality.
            writeParam.setCompressionQuality(compression);


            // Save the image
            writer.setOutput(
                    new FileImageOutputStream(
                            new File(String.valueOf(
                                    root.resolve(filename)
                            ))
                    )
            );
            writer.write(null, new IIOImage(image, null, null), writeParam);
            return new ImageMetadata(
                    image.getWidth() + "x" + image.getHeight(),
                    file.getSize()
            );
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void update(MultipartFile newFile, String filename) {
        try {
            Path oldFile = root.resolve(filename);
            if (Files.deleteIfExists(oldFile)) {
                save(newFile, filename);
            } else {
                save(newFile, filename);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    public void setPreviewQuality(float percentFloat) {
        compression = percentFloat;
    }

    public Integer getPreviewQuality() {
        return (int) (compression * 100);
    }
}

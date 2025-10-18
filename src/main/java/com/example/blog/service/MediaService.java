package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.entity.Media;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    @Value("${app.file.upload-dir:uploads}")
    protected String uploadDir;

    @Value("${app.file.base-url:http://localhost:8080/uploads/}")
    protected String baseUrl;

    public List<String> uploadFile(MultipartFile[] files) throws IOException {
        Path rootLocation = Paths.get(uploadDir);
        Files.createDirectories(rootLocation);

        List<String> fileUrls = new ArrayList<>();
        List<Media> mediaList = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();
            Path targetDir;

            if (contentType != null && contentType.startsWith("image/")) {
                targetDir = rootLocation.resolve("images");
            } else if (contentType != null && contentType.startsWith("video/")) {
                targetDir = rootLocation.resolve("videos");
            } else {
                targetDir = rootLocation.resolve("others");
            }

            Files.createDirectories(targetDir);

            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            String uuidName = UUID.randomUUID().toString();
            String newFileName = extension.isEmpty() ? uuidName : uuidName + "." + extension;

            Path destinationFile = targetDir.resolve(newFileName);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = rootLocation.relativize(destinationFile).toString().replace("\\", "/");
            String fileUrl = baseUrl.endsWith("/") ? baseUrl + relativePath : baseUrl + "/" + relativePath;

            fileUrls.add(fileUrl);
            log.info("Uploaded file: {}", fileUrl);

            mediaList.add(Media.builder()
                    .id(uuidName)
                    .name(originalFilename != null ? originalFilename : newFileName)
                    .url(fileUrl)
                    .type(extension)
                    .size(file.getSize())
                    .build());
        }

        if (!mediaList.isEmpty()) {
            mediaRepository.saveAll(mediaList);
        }

        return fileUrls;
    }

    public boolean deleteFileByUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) return false;

        // Tìm media trong DB theo url
        Media media = mediaRepository.findByUrl(fileUrl).orElse(null);
        if (media == null) {
            log.warn("File not found in DB: {}", fileUrl);
            return false;
        }

        try {
            // Lấy path thực tế từ URL
            Path rootLocation = Paths.get(uploadDir);
            URI uri = new URI(fileUrl);
            String relativePath = uri.getPath().replaceFirst("/uploads/", "");
            Path filePath = rootLocation.resolve(relativePath).normalize();

            // Xóa file trên filesystem nếu tồn tại
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Deleted file: {}", filePath);
            } else {
                log.warn("File not found on disk: {}", filePath);
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }

            // Xóa record trong DB
            mediaRepository.delete(media);
            log.info("Deleted media record from DB: {}", fileUrl);

            return true;
        } catch (IOException e) {
            log.error("Error deleting file: {}", fileUrl, e);
            return false;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}

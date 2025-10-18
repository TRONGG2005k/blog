package com.example.blog.controller;

import com.example.blog.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/uploads")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/multi-file")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        try {
            if (files == null || files.length == 0) {
                return ResponseEntity.badRequest().body("No files uploaded");
            }

            List<String> fileUrls = mediaService.uploadFile(files);
            return ResponseEntity.ok(fileUrls);

        } catch (IOException e) {
            log.error("File upload failed: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to upload files");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFileByUrl(@RequestBody List<String> fileUrls) {
        boolean allDeleted = true;
        for (String url : fileUrls) {
            allDeleted &= mediaService.deleteFileByUrl(url);
        }
        if (allDeleted) return ResponseEntity.ok("All files deleted");
        else return ResponseEntity.status(207).body("Some files could not be deleted");
    }
}

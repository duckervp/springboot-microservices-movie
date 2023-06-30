package com.duckervn.authservice.controller;

import com.duckervn.authservice.service.IFileStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileStoreController {

    private final IFileStoreService fileStoreService;

    @GetMapping("/users/stored-files")
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(fileStoreService.getStoredImageUrls().toArray());
    }
}

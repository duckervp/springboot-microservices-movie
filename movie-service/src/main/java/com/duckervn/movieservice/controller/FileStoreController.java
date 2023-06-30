package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.service.IFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileStoreController {

    private final IFileService fileService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/movies/stored-files")
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllStoredFiles());
    }
}

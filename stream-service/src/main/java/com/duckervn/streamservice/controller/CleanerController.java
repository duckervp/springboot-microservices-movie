package com.duckervn.streamservice.controller;

import com.duckervn.streamservice.common.Response;
import com.duckervn.streamservice.service.CleanerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cleaner")
public class CleanerController {

    private final CleanerService cleanerService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/clean")
    public Mono<ResponseEntity<Response>> clean(){
        return Mono.just(ResponseEntity.ok(cleanerService.clean()));
    }


    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/download-all-images")
    public Mono<Void> download() throws InterruptedException {
        cleanerService.downloadAllImages();
        return Mono.empty();
    }
}

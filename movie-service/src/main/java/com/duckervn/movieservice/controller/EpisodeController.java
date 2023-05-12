package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findepisode.EpisodeOutput;
import com.duckervn.movieservice.service.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episodes")
@RequiredArgsConstructor
public class EpisodeController {
    private final IEpisodeService episodeService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody EpisodeInput episodeInput) {
        return new ResponseEntity<>(episodeService.save(episodeInput), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEpisodeOutputById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.findEpisodeOutputById(id));
    }
}

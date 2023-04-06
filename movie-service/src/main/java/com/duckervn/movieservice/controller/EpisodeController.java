package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findepisode.EpisodeOutput;
import com.duckervn.movieservice.service.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {
    private final IEpisodeService episodeService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody EpisodeInput episodeInput) {
        return new ResponseEntity<>(episodeService.save(episodeInput), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEpisodeOutputById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.findEpisodeOutputById(id));
    }
}

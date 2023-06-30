package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.service.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{episodeId}")
    public ResponseEntity<?> update(@PathVariable Long episodeId, @RequestBody EpisodeInput episodeInput) {
        return ResponseEntity.ok(episodeService.update(episodeId, episodeInput));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{episodeId}")
    public ResponseEntity<?> delete(@PathVariable Long episodeId) {
        return ResponseEntity.ok(episodeService.delete(episodeId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> episodeIds) {
        return ResponseEntity.ok(episodeService.delete(episodeIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findEpisodeById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.findEpisode(id));
    }
}

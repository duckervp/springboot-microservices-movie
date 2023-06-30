package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.service.IMovieCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/c")
public class MovieCharacterController {

    private final IMovieCharacterService movieCharacterService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> add(@RequestParam Long movieId, @RequestParam List<Long> characterIds) {
        return ResponseEntity.ok(movieCharacterService.add(movieId, characterIds));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long movieId, @RequestParam List<Long> characterIds) {
        return ResponseEntity.ok(movieCharacterService.delete(movieId, characterIds));
    }
}

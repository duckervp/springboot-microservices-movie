package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.service.IMovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies/g")
public class MovieGenreController {

    private final IMovieGenreService movieGenreService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> add(@RequestParam Long movieId, @RequestParam List<Long> genreIds) {
        return ResponseEntity.ok(movieGenreService.add(movieId, genreIds));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long movieId, @RequestParam List<Long> genreIds) {
        return ResponseEntity.ok(movieGenreService.delete(movieId, genreIds));
    }
}

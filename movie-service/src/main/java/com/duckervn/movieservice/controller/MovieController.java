package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final IMovieService movieService;

    @GetMapping()
    public ResponseEntity<?> findMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
            ) {
        return ResponseEntity.ok(movieService.findMovie(name, releaseYear, country, genre, pageNo, pageSize));
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody @Valid MovieInput movieInput) {
        return new ResponseEntity<>(movieService.save(movieInput), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieInput movieInput) {
        return ResponseEntity.ok(movieService.update(id, movieInput));
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.delete(id));
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteMovie(@RequestParam List<Long> movieIds) {
        return ResponseEntity.ok(movieService.delete(movieIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.findMovie(id));
    }

    @GetMapping("/s/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(movieService.findMovie(slug));
    }

    @GetMapping("/slug/reset")
    public ResponseEntity<?> resetSlug() {
        return ResponseEntity.ok(movieService.resetSlug());
    }
}

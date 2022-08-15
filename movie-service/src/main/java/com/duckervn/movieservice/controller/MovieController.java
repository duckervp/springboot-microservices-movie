package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final IMovieService movieService;
//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping()
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @PostMapping()
    public void save(@RequestBody Movie movie) {
        movieService.save(movie);
    }

    @GetMapping("/{id}")
    public Movie findById(@PathVariable Long id) {
        return movieService.findById(id);
    }
}

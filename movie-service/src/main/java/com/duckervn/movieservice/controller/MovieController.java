package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final IMovieService movieService;
//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping()
    public PageOutput<?> findMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
            ) {
        return movieService.findMovie(name, releaseYear, country, genreId, pageNo, pageSize);
    }

    @PostMapping()
    public void save(@RequestBody MovieInput movieInput) {
        movieService.save(movieInput);
    }

    @GetMapping("/{id}")
    public Movie findById(@PathVariable Long id) {
        return movieService.findById(id);
    }
}

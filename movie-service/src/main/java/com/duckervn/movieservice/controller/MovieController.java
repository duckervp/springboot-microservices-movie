package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        PageOutput<?> pageOutput = movieService.findMovie(name, releaseYear, country, genre, pageNo, pageSize);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_MOVIES)
                .results(pageOutput.getContent())
                .pageSize(pageOutput.getPageSize())
                .totalElements(pageOutput.getTotalElements())
                .pageNo(pageOutput.getPageNo()).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping()
    public ResponseEntity<?> save(@RequestBody @Valid MovieInput movieInput) {
        Response response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_MOVIE)
                .result(movieService.save(movieInput)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieInput movieInput) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_MOVIE)
                .result(movieService.update(id, movieInput))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_MOVIE).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteMovie(@RequestParam List<Long> movieIds) {
        movieService.delete(movieIds);
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.DELETED_MOVIE)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_MOVIE)
                .result(movieService.findById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ids")
    public ResponseEntity<?> findByIds(@RequestParam List<Long> values) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_ALL_MOVIES)
                .results(movieService.findByIds(values))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/s/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_MOVIE)
                .result(movieService.findMovie(slug)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/reset")
    public ResponseEntity<?> resetSlug() {
        movieService.resetSlug();
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.RESET_SLUG).build();
        return ResponseEntity.ok(response);
    }
}

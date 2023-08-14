package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.service.IMovieCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        movieCharacterService.add(movieId, characterIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.ADDED_MOVIE_CHARACTERS).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long movieId, @RequestParam List<Long> characterIds) {
        movieCharacterService.delete(movieId, characterIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_MOVIE_CHARACTERS).build();
        return ResponseEntity.ok(response);
    }
}

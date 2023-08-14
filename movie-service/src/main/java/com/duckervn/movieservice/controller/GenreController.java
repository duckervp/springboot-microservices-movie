package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.service.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final IGenreService genreService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid GenreInput genreInput) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_GENRE)
                .result(genreService.save(genreInput)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{genreId}")
    public ResponseEntity<?> update(@PathVariable Long genreId, @RequestBody @Valid GenreInput genreInput) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_GENRE)
                .result(genreService.update(genreId, genreInput))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> delete(@PathVariable Long genreId) {
        genreService.delete(genreId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_GENRE).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> genreIds) {
        genreService.delete(genreIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_GENRES).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_ALL_GENRES)
                .results(genreService.findAll()).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_GENRE)
                .result(genreService.findById(id)).build();

        return ResponseEntity.ok(response);
    }
}

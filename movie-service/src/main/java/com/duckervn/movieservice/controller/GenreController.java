package com.duckervn.movieservice.controller;

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
        return new ResponseEntity<>(genreService.save(genreInput), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{genreId}")
    public ResponseEntity<?> update(@PathVariable Long genreId, @RequestBody @Valid GenreInput genreInput) {
        return ResponseEntity.ok(genreService.update(genreId, genreInput));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> delete(@PathVariable Long genreId) {
        return ResponseEntity.ok(genreService.delete(genreId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> genreIds) {
        return ResponseEntity.ok(genreService.delete(genreIds));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findGenre(id));
    }
}

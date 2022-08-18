package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.service.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {
    private final IGenreService genreService;

    @PostMapping
    public void save(@RequestBody GenreInput genreInput) {
        genreService.save(genreInput);
    }

    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable Long id) {
        return genreService.findById(id);
    }
}

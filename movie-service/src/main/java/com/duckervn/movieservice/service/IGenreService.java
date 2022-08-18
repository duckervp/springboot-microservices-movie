package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;

import java.util.List;

public interface IGenreService {
    Genre findById(Long id);

    void save(GenreInput genreInput);

    List<Genre> findAll();
}

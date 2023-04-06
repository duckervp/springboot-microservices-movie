package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;

public interface IGenreService {
    Response findById(Long id);

    Response save(GenreInput genreInput);

    void save(Genre genre);

    Response findAll();
}

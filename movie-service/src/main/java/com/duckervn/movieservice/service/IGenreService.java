package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;

import java.util.List;

public interface IGenreService {
    Response findGenre(Long id);

    Genre findById(Long id);

    Response save(GenreInput genreInput);

    void save(Genre genre);

    Response findAll();

    Response update(Long genreId, GenreInput genreInput);

    Response delete(Long genreId);

    Response delete(List<Long> genreIds);
}

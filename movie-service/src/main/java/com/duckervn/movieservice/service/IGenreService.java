package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;

import java.util.List;

public interface IGenreService {

    Genre findById(Long id);

    Genre save(GenreInput genreInput);

    void save(Genre genre);

    List<Genre> findAll();

    Genre update(Long genreId, GenreInput genreInput);

    void delete(Long genreId);

    void delete(List<Long> genreIds);
}

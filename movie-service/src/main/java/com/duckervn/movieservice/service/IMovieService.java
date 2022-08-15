package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Movie;

import java.util.List;

public interface IMovieService {
    void save(Movie movie);
    Movie findById(Long id);
    List<Movie> findAll();
}

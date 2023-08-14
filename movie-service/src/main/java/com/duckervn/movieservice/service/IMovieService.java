package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;

import java.util.List;

public interface IMovieService {
    Movie save(MovieInput movieInput);

    Movie findMovie(String slug);

    Movie findById(Long id);

    PageOutput<?> findMovie(String name, Integer releaseYear, String country, String genre, Integer pageNo, Integer pageSize);

    Movie update(Long id, MovieInput movieInput);

    void delete(Long id);

    void resetSlug();

    void delete(List<Long> ids);
}

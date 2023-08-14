package com.duckervn.movieservice.service;

import java.util.List;

public interface IMovieGenreService {
    void add(Long movieId, List<Long> genreIds);

    void delete(Long movieId, List<Long> genreIds);
}

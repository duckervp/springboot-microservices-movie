package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;

import java.util.List;

public interface IMovieGenreService {
    Response add(Long movieId, List<Long> genreIds);

    Response delete(Long movieId, List<Long> genreIds);
}

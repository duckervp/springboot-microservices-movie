package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;

import java.util.List;

public interface IMovieService {
    Response save(MovieInput movieInput);

    Response findMovie(Long id);

    Response findMovie(String slug);

    Movie findById(Long id);

    Response findMovie(String name, Integer releaseYear, String country, String genre, Integer pageNo, Integer pageSize);

    Response update(Long id, MovieInput movieInput);

    Response delete(Long id);

    MovieDTO findMovieDTOById(Long id);

    Response resetSlug();

    Response delete(List<Long> ids);
}

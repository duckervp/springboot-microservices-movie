package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;

public interface IMovieService {
    Response save(MovieInput movieInput);
    Response findMovie(Long id);

    Movie findById(Long id);

    Response findMovie(String name, Integer releaseYear, String country, Long genreId, Integer pageNo, Integer pageSize);
    Response update(MovieInput movieInput);
    MovieDTO findMovieDTOById(Long id);
}

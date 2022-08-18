package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;

public interface IMovieService {
    void save(MovieInput movieInput);
    Movie findById(Long id);
    PageOutput<?> findMovie(String name, Integer releaseYear, String country, Long genreId, Integer pageNo, Integer pageSize);
    void update(MovieInput movieInput);
    MovieDTO findMovieDTOById(Long id);
}

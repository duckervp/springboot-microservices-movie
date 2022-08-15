package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.service.IMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;
    /**
     * @param movie m
     */
    @Override
    public void save(Movie movie) {
        movieRepository.save(movie);
    }

    /**
     * @param id i
     * @return movie
     */
    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }

    /**
     * @return list movie
     */
    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }
}

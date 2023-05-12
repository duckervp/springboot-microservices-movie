package com.duckervn.movieservice.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomMovieRepository {
    List<?> findMovieOutput(String name, Integer releaseYear, String country, String genre, Pageable pageable);
}

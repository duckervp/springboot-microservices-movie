package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.model.page.PageOutput;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomMovieRepository {
    PageOutput<?> findMovieOutput(String name, Integer releaseYear, String country, String genre, Pageable pageable);
}

package com.duckervn.movieservice.service;

import java.util.List;

public interface IMovieCharacterService {
    void add(Long movieId, List<Long> characterIds);

    void delete(Long movieId, List<Long> characterIds);
}

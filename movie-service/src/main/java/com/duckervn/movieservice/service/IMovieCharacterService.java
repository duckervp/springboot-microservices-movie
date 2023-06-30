package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;

import java.util.List;

public interface IMovieCharacterService {
    Response add(Long movieId, List<Long> characterIds);

    Response delete(Long movieId, List<Long> characterIds);
}

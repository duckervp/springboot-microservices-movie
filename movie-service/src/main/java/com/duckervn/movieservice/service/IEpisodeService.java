package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;

public interface IEpisodeService {
    Response save(EpisodeInput episodeInput);

    Response findById(Long id);

    Response findEpisodeOutputById(Long id);
}

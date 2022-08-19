package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findepisode.EpisodeOutput;

public interface IEpisodeService {
    void save(EpisodeInput episodeInput);

    Episode findById(Long id);

    EpisodeOutput findEpisodeOutputById(Long id);
}

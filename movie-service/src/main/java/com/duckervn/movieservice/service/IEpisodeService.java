package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;

import java.util.List;

public interface IEpisodeService {
    Episode save(EpisodeInput episodeInput);

    Episode findById(Long id);

    Episode update(Long episodeId, EpisodeInput episodeInput);

    void delete(Long episodeId);

    void delete(List<Long> episodeIds);
}

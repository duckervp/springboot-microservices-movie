package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;

import java.util.List;

public interface IEpisodeService {
    Response save(EpisodeInput episodeInput);

    Episode findById(Long id);

    Response findEpisode(Long id);

    Object update(Long episodeId, EpisodeInput episodeInput);

    Object delete(Long episodeId);

    Object delete(List<Long> episodeIds);
}

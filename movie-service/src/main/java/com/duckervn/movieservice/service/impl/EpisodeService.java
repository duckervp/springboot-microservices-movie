package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.dto.EpisodeDTO;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findEpisode.EpisodeOutput;
import com.duckervn.movieservice.repository.EpisodeRepository;
import com.duckervn.movieservice.service.IEpisodeService;
import com.duckervn.movieservice.service.IMovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EpisodeService implements IEpisodeService {
    private final EpisodeRepository episodeRepository;

    private final IMovieService movieService;

    private final ObjectMapper objectMapper;
    /**
     * @param episodeInput episode
     */
    @Override
    public void save(EpisodeInput episodeInput) {
        System.out.println(episodeInput);
        Episode episode = objectMapper.convertValue(episodeInput, Episode.class);
        System.out.println(episode.getId());
        Movie movie = movieService.findById(episodeInput.getMovieId());
        episode.addMovie(movie);
        movie.addEpisode(episode);
        episodeRepository.save(episode);
    }

    /**
     * @param id id
     * @return
     */
    @Override
    public Episode findById(Long id) {
        return episodeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Episode not found with id " + id));
    }

    /**
     * @param id id
     * @return episode output
     */
    @Override
    public EpisodeOutput findEpisodeOutputById(Long id) {
        EpisodeDTO episode = episodeRepository.findEpisodeDTOById(id)
                .orElseThrow(() -> new IllegalArgumentException("EpisodeDTO not found with id " + id));
        EpisodeOutput episodeOutput = objectMapper.convertValue(episode, EpisodeOutput.class);
        episodeOutput.setMovie(movieService.findMovieDTOById(episode.getMovieId()));
        return episodeOutput;
    }
}

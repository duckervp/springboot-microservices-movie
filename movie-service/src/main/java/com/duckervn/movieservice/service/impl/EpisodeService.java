package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.domain.dto.EpisodeDTO;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findepisode.EpisodeOutput;
import com.duckervn.movieservice.repository.EpisodeRepository;
import com.duckervn.movieservice.service.IEpisodeService;
import com.duckervn.movieservice.service.IMovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    public Response save(EpisodeInput episodeInput) {
        Episode episode = objectMapper.convertValue(episodeInput, Episode.class);
        if (Objects.nonNull(episodeInput.getMovieId())) {
            Movie movie = movieService.findById(episodeInput.getMovieId());
            episode.addMovie(movie);
            movie.addEpisode(episode);
        }
        episodeRepository.save(episode);
        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_EPISODE).build();
    }

    /**
     * @param id id
     * @return Response
     */
    @Override
    public Response findById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_EPISODE).result(episode).build();
    }

    /**
     * @param id id
     * @return episode output
     */
    @Override
    public Response findEpisodeOutputById(Long id) {
        EpisodeDTO episode = episodeRepository.findEpisodeDTOById(id)
                .orElseThrow(ResourceNotFoundException::new);
        EpisodeOutput episodeOutput = objectMapper.convertValue(episode, EpisodeOutput.class);
        episodeOutput.setMovie(movieService.findMovieDTOById(episode.getMovieId()));
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_EPISODE).result(episodeOutput).build();
    }
}

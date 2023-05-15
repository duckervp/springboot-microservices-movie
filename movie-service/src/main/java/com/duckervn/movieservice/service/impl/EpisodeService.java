package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.domain.dto.EpisodeDTO;
import com.duckervn.movieservice.domain.entity.Character;
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

import java.time.LocalDateTime;
import java.util.List;
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
        episode.setCreatedAt(LocalDateTime.now());
        episode.setModifiedAt(LocalDateTime.now());
        episodeRepository.save(episode);
        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_EPISODE).build();
    }

    @Override
    public Episode findById(Long id) {
        return episodeRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @param id id
     * @return Response
     */
    @Override
    public Response findEpisode(Long id) {
        Episode episode = findById(id);
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



    @Override
    public Object update(Long episodeId, EpisodeInput episodeInput) {
        Episode episode = findById(episodeId);

        if (Objects.nonNull(episodeInput.getName())) {
            episode.setName(episodeInput.getName());
        }

        if (Objects.nonNull(episodeInput.getDescription())) {
            episode.setDescription(episodeInput.getDescription());
        }

        if (Objects.nonNull(episodeInput.getUrl())) {
            episode.setUrl(episodeInput.getUrl());
        }

        if (Objects.nonNull(episodeInput.getDuration())) {
            episode.setDuration(episodeInput.getDuration());
        }

        episode.setModifiedAt(LocalDateTime.now());

        episodeRepository.save(episode);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.UPDATED_EPISODE).build();
    }

    @Override
    public Object delete(Long episodeId) {
        Episode episode = findById(episodeId);

        episodeRepository.delete(episode);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_EPISODE).build();
    }

    @Override
    public Object delete(List<Long> episodeIds) {
        List<Episode> episodes = episodeRepository.findAllById(episodeIds);

        episodeRepository.deleteAll(episodes);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_EPISODES).build();
    }
}

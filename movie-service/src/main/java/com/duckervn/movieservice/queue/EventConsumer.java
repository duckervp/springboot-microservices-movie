package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.common.Constants;
import com.duckervn.movieservice.common.TypeRef;
import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.repository.EpisodeRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.service.IFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final ObjectMapper objectMapper;

    private final IFileService fileService;

    private final ServiceConfig serviceConfig;

    private final MovieRepository movieRepository;

    private final EpisodeRepository episodeRepository;

    @SneakyThrows
    @KafkaListener(topics = "${topic.movie}")
    @SendTo
    public String consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        Map<String, Object> resultMap = new HashMap<>();

        if (Objects.nonNull(event)) {
            resultMap.put(Constants.EVENT_ATTR, event);
            Map<String, Object> data = objectMapper.convertValue(requestMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
            if (event.equals(serviceConfig.getFindMovieStoredFileEvent())) {
                resultMap.put(Constants.DATA_ATTR, fileService.getAllStoredFiles());
            } else if (event.equals(serviceConfig.getCheckMovieExistEvent())) {
                Long movieId = null;
                boolean exist = false;
                if (data.containsKey("movieId") && Objects.nonNull(data.get("movieId"))) {
                    movieId = ((Integer) data.get("movieId")).longValue();
                }
                if (Objects.nonNull(movieId)) {
                    exist = movieRepository.existsById(movieId);
                }
                Map<String, Object> response = new HashMap<>();
                response.put("movieId", movieId);
                response.put("exist", exist);
                resultMap.put(Constants.DATA_ATTR, response);
            } else if (event.equals(serviceConfig.getUpdateMovieRatingEvent())) {
                if (data.containsKey("movieId") && Objects.nonNull(data.get("ratingPoint"))) {
                    Long movieId = ((Integer) data.get("movieId")).longValue();
                    Double ratingPoint = ((Float) data.get("ratingPoint")).doubleValue();
                    Movie movie = movieRepository.findById(movieId).orElse(null);
                    if (Objects.nonNull(movie)) {
                        movie.setRating(ratingPoint);
                        movie.setModifiedAt(LocalDateTime.now());
                        movieRepository.save(movie);
                    }
                }
            } else if (event.equals(serviceConfig.getUpdateEpisodeViewEvent())) {
                if (Objects.nonNull(data.get("episodeId"))) {
                    Long episodeId = ((Integer) data.get("episodeId")).longValue();
                    Episode episode = episodeRepository.findById(episodeId).orElse(null);
                    if (Objects.nonNull(episode)) {
                        Long view = Objects.nonNull(episode.getView()) ? episode.getView() + 1L : 1L;
                        episode.setView(view);
                        episode.setModifiedAt(LocalDateTime.now());
                        episodeRepository.save(episode);
                    }
                }
            }
        }

        System.out.println("RS back: " + objectMapper.writeValueAsString(resultMap));
        return objectMapper.writeValueAsString(resultMap);
    }
}

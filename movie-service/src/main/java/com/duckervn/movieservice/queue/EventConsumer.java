package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.common.Constants;
import com.duckervn.movieservice.common.TypeRef;
import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.repository.EpisodeRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final ObjectMapper objectMapper;

    private final ServiceConfig serviceConfig;

    private final MovieRepository movieRepository;

    private final EpisodeRepository episodeRepository;

    @SneakyThrows
    @KafkaListener(topics = "${topic.movie}")
    public void consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        if (Objects.nonNull(event)) {
            Map<String, Object> data = objectMapper.convertValue(requestMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
            if (event.equals(serviceConfig.getUpdateMovieRatingEvent())) {
                if (data.containsKey("movieId") && Objects.nonNull(data.get("ratingPoint"))) {
                    Long movieId = ((Integer) data.get("movieId")).longValue();
                    Double ratingPoint = (Double) data.get("ratingPoint");
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
    }
}

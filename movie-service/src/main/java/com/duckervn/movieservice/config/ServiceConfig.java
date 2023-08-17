package com.duckervn.movieservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {

    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${event.movie.updateRating}")
    private String updateMovieRatingEvent;

    @Value("${event.movie.updateEpisodeView}")
    private String updateEpisodeViewEvent;
}

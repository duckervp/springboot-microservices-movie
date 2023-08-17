package com.duckervn.activityservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${topic.movie}")
    private String movieTopic;

    @Value("${topic.user}")
    private String userTopic;

    @Value("${event.movie.updateRating}")
    private String updateMovieRatingEvent;

    @Value("${event.movie.updateEpisodeView}")
    private String updateEpisodeViewEvent;

    @Value("${event.user.updateExp}")
    private String updateUserExpEvent;

}

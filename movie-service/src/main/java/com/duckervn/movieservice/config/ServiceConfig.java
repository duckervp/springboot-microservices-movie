package com.duckervn.movieservice.config;

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

    @Value("${topic.reply.movieToStream}")
    private String movieToStreamReplyTopic;

    @Value("${topic.reply.movieToActivity}")
    private String movieToActivityReplyTopic;

    @Value("${event.movie.findStoredFile}")
    private String findMovieStoredFileEvent;

    @Value("${event.movie.exist}")
    private String checkMovieExistEvent;
}

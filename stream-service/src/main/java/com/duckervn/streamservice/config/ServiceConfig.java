package com.duckervn.streamservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${root.location}")
    private String rootLocation;

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${topic.movie}")
    private String movieTopic;

    @Value("${topic.user}")
    private String userTopic;

    @Value("${topic.reply.userToStream}")
    private String userToStreamReplyTopic;

    @Value("${topic.reply.movieToStream}")
    private String movieToStreamReplyTopic;

    @Value("${event.movie.findStoredFile}")
    private String findMovieStoredFileEvent;

    @Value("${event.user.findStoredFile}")
    private String findUserStoredFileEvent;
}

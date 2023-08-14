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

    @Value("${topic.reply.userToActivity}")
    private String userToActivityReplyTopic;

    @Value("${topic.reply.movieToActivity}")
    private String movieToActivityReplyTopic;

    @Value("${event.movie.update}")
    private String updateMovieRating;

    @Value("${event.user.updateExp}")
    private String updateUserExpEvent;

    @Value("${event.user.exist}")
    private String checkUserExistEvent;

    @Value("${event.movie.exist}")
    private String checkMovieExistEvent;

}

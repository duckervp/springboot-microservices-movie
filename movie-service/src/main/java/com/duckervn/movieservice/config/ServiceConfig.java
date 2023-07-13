package com.duckervn.movieservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {

    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${topic.movie.request}")
    private String movieRequestTopic;

    @Value("${topic.movie.response}")
    private String movieResponseTopic;

}

package com.duckervn.movieservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {

    @Value("${queue.exchange}")
    private String exchange;

    @Value("${queue.movieQueue.name}")
    private String movieQueue;

    @Value("${queue.movieQueue.routingKey.all}")
    private String movieRoutingKey;

    @Value("${queue.movieQueue.routingKey.storedFile}")
    private String findMovieStoredFileRK;

}

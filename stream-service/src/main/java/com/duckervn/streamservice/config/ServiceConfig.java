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

    @Value("${queue.exchange}")
    private String exchange;

    @Value("${queue.movieQueue.name}")
    private String movieQueue;

    @Value("${queue.movieQueue.routingKey.all}")
    private String movieRoutingKey;

    @Value("${queue.movieQueue.routingKey.storedFile}")
    private String findMovieStoredFileRK;

    @Value("${queue.userQueue.name}")
    private String userQueue;

    @Value("${queue.userQueue.routingKey.all}")
    private String userRoutingKey;

    @Value("${queue.userQueue.routingKey.storedFile}")
    private String findUserStoredFileRK;
}

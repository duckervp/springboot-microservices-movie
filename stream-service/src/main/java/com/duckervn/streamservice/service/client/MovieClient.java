package com.duckervn.streamservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "movie-service", url = "${MOVIE_SERVICE_URL}")
public interface MovieClient {
    @GetMapping("/movies/stored-files")
    List<String> getAllStoredFiles();
}

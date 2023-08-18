package com.duckervn.activityservice.service.client;

import com.duckervn.activityservice.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "movie-service", url = "${MOVIE_SERVICE_URL}")
public interface MovieClient {
    @GetMapping("/movies/{id}")
    Response findMovieById(@PathVariable Long id);

    @GetMapping("/movies/ids")
    Response findByIds(@RequestParam List<Long> values);
}

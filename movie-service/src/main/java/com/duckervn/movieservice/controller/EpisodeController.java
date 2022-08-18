package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.model.addepisode.EpisodeInput;
import com.duckervn.movieservice.domain.model.findEpisode.EpisodeOutput;
import com.duckervn.movieservice.service.IEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episode")
@RequiredArgsConstructor
public class EpisodeController {
    private final IEpisodeService episodeService;

    @PostMapping
    public void save(@RequestBody EpisodeInput episodeInput) {
        episodeService.save(episodeInput);
    }

    @GetMapping("/{id}")
    public EpisodeOutput findEpisodeOutputById(@PathVariable Long id) {
        return episodeService.findEpisodeOutputById(id);
    }
}

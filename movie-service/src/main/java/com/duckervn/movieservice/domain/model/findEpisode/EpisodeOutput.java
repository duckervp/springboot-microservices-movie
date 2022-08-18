package com.duckervn.movieservice.domain.model.findEpisode;

import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeOutput {
    private Long id;
    private String name;
    private Long duration;
    private String url;
    private String description;
    private MovieDTO movie;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

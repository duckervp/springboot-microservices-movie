package com.duckervn.movieservice.domain.model.addepisode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeInput {
    private Long id;

    private String name;

    private Long duration;

    private String url;

    private Long view;

    private String description;

    private Long movieId;
}

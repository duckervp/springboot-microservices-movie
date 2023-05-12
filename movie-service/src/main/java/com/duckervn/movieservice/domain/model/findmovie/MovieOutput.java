package com.duckervn.movieservice.domain.model.findmovie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieOutput {
    private Long id;

    private String name;

    private Integer releaseYear;

    private Integer totalEpisode;

    private String country;

    private String bannerUrl;

    private String posterUrl;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String slug;
}

package com.duckervn.movieservice.domain.dto;

public interface EpisodeDTO {
    Long getId();

    String getName();

    Long getDuration();

    String getUrl();

    String getDescription();

    Long getMovieId();
}

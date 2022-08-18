package com.duckervn.movieservice.domain.dto;

import java.time.LocalDateTime;

public interface MovieDTO {
    Long getId();
    String getName();
    Integer getReleaseYear();
    Integer getTotalEpisode();
    String getCountry();
    String getBannerUrl();
    String getPosterUrl();
    String getDescription();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();
}

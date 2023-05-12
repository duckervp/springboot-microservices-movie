package com.duckervn.movieservice.domain.model.addmovie;

import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.entity.Episode;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.entity.Producer;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieInput {
    private Long id;

    private String name;

    private Integer releaseYear;

    private Integer totalEpisode;

    private String country;

    private String bannerUrl;

    private String posterUrl;

    private String description;

    private Set<Episode> episodes;

    private Set<Genre> genres;

    private Set<Character> characters;

    private Producer producer;

    private String slug;
}

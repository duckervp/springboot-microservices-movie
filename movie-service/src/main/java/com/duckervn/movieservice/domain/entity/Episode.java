package com.duckervn.movieservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "episode")
public class Episode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long duration;

    private Long view;

    private String url;

    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public void addMovie(Movie movie) {
        this.setMovie(movie);
        movie.getEpisodes().add(this);
    }

    public void removeMovie(Movie movie) {
        this.setMovie(null);
        movie.getEpisodes().remove(this);
    }
}

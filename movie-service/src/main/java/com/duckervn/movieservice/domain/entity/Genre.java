package com.duckervn.movieservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public void addMovie(Movie movie) {
        if (Objects.nonNull(movie.getId()) &&
                !movies.stream()
                        .map(Movie::getId)
                        .collect(Collectors.toList())
                        .contains(movie.getId())) {
            movies.add(movie);
            movie.getGenres().add(this);
        }
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.getGenres().remove(this);
    }
}

package com.duckervn.movieservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "producer")
public class Producer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "producer", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Set<Movie> movies;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public void addMovie(Movie movie) {
        if (Objects.nonNull(movie.getId()) &&
                !movies.stream()
                        .map(Movie::getId)
                        .collect(Collectors.toList())
                        .contains(movie.getId())) {
            movies.add(movie);
            movie.setProducer(this);
        }
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
        movie.setProducer(null);
    }
}

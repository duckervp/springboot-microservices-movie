package com.duckervn.movieservice.domain.entity;

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
@Table(name = "movie", indexes = @Index(columnList = "slug"))
@NamedEntityGraph(
        name = Movie.FULL_MOVIE_GRAPH,
        attributeNodes = {
                @NamedAttributeNode("episodes"),
                @NamedAttributeNode("genres"),
                @NamedAttributeNode("characters"),
                @NamedAttributeNode("producer")
        }
)
public class Movie {
    public static final String FULL_MOVIE_GRAPH = "full-movie-graph";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer releaseYear;

    private Integer totalEpisode;

    private String country;

    private String bannerUrl;

    private String posterUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true)
    private String slug;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Set<Episode> episodes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_character",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Character> characters = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public void addEpisode(Episode episode) {
        if (Objects.nonNull(episode.getId()) &&
                !episodes.stream()
                        .map(Episode::getId)
                        .collect(Collectors.toList())
                        .contains(episode.getId())) {
            episodes.add(episode);
            episode.setMovie(this);
        }
    }

    public void removeEpisode(Episode episode) {
        episodes.remove(episode);
        episode.setMovie(null);
    }

    public void addCharacter(Character character) {
        if (Objects.nonNull(character.getId()) &&
                !characters.stream()
                        .map(Character::getId)
                        .collect(Collectors.toList())
                        .contains(character.getId())) {
            characters.add(character);
            character.getMovies().add(this);
        }
    }

    public void removeCharacter(Character character) {
        characters.remove(character);
        character.getMovies().remove(this);
    }

    public void addGenre(Genre genre) {
        if (Objects.nonNull(genre.getId()) &&
                !genres.stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList())
                        .contains(genre.getId())) {
            genres.add(genre);
            genre.getMovies().add(this);
        }
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getMovies().remove(this);
    }

    public void addProducer(Producer producer) {
        this.setProducer(producer);
        producer.getMovies().add(this);
    }

    public void removeProducer(Producer producer) {
        this.setProducer(null);
        producer.getMovies().remove(this);
    }


}

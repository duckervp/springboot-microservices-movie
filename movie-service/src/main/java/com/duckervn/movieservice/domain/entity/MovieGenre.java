package com.duckervn.movieservice.domain.entity;

import com.duckervn.movieservice.domain.bean.MovieGenreId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "movie_genre")
@IdClass(MovieGenreId.class)
public class MovieGenre {
    @Id
    @Column(name = "movie_id")
    private Long movieId;
    @Id
    @Column(name = "genre_id")
    private Long genreId;

}

package com.duckervn.movieservice.domain.entity;

import com.duckervn.movieservice.domain.bean.MovieCharacterId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "movie_character")
@IdClass(MovieCharacterId.class)
public class MovieCharacter implements Serializable {
    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Id
    @Column(name = "character_id")
    private Long characterId;

}

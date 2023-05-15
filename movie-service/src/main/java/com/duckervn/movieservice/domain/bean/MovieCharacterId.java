package com.duckervn.movieservice.domain.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieCharacterId implements Serializable {

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "character_id")
    private Long characterId;
}

package com.duckervn.movieservice.domain.model.addcharacter;

import com.duckervn.movieservice.domain.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterInput {
    private Long id;

    private String name;

    private String avatarUrl;

    private String description;

    private List<Long> movieIds;
}

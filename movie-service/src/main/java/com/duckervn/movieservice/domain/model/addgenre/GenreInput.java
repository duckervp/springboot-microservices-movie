package com.duckervn.movieservice.domain.model.addgenre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreInput {
    private Long id;

    private String name;

    private String description;
}

package com.duckervn.movieservice.domain.model.addproducer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerInput {
    private Long id;

    private String name;

    private String description;
}

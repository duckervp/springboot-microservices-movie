package com.duckervn.activityservice.domain.model.addrating;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingInput {
    private Long movieId;

    private String userId;

    private Integer point;
}

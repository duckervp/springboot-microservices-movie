package com.duckervn.movieservice.domain.dto;

import java.time.LocalDateTime;

public interface ProducerDTO {
    Long getId();
    String getName();
    String getDescription();
    LocalDateTime getCreatedAt();
    LocalDateTime getModifiedAt();
}

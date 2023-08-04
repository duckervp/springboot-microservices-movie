package com.duckervn.activityservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "history")
@JsonIgnoreProperties(ignoreUnknown = true)
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private String userId;

    private Long episodeId;

    private LocalDateTime createdAt;

}

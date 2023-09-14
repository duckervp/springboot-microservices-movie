package com.duckervn.cronservice.domain.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Table
@Entity
public class Task {
    @Id
    private String id;

    private String name;

    private String cronExpression;

    private String method;

    private String url;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}

package com.duckervn.campaignservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "provider")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String status;

    private String type;

    private String sendMethod;

    private String hostname;

    private Integer port;

    private String username;

    private String password;

    private String apiDomainName;

    private String apiKey;

    private String sender;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}

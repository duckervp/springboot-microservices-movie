package com.duckervn.authservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(name = "client")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
    @Id
    private String id;

    @Column(unique = true)

    private String clientId;

    private Instant clientIdIssuedAt;

    private String clientSecret;

    private Instant clientSecretExpiresAt;

    private String clientName;

    @Column(length = 1000)
    private String clientAuthenticationMethods;

    @Column(length = 1000)
    private String authorizationGrantTypes;

    @Column(length = 1000)
    private String redirectUris;

    @Column(length = 1000)
    private String scopes;

    @Column(length = 2000)
    private String clientSettings;

    @Column(length = 2000)
    private String tokenSettings;
}

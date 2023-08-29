package com.duckervn.authservice.domain.model.login;

import lombok.Data;

@Data
public class CredentialInput {
    private String clientId;

    private String clientSecret;
}

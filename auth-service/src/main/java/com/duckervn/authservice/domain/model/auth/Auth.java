package com.duckervn.authservice.domain.model.auth;

import lombok.Data;

@Data
public class Auth {
    private String userId;

    private boolean isAdmin;
}

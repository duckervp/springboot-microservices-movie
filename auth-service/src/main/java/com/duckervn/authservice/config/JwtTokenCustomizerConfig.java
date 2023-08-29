package com.duckervn.authservice.config;

import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import com.duckervn.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@RequiredArgsConstructor
public class JwtTokenCustomizerConfig {
    private final UserRepository userRepository;
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                RegisteredClient registeredClient = context.getRegisteredClient();
                User user = userRepository.findById(registeredClient.getId()).orElseThrow(ResourceNotFoundException::new);
                context.getClaims().claims(claims -> {
                    claims.put("id", registeredClient.getId());
                    claims.put("name", registeredClient.getClientName());
                    claims.put("avt", user.getAvatarUrl());
                });
            }
        };
    }
}

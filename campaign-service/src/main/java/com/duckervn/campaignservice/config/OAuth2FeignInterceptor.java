package com.duckervn.campaignservice.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Objects;

/**
 * This class splits authorization header when each request come to this service
 * and add that authorization header to each request made by feign client on this service to another service
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2FeignInterceptor implements RequestInterceptor {
    public static final String TOKEN_TYPE = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final ObjectMapper objectMapper;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getPrincipal())) {
            TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
            };
            Map<String, Object> principal = objectMapper.convertValue(authentication.getPrincipal(), typeReference);
            requestTemplate.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, principal.get("tokenValue")));
        }
    }
}

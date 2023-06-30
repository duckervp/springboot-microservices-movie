package com.duckervn.streamservice.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.reactive.ServerWebExchangeContextFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class AccessTokenFilter implements WebFilter {

    private static final String ACCESS_TOKEN_HEADER = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String accessToken = extractAccessToken(request);
        // You can perform additional validation or manipulation of the access token if needed

        // Set the access token in a context object or pass it as a header to subsequent requests
        AccessTokenContextHolder.setAccessToken(accessToken);

        return chain.filter(exchange)
                .doFinally(signal -> {
                    // Clean up the access token after the request is processed
                    AccessTokenContextHolder.clearAccessToken();
                });
    }

    private String extractAccessToken(ServerHttpRequest request) {
        // Extract the access token from the request headers or other sources
        List<String> authorizationHeaders = request.getHeaders().getOrDefault(ACCESS_TOKEN_HEADER, Collections.emptyList());
        if (!authorizationHeaders.isEmpty()) {
            String authorizationHeader = authorizationHeaders.get(0);
            // Perform further parsing or manipulation to extract the access token value
            // Example: "Bearer <access_token>"
            return authorizationHeader.replace("Bearer ", "");
        }
        return null; // No access token found
    }
}

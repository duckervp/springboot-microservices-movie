package com.duckervn.cronservice.service;

import com.duckervn.cronservice.common.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Value("${cron.username}")
    private String cronUsername;

    @Value("${cron.password}")
    private String cronPassword;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    public void makeRequest(HttpMethod method, String url) {
        authenticate().subscribe(responseBody -> {
            String accessToken = responseBody.get("access_token");
            if (Objects.nonNull(accessToken)) {
                sendRequest(method, url, accessToken);
            }
        });
    }

    public void sendRequest(HttpMethod method, String url, String token) {
        String bearerToken = String.format("%s %s", Constants.BEARER_TOKEN, token);
        WebClient.create()
                .method(method)
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public Mono<Map<String, String>> authenticate() {
        WebClient webClient = WebClient.create();

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", Constants.CLIENT_CREDENTIALS);
        params.put("client_id", cronUsername);
        params.put("client_secret", cronPassword);

        // Create UriComponentsBuilder
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();

        builder.uri(URI.create(String.format("%s/oauth2/token", issuerUri)));

        // Add query parameters from the map
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }

        // Build the URI with query parameters
        String uriString = builder.build().toUriString();

        ParameterizedTypeReference<Map<String, String>> typeReference =
                new ParameterizedTypeReference<>() {};

        // Make the request and send form data
        return webClient.post()
                .uri(uriString)
                .retrieve()
                .bodyToMono(typeReference);
    }

}

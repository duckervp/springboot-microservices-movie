package com.duckervn.campaignservice.config;

import com.duckervn.campaignservice.common.TypeRef;
import com.duckervn.campaignservice.service.client.UserClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class CampaignFeignConfig {

    private final UserClient auth2AuthorizationClient;

    private final ServiceConfig serviceConfig;

    private final ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Object tokenOutput = auth2AuthorizationClient.getToken(
                    "client_credentials",
                    serviceConfig.getCronUsername(),
                    serviceConfig.getCronPassword()
            );
            if (Objects.nonNull(tokenOutput)) {
                Map<String, Object> tokenOutputMap = objectMapper.convertValue(tokenOutput, TypeRef.MAP_STRING_OBJECT);
                requestTemplate.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenOutputMap.get("access_token")));
            }
        };
    }
}

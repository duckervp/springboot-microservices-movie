package com.duckervn.authservice.config;

import com.duckervn.authservice.common.Constants;
import com.duckervn.authservice.common.Credential;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.service.client.OAuth2AuthorizationClient;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class CampaignFeignConfig {

    private final OAuth2AuthorizationClient auth2AuthorizationClient;

    private final ServiceConfig serviceConfig;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            TokenOutput tokenOutput = auth2AuthorizationClient.getToken(
                    Credential.GRANT_TYPE,
                    serviceConfig.getCronUsername(),
                    serviceConfig.getCronPassword()
            );
            if (Objects.nonNull(tokenOutput)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenOutput.getAccess_token()));
            }
        };
    }
}

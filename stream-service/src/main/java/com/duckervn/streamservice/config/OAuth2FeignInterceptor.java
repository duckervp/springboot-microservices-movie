package com.duckervn.streamservice.config;

import com.duckervn.streamservice.common.Constants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

/**
 * This class splits authorization header when each request come to this service
 * and add that authorization header to each request made by feign client on this service to another service
 */
@Configuration
public class OAuth2FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken = AccessTokenContextHolder.getAccessToken();

        if (Objects.nonNull(accessToken)) {
            requestTemplate.header(
                    HttpHeaders.AUTHORIZATION,
                    String.format("%s %s", Constants.BEARER_TOKEN, accessToken)
            );
        }
    }
}

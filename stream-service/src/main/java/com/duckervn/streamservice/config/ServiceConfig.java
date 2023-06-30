package com.duckervn.streamservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${root.location}")
    private String rootLocation;

    @Value("${gateway.url}")
    private String gatewayUrl;
}

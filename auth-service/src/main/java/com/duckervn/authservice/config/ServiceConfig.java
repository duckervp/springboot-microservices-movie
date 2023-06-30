package com.duckervn.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${service.auth.hostname}")
    private String serverHostname;

    @Value("${server.port}")
    private int serverPort;

    @Value("${FRONTEND.GATEWAY}")
    private String frontendGateway;

}

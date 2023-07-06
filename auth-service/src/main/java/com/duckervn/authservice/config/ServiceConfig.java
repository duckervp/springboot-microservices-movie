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

    @Value("${frontend.gateway}")
    private String frontendGateway;

    @Value("${service.loki.url}")
    private String lokiUrl;

    @Value("${service.campaign.resetTokenCampaignId}")
    private Long resetTokenCampaignId;

    @Value("${service.cron.username}")
    private String cronUsername;

    @Value("${service.cron.password}")
    private String cronPassword;

    @Value("${rptValidityInMinute}")
    private Long rptValidityInMinute;

}

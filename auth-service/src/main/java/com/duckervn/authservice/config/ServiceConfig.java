package com.duckervn.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${provider.issuerUri}")
    private String providerIssuerUri;

    @Value("${server.port}")
    private int serverPort;

    @Value("${frontend.gateway}")
    private String frontendGateway;

    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${cron.username}")
    private String cronUsername;

    @Value("${cron.password}")
    private String cronPassword;

    @Value("${campaign.rptCampaignId}")
    private Long rptCampaignId;

    @Value("${rptValidityInMinute}")
    private Long rptValidityInMinute;

}

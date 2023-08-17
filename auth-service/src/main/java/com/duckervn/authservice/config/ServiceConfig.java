package com.duckervn.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${provider.issuerUri}")
    private String providerIssuerUri;

    @Value("${frontend.gateway}")
    private String frontendGateway;

    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${rpt.campaignId}")
    private Long rptCampaignId;

    @Value("${rpt.validityInMinute}")
    private Long rptValidityInMinute;

    @Value("${topic.campaign}")
    private String campaignTopic;

    @Value("${event.campaign.addRecipient}")
    private String addCampaignRecipientEvent;

    @Value("${event.user.updateExp}")
    private String updateUserExpEvent;

}

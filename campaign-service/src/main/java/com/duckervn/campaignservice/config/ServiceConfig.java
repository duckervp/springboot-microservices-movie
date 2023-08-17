package com.duckervn.campaignservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${cron.username}")
    private String cronUsername;

    @Value("${cron.password}")
    private String cronPassword;

    @Value("${event.campaign.addRecipient}")
    private String addCampaignRecipientEvent;

}

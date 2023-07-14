package com.duckervn.campaignservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ServiceConfig {
    @Value("${loki.url}")
    private String lokiUrl;

    @Value("${topic.campaign}")
    private String campaignTopic;

    @Value("${topic.user}")
    private String userTopic;

    @Value("${topic.reply.userToCampaign}")
    private String userToCampaignReplyTopic;

    @Value("${event.campaign.addRecipient}")
    private String addCampaignRecipientEvent;

    @Value("${event.user.find}")
    private String findUserEvent;

    @Value("${event.user.exist}")
    private String checkUserExistEvent;

}

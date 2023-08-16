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

    @Value("${rpt.campaignId}")
    private Long rptCampaignId;

    @Value("${rpt.validityInMinute}")
    private Long rptValidityInMinute;

    @Value("${topic.campaign}")
    private String campaignTopic;

    @Value("${topic.user}")
    private String userTopic;

    @Value("${topic.reply.userToCampaign}")
    private String userToCampaignReplyTopic;

    @Value("${topic.reply.userToStream}")
    private String userToStreamReplyTopic;

    @Value("${topic.reply.userToActivity}")
    private String userToActivityReplyTopic;

    @Value("${event.user.find}")
    private String findUserEvent;

    @Value("${event.user.exist}")
    private String checkUserExistEvent;

    @Value("${event.user.findStoredFile}")
    private String findUserStoredFileEvent;

    @Value("${event.campaign.addRecipient}")
    private String addCampaignRecipientEvent;

    @Value("${event.user.updateExp}")
    private String updateUserExpEvent;

}

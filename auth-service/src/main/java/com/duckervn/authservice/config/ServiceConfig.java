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

    @Value("${queue.exchange}")
    private String exchange;

    @Value("${queue.campaignQueue.name}")
    private String campaignQueue;

    @Value("${queue.campaignQueue.routingKey.all}")
    private String campaignRoutingKey;

    @Value("${queue.campaignQueue.routingKey.addRecipient}")
    private String addRecipientRK;

    @Value("${queue.userQueue.name}")
    private String userQueue;

    @Value("${queue.userQueue.routingKey.all}")
    private String userRoutingKey;

    @Value("${queue.userQueue.routingKey.checkUserExist}")
    private String checkUserExistRK;

    @Value("${queue.userQueue.routingKey.findUser}")
    private String findUserRK;

    @Value("${queue.userQueue.routingKey.storedFile}")
    private String findUserStoredFileRK;

}

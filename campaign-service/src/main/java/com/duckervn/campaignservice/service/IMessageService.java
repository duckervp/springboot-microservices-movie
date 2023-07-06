package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.entity.Provider;
import com.duckervn.campaignservice.domain.model.startcampaign.SendCampaignOutput;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface IMessageService {
    Campaign getCampaignDetail(Long campaignId);

    default String mapData(String text, Map<String, Object> dataMapping) {
        for (String key : dataMapping.keySet()) {
            String variableName = "%" + key + "%";
            if (text.contains(variableName)) {
                text = text.replaceAll(variableName, (String) dataMapping.get(key));
            }
        }
        return text;
    }

    List<CampaignRecipient> getCampaignRecipients(Long campaignId);

    Map<String, Object> getDataMapping(CampaignRecipient campaignRecipient);

    Provider getProvider(Long providerId);

    Object send(Provider provider, Map<String, Object> recipient, String subject, String body);

    SendCampaignOutput finishCampaign(Campaign campaign, CampaignRecipient recipient, Provider provider, Object sendResult);

    default Response startCampaign(Long campaignId) {
        Campaign campaign = getCampaignDetail(campaignId);
        if (Objects.nonNull(campaign) && campaign.getStatus().equals(Constants.ACTIVE)) {
            List<CampaignRecipient> campaignRecipients = getCampaignRecipients(campaignId);

            List<SendCampaignOutput> finalResults = new ArrayList<>();

            for (CampaignRecipient recipient : campaignRecipients) {
                Map<String, Object> dataMapping = getDataMapping(recipient);
                String subject = mapData(campaign.getSubject(), dataMapping);
                String body = mapData(campaign.getBody(), dataMapping);
                Provider provider = getProvider(campaign.getProviderId());
                Object sendResult;
                try {
                    sendResult = send(provider, dataMapping, subject, body);
                } catch (Exception e) {
                    sendResult = null;
                }
                finalResults.add(finishCampaign(campaign, recipient, provider, sendResult));
            }

            return Response.builder().code(HttpStatus.OK.value())
                    .message("Start Campaign successfully!")
                    .results(finalResults).build();
        }
        return Response.builder().build();
    }
}

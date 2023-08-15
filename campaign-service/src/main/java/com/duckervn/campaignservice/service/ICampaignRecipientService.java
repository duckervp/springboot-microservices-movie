package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;

import java.util.List;

public interface ICampaignRecipientService {
    List<CampaignRecipient> findAll(Long campaignId);

    CampaignRecipient findByCampaignIdAndRecipientId(Long campaignId, Long campaignRecipientId);

    CampaignRecipient save(Long campaignId, CampaignRecipientInput campaignRecipientInput);

    CampaignRecipient update(Long campaignId, Long campaignRecipientId, CampaignRecipientInput campaignRecipientInput);

    void delete(Long campaignId, Long campaignRecipientId);
}

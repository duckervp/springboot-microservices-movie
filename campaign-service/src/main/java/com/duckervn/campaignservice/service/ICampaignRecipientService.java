package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.domain.entity.CampaignRecipient;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;

import java.util.List;

public interface ICampaignRecipientService {
    List<CampaignRecipient> findAll();

    CampaignRecipient findById(Long campaignRecipientId);

    CampaignRecipient save(CampaignRecipientInput campaignRecipientInput);

    CampaignRecipient update(Long campaignRecipientId, CampaignRecipientInput campaignRecipientInput);

    void delete(Long campaignRecipientId);
}

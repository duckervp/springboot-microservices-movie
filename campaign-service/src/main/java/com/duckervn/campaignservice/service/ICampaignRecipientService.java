package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;

public interface ICampaignRecipientService {
    Response findAll();

    Response findById(Long campaignRecipientId);

    Response save(CampaignRecipientInput campaignRecipientInput);

    Response update(Long campaignRecipientId, CampaignRecipientInput campaignRecipientInput);

    Response delete(Long campaignRecipientId);
}

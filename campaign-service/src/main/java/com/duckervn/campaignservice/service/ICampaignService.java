package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;

public interface ICampaignService {
    Response findAll();

    Response findById(Long campaignId);

    Response save(CampaignInput campaignInput);

    Response update(Long campaignId, CampaignInput campaignInput);

    Response delete(Long campaignId);
}

package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;

import java.util.List;

public interface ICampaignService {
    List<Campaign> findAll();

    Campaign findById(Long campaignId);

    Campaign save(CampaignInput campaignInput);

    Campaign update(Long campaignId, CampaignInput campaignInput);

    void delete(Long campaignId);
}

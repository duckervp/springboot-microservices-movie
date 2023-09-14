package com.duckervn.campaignservice.service;

import com.duckervn.campaignservice.domain.entity.Campaign;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;
import com.duckervn.campaignservice.domain.model.getcampaign.CampaignOutput;
import com.duckervn.campaignservice.domain.model.page.PageOutput;

import java.util.List;

public interface ICampaignService {
    PageOutput<CampaignOutput> findAllCampaignOutput(Integer pageNo, Integer pageSize);

    Campaign findById(Long campaignId);

    Campaign save(CampaignInput campaignInput);

    Campaign update(Long campaignId, CampaignInput campaignInput);

    void delete(Long campaignId);

    void delete(List<Long> campaignIds);
}

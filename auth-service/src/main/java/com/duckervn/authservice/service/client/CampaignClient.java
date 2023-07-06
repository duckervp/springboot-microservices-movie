package com.duckervn.authservice.service.client;

import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.config.CampaignFeignConfig;
import com.duckervn.authservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "campaign-service", url = "${service.campaign.hostname}:${service.campaign.port}", configuration = CampaignFeignConfig.class)
public interface CampaignClient {
    @PostMapping("/campaigns/recipients")
    Response addCampaignRecipient(@RequestBody CampaignRecipientInput input);
}

package com.duckervn.campaignservice.service.client;

import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.config.CampaignFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service2", url = "${AUTH_SERVICE_URL}", configuration = CampaignFeignConfig.class)
public interface SpecialUserClient {
    @GetMapping("/users/{userId}")
    Response findUserById(@PathVariable String userId);
}

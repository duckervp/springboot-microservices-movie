package com.duckervn.campaignservice.service.client;

import com.duckervn.campaignservice.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${service.auth.hostname}:${service.auth.port}")
public interface UserClient {
    @GetMapping("/users/{userId}")
    Response findUserById(@PathVariable String userId);
}

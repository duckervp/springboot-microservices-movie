package com.duckervn.campaignservice.service.client;

import com.duckervn.campaignservice.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${AUTH_SERVICE_URL}")
public interface UserClient {
    @PostMapping("/oauth2/token")
    Object getToken(@RequestParam String grant_type,
                         @RequestParam String client_id,
                         @RequestParam String client_secret);

    @GetMapping("/users/{userId}")
    Response findUserById(@PathVariable String userId);
}

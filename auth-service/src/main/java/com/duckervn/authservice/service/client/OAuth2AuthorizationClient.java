package com.duckervn.authservice.service.client;

import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "${service.auth.hostname}:${server.port}")
public interface OAuth2AuthorizationClient {
    @PostMapping("/oauth2/token")
    TokenOutput getToken(@RequestParam String grant_type,
            @RequestParam String client_id,
            @RequestParam String client_secret);
}

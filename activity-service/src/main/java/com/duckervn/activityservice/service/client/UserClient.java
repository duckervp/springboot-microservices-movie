package com.duckervn.activityservice.service.client;

import com.duckervn.activityservice.common.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${AUTH_SERVICE_URL}")
public interface UserClient {
    @GetMapping("/users/{userId}")
    Response findUserById(@PathVariable String userId);
}

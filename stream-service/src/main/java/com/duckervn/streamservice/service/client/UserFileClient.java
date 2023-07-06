package com.duckervn.streamservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "user-service", url = "${AUTH_SERVICE_URL}")
public interface UserFileClient {
    @GetMapping("/users/stored-files")
    List<String> getAllStoredFiles();
}

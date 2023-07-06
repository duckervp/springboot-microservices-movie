package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
public class MessageController {
    private final IMessageService messageService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/startCampaign")
    public ResponseEntity<?> startCampaign(@RequestParam Long campaignId) {
        return ResponseEntity.ok(messageService.startCampaign(campaignId));
    }

}

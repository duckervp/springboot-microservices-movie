package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.service.ICampaignRecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns/recipients")
public class CampaignRecipientController {
    private final ICampaignRecipientService campaignRecipientService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(campaignRecipientService.findAll());
    }

    @GetMapping("/{campaignRecipientId}")
    public ResponseEntity<?> findCampaignRecipient(@PathVariable Long campaignRecipientId) {
        return ResponseEntity.ok(campaignRecipientService.findById(campaignRecipientId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCampaignRecipient(@RequestBody @Valid CampaignRecipientInput input) {
        return ResponseEntity.ok(campaignRecipientService.save(input));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{campaignRecipientId}")
    public ResponseEntity<?> updateCampaignRecipient(@PathVariable Long campaignRecipientId, @RequestBody CampaignRecipientInput input) {
        return ResponseEntity.ok(campaignRecipientService.update(campaignRecipientId, input));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{campaignRecipientId}")
    public ResponseEntity<?> deleteCampaignRecipient(@PathVariable Long campaignRecipientId) {
        return ResponseEntity.ok(campaignRecipientService.delete(campaignRecipientId));
    }

}

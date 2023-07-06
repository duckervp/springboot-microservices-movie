package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;
import com.duckervn.campaignservice.service.ICampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
public class CampaignController {
    private final ICampaignService campaignService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(campaignService.findAll());
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{campaignId}")
    public ResponseEntity<?> findCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.findById(campaignId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCampaign(@RequestBody @Valid CampaignInput input) {
        return ResponseEntity.ok(campaignService.save(input));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{campaignId}")
    public ResponseEntity<?> updateCampaign(@PathVariable Long campaignId, @RequestBody CampaignInput input) {
        return ResponseEntity.ok(campaignService.update(campaignId, input));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{campaignId}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignService.delete(campaignId));
    }

}

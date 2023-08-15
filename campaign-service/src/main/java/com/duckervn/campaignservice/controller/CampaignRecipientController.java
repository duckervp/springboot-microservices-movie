package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.service.ICampaignRecipientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns/{campaignId}/recipients")
public class CampaignRecipientController {
    private final ICampaignRecipientService campaignRecipientService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll(@PathVariable Long campaignId) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN_RECIPIENTS)
                .results(campaignRecipientService.findAll(campaignId)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{campaignRecipientId}")
    public ResponseEntity<?> findCampaignRecipient(@PathVariable Long campaignId, @PathVariable Long campaignRecipientId) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN_RECIPIENT)
                .result(campaignRecipientService.findByCampaignIdAndRecipientId(campaignId, campaignRecipientId))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCampaignRecipient(@PathVariable Long campaignId, @RequestBody @Valid CampaignRecipientInput input) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_CAMPAIGN_RECIPIENT)
                .result(campaignRecipientService.save(campaignId, input)).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{campaignRecipientId}")
    public ResponseEntity<?> updateCampaignRecipient(@PathVariable Long campaignId, @PathVariable Long campaignRecipientId, @RequestBody CampaignRecipientInput input) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .result(campaignRecipientService.update(campaignId, campaignRecipientId, input))
                .message(RespMessage.UPDATED_CAMPAIGN_RECIPIENT).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{campaignRecipientId}")
    public ResponseEntity<?> deleteCampaignRecipient(@PathVariable Long campaignId, @PathVariable Long campaignRecipientId) {
        campaignRecipientService.delete(campaignId, campaignRecipientId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CAMPAIGN_RECIPIENT).build();
        return ResponseEntity.ok(response);
    }

}

package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addcampaign.CampaignInput;
import com.duckervn.campaignservice.domain.model.page.PageOutput;
import com.duckervn.campaignservice.service.ICampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
public class CampaignController {
    private final ICampaignService campaignService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        PageOutput<?> pageOutput = campaignService.findAllCampaignOutput(pageNo, pageSize);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_CAMPAIGNS)
                .results(pageOutput.getContent())
                .pageSize(pageOutput.getPageSize())
                .totalElements(pageOutput.getTotalElements())
                .pageNo(pageOutput.getPageNo()).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{campaignId}")
    public ResponseEntity<?> findCampaign(@PathVariable Long campaignId) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CAMPAIGN)
                .result(campaignService.findById(campaignId))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCampaign(@RequestBody @Valid CampaignInput input) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_CAMPAIGN)
                .result(campaignService.save(input)).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{campaignId}")
    public ResponseEntity<?> updateCampaign(@PathVariable Long campaignId, @RequestBody CampaignInput input) {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_CAMPAIGN)
                .result(campaignService.update(campaignId, input)).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{campaignId}")
    public ResponseEntity<?> deleteCampaign(@PathVariable Long campaignId) {
        campaignService.delete(campaignId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CAMPAIGN).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteCampaigns(@RequestParam List<Long> campaignIds) {
        campaignService.delete(campaignIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_CAMPAIGNS).build();
        return ResponseEntity.ok(response);
    }
}

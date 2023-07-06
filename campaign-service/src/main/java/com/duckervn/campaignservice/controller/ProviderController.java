package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.domain.model.addprovider.ProviderInput;
import com.duckervn.campaignservice.service.IProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/providers")
public class ProviderController {
    private final IProviderService providerService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(providerService.findAll());
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{providerId}")
    public ResponseEntity<?> findProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerService.findById(providerId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProvider(@RequestBody @Valid ProviderInput input) {
        return ResponseEntity.ok(providerService.save(input));
    }

    @PatchMapping("/{providerId}")
    public ResponseEntity<?> updateProvider(@PathVariable Long providerId, @RequestBody @Valid ProviderInput input) {
        return ResponseEntity.ok(providerService.update(providerId, input));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{providerId}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerService.delete(providerId));
    }

}

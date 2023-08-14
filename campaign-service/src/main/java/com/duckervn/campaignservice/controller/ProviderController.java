package com.duckervn.campaignservice.controller;

import com.duckervn.campaignservice.common.RespMessage;
import com.duckervn.campaignservice.common.Response;
import com.duckervn.campaignservice.domain.model.addprovider.ProviderInput;
import com.duckervn.campaignservice.service.IProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_PROVIDERS)
                .results(providerService.findAll()).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{providerId}")
    public ResponseEntity<?> findProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(providerService.findById(providerId));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createProvider(@RequestBody @Valid ProviderInput input) {
        Response response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_PROVIDER)
                .result(providerService.save(input))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{providerId}")
    public ResponseEntity<?> updateProvider(@PathVariable Long providerId, @RequestBody @Valid ProviderInput input) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_PROVIDER)
                .result(providerService.update(providerId, input)).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{providerId}")
    public ResponseEntity<?> deleteProvider(@PathVariable Long providerId) {
        providerService.delete(providerId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_PROVIDER).build();
        return ResponseEntity.ok(response);
    }

}

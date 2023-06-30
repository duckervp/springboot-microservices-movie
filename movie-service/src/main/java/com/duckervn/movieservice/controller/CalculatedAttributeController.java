package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.service.ICalculatedAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("movies/calculated-attributes")
public class CalculatedAttributeController {
    private final ICalculatedAttributeService calculatedAttributeService;
    @GetMapping
    public ResponseEntity<Response> getAdminDashboardCalculatedAttributes() {
        return ResponseEntity.ok(calculatedAttributeService.getAdminDashboardCalculatedAttributes());
    }
}

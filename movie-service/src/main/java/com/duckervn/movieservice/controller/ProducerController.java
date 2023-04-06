package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.service.IProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer")
@RequiredArgsConstructor
public class ProducerController {
    private final IProducerService producerService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProducerInput producerInput) {
        return new ResponseEntity<>(producerService.save(producerInput), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(producerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.findById(id));
    }
}

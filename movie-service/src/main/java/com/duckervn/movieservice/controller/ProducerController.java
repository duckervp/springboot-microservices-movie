package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.service.IProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/producers")
@RequiredArgsConstructor
public class ProducerController {
    private final IProducerService producerService;

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProducerInput producerInput) {
        return new ResponseEntity<>(producerService.save(producerInput), HttpStatus.CREATED);
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{producerId}")
    public ResponseEntity<?> update(@PathVariable Long producerId, @RequestBody @Valid ProducerInput producerInput) {
        return ResponseEntity.ok(producerService.update(producerId, producerInput));
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{producerId}")
    public ResponseEntity<?> delete(@PathVariable Long producerId) {
        return ResponseEntity.ok(producerService.delete(producerId));
    }

//    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> producerIds) {
        return ResponseEntity.ok(producerService.delete(producerIds));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(producerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(producerService.findProducer(id));
    }
}

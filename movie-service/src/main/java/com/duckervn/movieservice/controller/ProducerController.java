package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
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

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProducerInput producerInput) {
        Response response = Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_PRODUCER)
                .result(producerService.save(producerInput))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{producerId}")
    public ResponseEntity<?> update(@PathVariable Long producerId, @RequestBody @Valid ProducerInput producerInput) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_PRODUCER)
                .result(producerService.update(producerId, producerInput))
                .build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{producerId}")
    public ResponseEntity<?> delete(@PathVariable Long producerId) {
        producerService.delete(producerId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_PRODUCER).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam List<Long> producerIds) {
        producerService.delete(producerIds);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_PRODUCERS).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll() {
        Response response = Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_ALL_PRODUCERS)
                .results(producerService.findAll()).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_PRODUCER)
                .result(producerService.findById(id)).build();
        return ResponseEntity.ok(response);
    }
}

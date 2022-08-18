package com.duckervn.movieservice.controller;

import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.service.IProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producer")
@RequiredArgsConstructor
public class ProducerController {
    private final IProducerService producerService;

    @PostMapping
    public void save(@RequestBody ProducerInput producerInput) {
        producerService.save(producerInput);
    }

    @GetMapping
    public List<Producer> findAll() {
        return producerService.findAll();
    }

    @GetMapping("/{id}")
    public Producer findById(@PathVariable Long id) {
        return producerService.findById(id);
    }
}

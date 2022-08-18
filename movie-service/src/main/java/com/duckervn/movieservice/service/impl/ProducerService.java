package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.dto.ProducerDTO;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.repository.ProducerRepository;
import com.duckervn.movieservice.service.IProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService implements IProducerService {
    private final ProducerRepository producerRepository;

    private final ObjectMapper objectMapper;

    /**
     * @param id id
     * @return producer
     */
    @Override
    public Producer findById(Long id) {
        return producerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producer not found with id " + id));
    }

    /**
     * @param producerInput product input
     */
    @Override
    public void save(ProducerInput producerInput) {
        Producer producer = objectMapper.convertValue(producerInput, Producer.class);
        producer.setCreatedAt(LocalDateTime.now());
        producerRepository.save(producer);
    }

    /**
     * @param id id
     * @return producer DTO
     */
    @Override
    public ProducerDTO findProductDTOById(Long id) {
        return producerRepository.findProducerDTOById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductDTO not found with id" + id));
    }

    /**
     * @return list producer
     */
    @Override
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }
}

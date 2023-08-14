package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.repository.ProducerRepository;
import com.duckervn.movieservice.service.IProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProducerService implements IProducerService {
    private final ProducerRepository producerRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Producer findById(Long id) {
        return producerRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @param producerInput product input
     * @return Response
     */
    @Override
    public Producer save(ProducerInput producerInput) {
        Producer producer = objectMapper.convertValue(producerInput, Producer.class);
        return save(producer);
    }

    /**
     * @param producer product
     * @return Response
     */
    @Override
    public Producer save(Producer producer) {
        producer.setCreatedAt(LocalDateTime.now());
        producer.setModifiedAt(LocalDateTime.now());
        producerRepository.save(producer);
        return producer;
    }

    /**
     * @return list producer
     */
    @Override
    public List<Producer> findAll() {
        return producerRepository.findAll();
    }

    @Override
    public Producer update(Long producerId, ProducerInput producerInput) {
        Producer producer = findById(producerId);

        if (Objects.nonNull(producerInput.getName())) {
            producer.setName(producerInput.getName());
        }

        if (Objects.nonNull(producerInput.getDescription())) {
            producer.setDescription(producerInput.getDescription());
        }

        producer.setModifiedAt(LocalDateTime.now());

        producerRepository.save(producer);

        return producer;
    }

    @Override
    public void delete(Long producerId) {
        Producer producer = findById(producerId);

        producerRepository.delete(producer);
    }

    @Override
    public void delete(List<Long> producerIds) {
        List<Producer> producers = producerRepository.findByIds(producerIds);

        producerRepository.deleteAll(producers);
    }
}

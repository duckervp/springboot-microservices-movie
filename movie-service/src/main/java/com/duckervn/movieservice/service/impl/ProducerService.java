package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.dto.ProducerDTO;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;
import com.duckervn.movieservice.repository.ProducerRepository;
import com.duckervn.movieservice.service.IProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public Response findProducer(Long id) {
        Producer producer = findById(id);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_PRODUCER)
                .result(producer).build();
    }

    @Override
    public Producer findById(Long id) {
        return producerRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @param producerInput product input
     * @return Response
     */
    @Override
    public Response save(ProducerInput producerInput) {
        Producer producer = objectMapper.convertValue(producerInput, Producer.class);
        return save(producer);
    }

    /**
     * @param producer product
     * @return Response
     */
    @Override
    public Response save(Producer producer) {
        producer.setCreatedAt(LocalDateTime.now());
        producer.setModifiedAt(LocalDateTime.now());
        producerRepository.save(producer);
        return Response.builder().code(HttpStatus.CREATED.value()).message(RespMessage.CREATED_PRODUCER).build();
    }

    /**
     * @param id id
     * @return producer DTO
     */
    @Override
    public Response findProductDTOById(Long id) {
        ProducerDTO producerDTO = producerRepository.findProducerDTOById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_PRODUCER)
                .result(producerDTO).build();
    }

    /**
     * @return list producer
     */
    @Override
    public Response findAll() {
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_PRODUCERS)
                .results(Utils.toObjectList(producerRepository.findAll())).build();
    }

    @Override
    public Response update(Long producerId, ProducerInput producerInput) {
        Producer producer = findById(producerId);

        if (Objects.nonNull(producerInput.getName())) {
            producer.setName(producerInput.getName());
        }

        if (Objects.nonNull(producerInput.getDescription())) {
            producer.setDescription(producerInput.getDescription());
        }

        producer.setModifiedAt(LocalDateTime.now());

        producerRepository.save(producer);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.UPDATED_PRODUCER).build();
    }

    @Override
    public Response delete(Long producerId) {
        Producer producer = findById(producerId);

        producerRepository.delete(producer);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_PRODUCER).build();
    }

    @Override
    public Response delete(List<Long> producerIds) {
        List<Producer> producers = producerRepository.findByIds(producerIds);

        producerRepository.deleteAll(producers);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_PRODUCERS).build();
    }
}

package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.dto.ProducerDTO;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;

import java.util.List;

public interface IProducerService {
    Producer findById(Long id);

    void save(ProducerInput producerInput);

    ProducerDTO findProductDTOById(Long id);

    List<Producer> findAll();
}

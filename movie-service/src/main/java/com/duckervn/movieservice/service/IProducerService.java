package com.duckervn.movieservice.service;

import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;

import java.util.List;

public interface IProducerService {

    Producer findById(Long id);

    Producer save(ProducerInput producerInput);

    Producer save(Producer producer);

    List<Producer> findAll();

    Producer update(Long producerId, ProducerInput producerInput);

    void delete(Long producerId);

    void delete(List<Long> producerIds);
}

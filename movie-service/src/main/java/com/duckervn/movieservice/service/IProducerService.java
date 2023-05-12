package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;

import java.util.List;

public interface IProducerService {
    Response findProducer(Long id);

    Producer findById(Long id);

    Response save(ProducerInput producerInput);

    Response save(Producer producer);

    Response findProductDTOById(Long id);

    Response findAll();

    Response update(Long producerId, ProducerInput producerInput);

    Response delete(Long producerId);

    Response delete(List<Long> producerIds);
}

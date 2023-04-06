package com.duckervn.movieservice.service;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addproducer.ProducerInput;

public interface IProducerService {
    Response findById(Long id);

    Response save(ProducerInput producerInput);

    Response save(Producer producer);

    Response findProductDTOById(Long id);

    Response findAll();
}

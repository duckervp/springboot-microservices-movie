package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.repository.GenreRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.repository.ProducerRepository;
import com.duckervn.movieservice.service.ICalculatedAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalculatedAttributeService implements ICalculatedAttributeService {
    private final MovieRepository movieRepository;

    private final GenreRepository genreRepository;

    private final ProducerRepository producerRepository;

    @Override
    public Response getAdminDashboardCalculatedAttributes() {
        long totalMovies =  movieRepository.count();
        long totalGenres =  genreRepository.count();
        long totalProducers =  producerRepository.count();
        Map<String, Object> calculatedAttributes = new LinkedHashMap<>();
        calculatedAttributes.put("totalMovies", totalMovies);
        calculatedAttributes.put("totalGenres", totalGenres);
        calculatedAttributes.put("totalProducers", totalProducers);
        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_CALCULATED_ATTRIBUTES)
                .result(calculatedAttributes)
                .build();
    }
}

package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.repository.ICustomMovieRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.service.IMovieService;
import com.duckervn.movieservice.service.IProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;

    private final ICustomMovieRepository customMovieRepository;

    private final IProducerService producerService;

    private final ObjectMapper objectMapper;
    /**
     * @param movieInput m
     */
    @Override
    public void save(MovieInput movieInput) {
        if (Objects.nonNull(movieInput.getId())) {
            update(movieInput);
            return;
        }
        Movie movie = objectMapper.convertValue(movieInput, Movie.class);
        Producer producer = movieInput.getProducer();
        if (!Objects.nonNull(producer.getId())) {
            producer.setCreatedAt(LocalDateTime.now());
            movie.setProducer(producer);
        } else {
            producer = producerService.findById(producer.getId());
            movie.setProducer(producer);
//            movie.addProducer(producer);
        }
        movie.setCreatedAt(LocalDateTime.now());
        movieRepository.save(movie);
    }

    /**
     * @param id i
     * @return movie
     */
    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id " + id));
    }

    /**
     * @return list movie
     */
    @Override
    public PageOutput<?> findMovie(String name, Integer releaseYear, String country, Long genreId, Integer pageNo, Integer pageSize) {
        if (Objects.nonNull(name)) {
            name = "%" + name + "%";
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return (PageOutput<?>) customMovieRepository.findMovieOutput(name, releaseYear, country, genreId, pageable).get(0);
    }

    /**
     * @param movieInput movieInput
     */
    @Override
    public void update(MovieInput movieInput) {
        Movie targetMovie = findById(movieInput.getId());
        if (Objects.nonNull(movieInput.getName()) && StringUtils.isNotBlank(movieInput.getName())) {
            targetMovie.setName(movieInput.getName());
        }
        if (Objects.nonNull(movieInput.getReleaseYear())) {
            targetMovie.setReleaseYear(movieInput.getReleaseYear());
        }
        if (Objects.nonNull(movieInput.getTotalEpisode())) {
            targetMovie.setTotalEpisode(movieInput.getTotalEpisode());
        }
        if (Objects.nonNull(movieInput.getCountry()) && StringUtils.isNotBlank(movieInput.getCountry())) {
            targetMovie.setCountry(movieInput.getCountry());
        }
        if (Objects.nonNull(movieInput.getBannerUrl()) && StringUtils.isNotBlank(movieInput.getBannerUrl())) {
            targetMovie.setBannerUrl(movieInput.getBannerUrl());
        }
        if (Objects.nonNull(movieInput.getPosterUrl()) && StringUtils.isNotBlank(movieInput.getPosterUrl())) {
            targetMovie.setPosterUrl(movieInput.getPosterUrl());
        }
        if (Objects.nonNull(movieInput.getDescription()) && StringUtils.isNotBlank(movieInput.getDescription())) {
            targetMovie.setDescription(movieInput.getDescription());
        }
        if (!CollectionUtils.isEmpty(movieInput.getEpisodes())) {
            movieInput.getEpisodes().forEach(targetMovie::addEpisode);
        }
        if (!CollectionUtils.isEmpty(movieInput.getCharacters())) {
            movieInput.getCharacters().forEach(targetMovie::addCharacter);
        }
        if (!CollectionUtils.isEmpty(movieInput.getGenres())) {
            movieInput.getGenres().forEach(targetMovie::addGenre);
        }
        targetMovie.setModifiedAt(LocalDateTime.now());
        movieRepository.save(targetMovie);
    }

    /**
     * @param id id
     * @return movie DTO
     */
    @Override
    public MovieDTO findMovieDTOById(Long id) {
        return movieRepository.findMovieDTOById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id "+ id));
    }
}

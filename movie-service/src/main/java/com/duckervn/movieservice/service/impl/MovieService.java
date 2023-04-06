package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.entity.Producer;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.repository.ICustomMovieRepository;
import com.duckervn.movieservice.repository.MovieRepository;
import com.duckervn.movieservice.repository.ProducerRepository;
import com.duckervn.movieservice.service.ICharacterService;
import com.duckervn.movieservice.service.IGenreService;
import com.duckervn.movieservice.service.IMovieService;
import com.duckervn.movieservice.service.IProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;

    private final ICustomMovieRepository customMovieRepository;


    private final IProducerService producerService;

    private final ObjectMapper objectMapper;

    private final ProducerRepository producerRepository;

    private final IGenreService genreService;

    private final ICharacterService characterService;

    /**
     * @param movieInput m
     * @return Response
     */
    @Override
    public Response save(MovieInput movieInput) {
        if (Objects.nonNull(movieInput.getId())) {
            update(movieInput);
        }
        Movie movie = objectMapper.convertValue(movieInput, Movie.class);
        Set<Genre> genres = movie.getGenres();
        for (Genre genre : genres) {
            if (Objects.isNull(genre.getId())) {
                genreService.save(genre);
            }
        }

        Set<Character> characters = movie.getCharacters();
        for (Character character : characters) {
            if (Objects.isNull(character.getId())) {
                characterService.save(character);
            }
        }

        Producer producer = movieInput.getProducer();
        if (!Objects.nonNull(producer.getId())) {
            producerService.save(producer);
            movie.setProducer(producer);
        } else {
            producer = producerRepository.findById(producer.getId())
                    .orElseThrow(ResourceNotFoundException::new);
            movie.setProducer(producer);
        }
        movie.setCreatedAt(LocalDateTime.now());
        movie.setModifiedAt(LocalDateTime.now());
        movieRepository.save(movie);
        return Response.builder().code(HttpStatus.CREATED.value()).message(RespMessage.CREATED_MOVIE).build();
    }

    /**
     * @param id i
     * @return movie
     */
    @Override
    public Response findMovie(Long id) {
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_MOVIE)
                .result(findById(id)).build();
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @return list movie
     */
    @Override
    public Response findMovie(String name, Integer releaseYear, String country, Long genreId, Integer pageNo, Integer pageSize) {
        if (Objects.nonNull(name)) {
            name = "%" + name + "%";
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageOutput<?> pageOutput = (PageOutput<?>) customMovieRepository.findMovieOutput(name, releaseYear, country, genreId, pageable).get(0);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_MOVIES)
                .results(Utils.toObjectList(pageOutput.getContent()))
                .pageSize(pageOutput.getPageSize())
                .totalElements(pageOutput.getTotalElements())
                .pageNo(pageOutput.getPageNo()).build();
    }

    /**
     * @param movieInput movieInput
     * @return Response
     */
    @Override
    public Response update(MovieInput movieInput) {
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
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.UPDATED_MOVIE).build();
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

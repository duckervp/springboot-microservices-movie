package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.*;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addmovie.MovieInput;
import com.duckervn.movieservice.domain.model.page.PageOutput;
import com.duckervn.movieservice.repository.*;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;

    private final ICustomMovieRepository customMovieRepository;

    private final IProducerService producerService;

    private final ObjectMapper objectMapper;

    private final ProducerRepository producerRepository;

    private final IGenreService genreService;

    private final ICharacterService characterService;

    private final CharacterRepository characterRepository;

    private final EpisodeRepository episodeRepository;

    private Producer processInputProducer(Producer producer) {
        if (Objects.isNull(producer.getId())) {
            producerService.save(producer);
        } else {
            producer = producerRepository.findById(producer.getId())
                    .orElseThrow(ResourceNotFoundException::new);

        }
        return producer;
    }

    private Set<Genre> processInputGenres(Set<Genre> genres) {
        return genres.stream().map(genre -> {
            if (Objects.isNull(genre.getId())) {
                genreService.save(genre);
                return genre;
            } else {
                return genreService.findById(genre.getId());
            }
        }).collect(Collectors.toSet());
    }

    private Set<Character> processInputCharacters(Set<Character> characters) {
        return characters.stream().map(character -> {
            if (Objects.isNull(character.getId())) {
                characterService.save(character);
                return character;
            } else {
                return characterService.findById(character.getId());
            }
        }).collect(Collectors.toSet());
    }

    /**
     * @param movieInput m
     * @return Response
     */
    @Override
    public Response save(MovieInput movieInput) {
        if (Objects.nonNull(movieInput.getId())) {
            update(movieInput.getId(), movieInput);
        }
        Movie movie = objectMapper.convertValue(movieInput, Movie.class);

        movie.setGenres(processInputGenres(movie.getGenres()));

        movie.setCharacters(processInputCharacters(movie.getCharacters()));

        movie.setProducer(processInputProducer(movieInput.getProducer()));

        if (Objects.isNull(movieInput.getSlug())) {
            movie.setSlug(Utils.genSlug(movie.getName()));
        }

        movie.setCreatedAt(LocalDateTime.now());

        movie.setModifiedAt(LocalDateTime.now());

        movieRepository.save(movie);

        return Response.builder()
                .code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_MOVIE)
                .result(movie).build();
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
    public Response findMovie(String name, Integer releaseYear, String country, String genre, Integer pageNo, Integer pageSize) {
        if (Objects.nonNull(name)) {
            name = "%" + name + "%";
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        PageOutput<?> pageOutput = customMovieRepository.findMovieOutput(name, releaseYear, country, genre, pageable);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_ALL_MOVIES)
                .results(pageOutput.getContent())
                .pageSize(pageOutput.getPageSize())
                .totalElements(pageOutput.getTotalElements())
                .pageNo(pageOutput.getPageNo()).build();
    }

    /**
     * @param id         movie id
     * @param movieInput movieInput
     * @return Response
     */
    @Override
    public Response update(Long id, MovieInput movieInput) {
        Movie targetMovie = findById(id);

        if (StringUtils.isNotBlank(movieInput.getName())) {
            targetMovie.setName(movieInput.getName());
            if (Objects.isNull(movieInput.getSlug())) {
                targetMovie.setSlug(Utils.genSlug(targetMovie.getName()));
            }
        }

        if (Objects.nonNull(movieInput.getSlug())) {
            targetMovie.setSlug(movieInput.getSlug());
        }

        if (Objects.nonNull(movieInput.getReleaseYear())) {
            targetMovie.setReleaseYear(movieInput.getReleaseYear());
        }

        if (Objects.nonNull(movieInput.getTotalEpisode())) {
            targetMovie.setTotalEpisode(movieInput.getTotalEpisode());
        }

        if (StringUtils.isNotBlank(movieInput.getCountry())) {
            targetMovie.setCountry(movieInput.getCountry());
        }

        if (StringUtils.isNotBlank(movieInput.getBannerUrl())) {
            targetMovie.setBannerUrl(movieInput.getBannerUrl());
        }

        if (StringUtils.isNotBlank(movieInput.getPosterUrl())) {
            targetMovie.setPosterUrl(movieInput.getPosterUrl());
        }

        if (StringUtils.isNotBlank(movieInput.getDescription())) {
            targetMovie.setDescription(movieInput.getDescription());
        }

        for (Episode episode : movieInput.getEpisodes()) {
            if (Objects.isNull(episode.getId())) {
                episode.setCreatedAt(LocalDateTime.now());
            }
            episode.setModifiedAt(LocalDateTime.now());
        }
        targetMovie.setEpisodes(movieInput.getEpisodes());

        targetMovie.setGenres(processInputGenres(movieInput.getGenres()));

        Set<Character> movieCharacters = movieInput.getCharacters().stream().map(character -> {
            if (Objects.isNull(character.getId())) {
                characterService.save(character);
                return character;
            } else {
                Character character1 = characterService.findById(character.getId());
                if (!character1.getName().equals(character.getName())
                        || !character1.getDescription().equals(character.getDescription())
                        || !character1.getAvatarUrl().equals(character.getAvatarUrl())) {
                    character1.setName(character.getName());
                    character1.setDescription(character.getDescription());
                    character1.setAvatarUrl(character.getAvatarUrl());
                    character1.setModifiedAt(LocalDateTime.now());
                    characterRepository.save(character1);
                }
                return character1;
            }
        }).collect(Collectors.toSet());

        targetMovie.setCharacters(movieCharacters);

        targetMovie.setProducer(processInputProducer(movieInput.getProducer()));

        targetMovie.setModifiedAt(LocalDateTime.now());

        movieRepository.save(targetMovie);

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_MOVIE)
                .result(targetMovie).build();
    }

    /**
     * @param id id
     * @return movie DTO
     */
    @Override
    public MovieDTO findMovieDTOById(Long id) {
        return movieRepository.findMovieDTOById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Response findMovie(String slug) {
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_MOVIE)
                .result(movieRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new)).build();
    }

    @Override
    public Response resetSlug() {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            movie.setSlug(Utils.genSlug(movie.getName()));
        }
        movieRepository.saveAll(movies);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.RESET_SLUG).build();
    }

    @Override
    public Response delete(Long id) {
        Movie movie = findById(id);
        Set<Episode> episodes = movie.getEpisodes();
        episodeRepository.deleteAll(episodes);
        movieRepository.delete(movie);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_MOVIE).build();
    }

    @Override
    public Response delete(List<Long> ids) {
        List<Movie> movies = movieRepository.findAllById(ids);

        movieRepository.deleteAll(movies);

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.DELETED_MOVIE)
                .build();
    }
}

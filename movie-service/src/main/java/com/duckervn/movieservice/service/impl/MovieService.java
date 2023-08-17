package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.entity.Character;
import com.duckervn.movieservice.domain.entity.*;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    public Movie save(MovieInput movieInput) {
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

        movie.setRating(0D);

        movie.setCreatedAt(LocalDateTime.now());

        movie.setModifiedAt(LocalDateTime.now());

        movieRepository.save(movie);

        return movie;
    }

    @Override
    public Movie findById(Long id) {
        return movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @return list movie
     */
    @Override
    @Cacheable(value = "movie")
    public PageOutput<?> findMovie(String name, Integer releaseYear, String country, String genre, Integer pageNo, Integer pageSize) {
        if (Objects.nonNull(name)) {
            name = "%" + name + "%";
        }
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return customMovieRepository.findMovieOutput(name, releaseYear, country, genre, pageable);
    }

    /**
     * @param id         movie id
     * @param movieInput movieInput
     * @return Response
     */
    @Override
    @CacheEvict(value = "movie", allEntries = true)
    public Movie update(Long id, MovieInput movieInput) {
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

        if (!CollectionUtils.isEmpty(movieInput.getEpisodes())) {
            for (Episode episode : movieInput.getEpisodes()) {
                if (Objects.isNull(episode.getId())) {
                    episode.setCreatedAt(LocalDateTime.now());
                }
                episode.setModifiedAt(LocalDateTime.now());
            }
            targetMovie.setEpisodes(movieInput.getEpisodes());
        }

        if (!CollectionUtils.isEmpty(movieInput.getGenres())) {
            targetMovie.setGenres(processInputGenres(movieInput.getGenres()));
        }

        if (!CollectionUtils.isEmpty(movieInput.getCharacters())) {
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
        }

        if (Objects.nonNull(movieInput.getProducer())) {
            targetMovie.setProducer(processInputProducer(movieInput.getProducer()));
        }

        targetMovie.setModifiedAt(LocalDateTime.now());

        movieRepository.save(targetMovie);

        return targetMovie;
    }

    @Override
    @Cacheable(value = "movie", key = "#slug")
    public Movie findMovie(String slug) {
        return movieRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void resetSlug() {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            movie.setSlug(Utils.genSlug(movie.getName()));
        }
        movieRepository.saveAll(movies);
    }

    @Override
    @CacheEvict(value = "movie", allEntries = true)
    public void delete(Long id) {
        Movie movie = findById(id);
        Set<Episode> episodes = movie.getEpisodes();
        episodeRepository.deleteAll(episodes);
        movieRepository.delete(movie);
    }

    @Override
    @CacheEvict(value = "movie", allEntries = true)
    public void delete(List<Long> ids) {
        List<Movie> movies = movieRepository.findAllById(ids);
        movieRepository.deleteAll(movies);
    }
}

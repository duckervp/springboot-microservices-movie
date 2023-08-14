package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.repository.GenreRepository;
import com.duckervn.movieservice.repository.MovieGenreRepository;
import com.duckervn.movieservice.service.IGenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class GenreService implements IGenreService {
    private final GenreRepository genreRepository;

    private final MovieGenreRepository movieGenreRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @param genreInput genre input
     * @return Response
     */
    @Override
    public Genre save(GenreInput genreInput) {
        Genre genre = objectMapper.convertValue(genreInput, Genre.class);

        save(genre);

        return genre;
    }

    /**
     * Save genre with audit field
     * @param genre genre entity
     */
    @Override
    public void save(Genre genre) {
        genre.setSlug(Utils.genSlug(genre.getName()));

        genre.setCreatedAt(LocalDateTime.now());

        genre.setModifiedAt(LocalDateTime.now());

        genreRepository.save(genre);
    }

    /**
     * @return list genre
     */
    @Override
    @Cacheable(value = "genre")
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    @CacheEvict(value = "genre", allEntries = true)
    public Genre update(Long genreId, GenreInput genreInput) {
        Genre genre = findById(genreId);

        if (Objects.nonNull(genreInput.getName())) {
            genre.setName(genreInput.getName());
        }

        if (Objects.nonNull(genreInput.getDescription())) {
            genre.setDescription(genreInput.getDescription());
        }

        genre.setSlug(Utils.genSlug(genre.getName()));

        genre.setModifiedAt(LocalDateTime.now());

        genreRepository.save(genre);

        return genre;
    }

    @Override
    @CacheEvict(value = "genre", allEntries = true)
    public void delete(Long genreId) {
        Genre genre = findById(genreId);

        movieGenreRepository.deleteByGenreId(genreId);

        genreRepository.delete(genre);
    }

    @Override
    @CacheEvict(value = "genre", allEntries = true)
    public void delete(List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAllById(genreIds);

        movieGenreRepository.deleteByGenreIdIn(genreIds);

        genreRepository.deleteAll(genres);
    }
}

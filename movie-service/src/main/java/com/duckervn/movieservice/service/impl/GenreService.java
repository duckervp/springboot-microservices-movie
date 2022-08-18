package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.repository.GenreRepository;
import com.duckervn.movieservice.service.IGenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService implements IGenreService {
    private final GenreRepository genreRepository;

    private final ObjectMapper objectMapper;

    /**
     * @param id id
     * @return genre
     */
    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Genre not found with id " + id));
    }

    /**
     * @param genreInput genre input
     */
    @Override
    public void save(GenreInput genreInput) {
        Genre genre = objectMapper.convertValue(genreInput, Genre.class);
        genre.setCreatedAt(LocalDateTime.now());
        genreRepository.save(genre);
    }

    /**
     * @return list genre
     */
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}

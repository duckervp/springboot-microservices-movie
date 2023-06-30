package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Utils;
import com.duckervn.movieservice.domain.entity.Genre;
import com.duckervn.movieservice.domain.exception.ResourceNotFoundException;
import com.duckervn.movieservice.domain.model.addgenre.GenreInput;
import com.duckervn.movieservice.repository.GenreRepository;
import com.duckervn.movieservice.repository.MovieGenreRepository;
import com.duckervn.movieservice.service.IGenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    /**
     * @param id id
     * @return genre
     */
    @Override
    public Response findGenre(Long id) {
        Genre genre = findById(id);
        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.FOUND_GENRE)
                .result(genre).build();
    }

    @Override
    public Genre findById(Long id) {
        return genreRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * @param genreInput genre input
     * @return Response
     */
    @Override
    public Response save(GenreInput genreInput) {
        Genre genre = objectMapper.convertValue(genreInput, Genre.class);

        save(genre);

        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.CREATED_GENRE)
                .result(genre).build();
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
    public Response findAll() {
        return Response.builder().code(HttpStatus.OK.value())
                .message(RespMessage.FOUND_ALL_GENRES)
                .results(genreRepository.findAll()).build();
    }

    @Override
    public Response update(Long genreId, GenreInput genreInput) {
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

        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_GENRE)
                .result(genre)
                .build();
    }

    @Override
    public Response delete(Long genreId) {
        Genre genre = findById(genreId);

        movieGenreRepository.deleteByGenreId(genreId);

        genreRepository.delete(genre);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_GENRE).build();
    }

    @Override
    public Response delete(List<Long> genreIds) {
        List<Genre> genres = genreRepository.findAllById(genreIds);

        movieGenreRepository.deleteByGenreIdIn(genreIds);

        genreRepository.deleteAll(genres);

        return Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_GENRES).build();
    }
}

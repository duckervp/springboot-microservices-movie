package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.MovieGenre;
import com.duckervn.movieservice.repository.MovieGenreRepository;
import com.duckervn.movieservice.service.IMovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class MovieGenreService implements IMovieGenreService {
    private final MovieGenreRepository movieGenreRepository;

    @Override
    public void add(Long movieId, List<Long> genreIds) {
        List<MovieGenre> movieGenres = new ArrayList<>();
        for (Long genreId : genreIds) {
            movieGenres.add(new MovieGenre(movieId, genreId));
        }
        movieGenreRepository.saveAll(movieGenres);
    }

    @Override
    public void delete(Long movieId, List<Long> genreIds) {
        movieGenreRepository.deleteByMovieIdAndGenreIdIn(movieId, genreIds);
    }
}

package com.duckervn.movieservice.service.impl;

import com.duckervn.movieservice.common.RespMessage;
import com.duckervn.movieservice.common.Response;
import com.duckervn.movieservice.domain.entity.MovieCharacter;
import com.duckervn.movieservice.repository.MovieCharacterRepository;
import com.duckervn.movieservice.service.IMovieCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class MovieCharacterService implements IMovieCharacterService {

    private final MovieCharacterRepository movieCharacterRepository;

    @Override
    public void add(Long movieId, List<Long> characterIds) {
        List<MovieCharacter> movieCharacters = new ArrayList<>();
        for (Long characterId : characterIds) {
            movieCharacters.add(new MovieCharacter(movieId, characterId));
        }
        movieCharacterRepository.saveAll(movieCharacters);
    }

    @Override
    public void delete(Long movieId, List<Long> characterIds) {
        movieCharacterRepository.deleteByMovieIdAndCharacterIdIn(movieId, characterIds);
    }
}

package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.bean.MovieCharacterId;
import com.duckervn.movieservice.domain.entity.MovieCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCharacterRepository extends JpaRepository<MovieCharacter, MovieCharacterId> {
    void deleteByCharacterId(Long characterId);

    void deleteByCharacterIdIn(List<Long> characterIds);

    void deleteByMovieIdAndCharacterIdIn(Long movieId, List<Long> characterIds);
}

package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.dto.EpisodeDTO;
import com.duckervn.movieservice.domain.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    @Query(value = "SELECT id, name, duration, url, description, movie_id as movieId " +
            "FROM episode WHERE id = :id", nativeQuery = true)
    Optional<EpisodeDTO> findEpisodeDTOById(Long id);
}

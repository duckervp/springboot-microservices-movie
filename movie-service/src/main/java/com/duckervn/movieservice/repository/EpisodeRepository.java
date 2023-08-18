package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @Query("SELECT e.url FROM Episode e")
    List<String> findEpisodeMovieUrls();

    @Query(value = "SELECT * FROM episode ep WHERE ep.movie_id = (SELECT e.movie_id FROM episode e WHERE e.id = :currentEpId) " +
            "AND ep.id > :currentEpId ORDER BY ep.id ASC LIMIT 1", nativeQuery = true)
    Optional<Episode> findNextEpisode(Long currentEpId);
}

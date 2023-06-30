package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    @Query("SELECT e.url FROM Episode e")
    List<String> findEpisodeMovieUrls();

}

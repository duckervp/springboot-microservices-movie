package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "SELECT id, name, release_year AS releaseYear, " +
            "total_episode AS totalEpisode, country, banner_url AS bannerUrl, " +
            "poster_url AS posterUrl, description, created_at AS createdAt, modified_at AS modifiedAt " +
            "FROM movie WHERE id = :id", nativeQuery = true)
    Optional<MovieDTO> findMovieDTOById(Long id);

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    Optional<Movie> findById(Long id);

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    Optional<Movie> findBySlug(String slug);

    @Query(value = "SELECT * FROM movie m WHERE m.id in (:movieIds)", nativeQuery = true)
    List<Movie> findByIds(List<Long> movieIds);

}

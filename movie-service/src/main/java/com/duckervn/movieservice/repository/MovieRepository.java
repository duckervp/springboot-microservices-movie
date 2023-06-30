package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.dto.MovieDTO;
import com.duckervn.movieservice.domain.dto.MovieImageDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query(value = "SELECT m.id, m.name, m.releaseYear AS releaseYear, " +
            "m.totalEpisode AS totalEpisode, m.country, m.bannerUrl AS bannerUrl, " +
            "m.posterUrl AS posterUrl, m.description, m.createdAt AS createdAt, m.modifiedAt AS modifiedAt " +
            "FROM Movie m WHERE m.id = :id")
    Optional<MovieDTO> findMovieDTOById(Long id);

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    Optional<Movie> findById(Long id);

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    Optional<Movie> findBySlug(String slug);

    @Query("SELECT m.bannerUrl AS bannerUrl, m.posterUrl AS posterUrl FROM Movie m")
    List<MovieImageDTO> findMovieImageUrls();
}

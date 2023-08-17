package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.dto.MovieImageDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    @NonNull
    Optional<Movie> findById(@NonNull Long id);

    @EntityGraph(Movie.FULL_MOVIE_GRAPH)
    Optional<Movie> findBySlug(String slug);

    @Query("SELECT m.bannerUrl AS bannerUrl, m.posterUrl AS posterUrl FROM Movie m")
    List<MovieImageDTO> findMovieImageUrls();
}

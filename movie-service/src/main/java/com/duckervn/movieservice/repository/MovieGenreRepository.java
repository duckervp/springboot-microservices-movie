package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.bean.MovieGenreId;
import com.duckervn.movieservice.domain.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {

    void deleteByGenreId(Long genreId);

    void deleteByGenreIdIn(List<Long> genreIds);
}

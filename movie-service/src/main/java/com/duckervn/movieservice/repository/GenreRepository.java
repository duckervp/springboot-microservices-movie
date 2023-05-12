package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(value = "SELECT * FROM genre g WHERE g.id in (:genreIds)", nativeQuery = true)
    List<Genre> findByIds(List<Long> genreIds);
}

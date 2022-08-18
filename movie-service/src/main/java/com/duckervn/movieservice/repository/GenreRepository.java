package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {}

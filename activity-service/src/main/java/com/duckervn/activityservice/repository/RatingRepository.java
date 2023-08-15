package com.duckervn.activityservice.repository;

import com.duckervn.activityservice.domain.entity.LoginActivity;
import com.duckervn.activityservice.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);
}

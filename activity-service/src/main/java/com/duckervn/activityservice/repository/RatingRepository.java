package com.duckervn.activityservice.repository;

import com.duckervn.activityservice.domain.entity.LoginActivity;
import com.duckervn.activityservice.domain.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByMovieId(Long movieId);

    Optional<Rating> findByUserIdAndMovieId(String userId, Long movieId);
}

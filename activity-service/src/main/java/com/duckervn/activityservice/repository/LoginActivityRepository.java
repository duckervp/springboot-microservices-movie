package com.duckervn.activityservice.repository;

import com.duckervn.activityservice.domain.entity.LoginActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LoginActivityRepository extends JpaRepository<LoginActivity, Long> {

    @Query("SELECT COUNT(*) FROM LoginActivity la WHERE la.userId = :userId AND DATE(la.loginAt) = DATE(:date) ")
    Long countByUserIdAndLoginAt(String userId, LocalDate date);
}

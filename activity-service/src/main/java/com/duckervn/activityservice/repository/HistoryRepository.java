package com.duckervn.activityservice.repository;

import com.duckervn.activityservice.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query(value = "SELECT * FROM (SELECT h.*, ROW_NUMBER() " +
            "OVER (PARTITION  BY (h.movie_id) ORDER BY h.created_at DESC) AS rank1 FROM history h WHERE h.user_id = :userId) AS ranks " +
            "WHERE rank1 = 1", nativeQuery = true)
    List<History> findUserWatchedHistory(String userId);
}

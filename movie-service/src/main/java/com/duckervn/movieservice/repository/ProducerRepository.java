package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.dto.ProducerDTO;
import com.duckervn.movieservice.domain.entity.Movie;
import com.duckervn.movieservice.domain.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    @Query(value = "SELECT id, name, description,  created_at AS createdAt, modified_at AS modifiedAt " +
            "FROM producer WHERE id = :id", nativeQuery = true)
    Optional<ProducerDTO> findProducerDTOById(Long id);

    @Query(value = "SELECT * from producer p WHERE p.id IN (:producerIds)", nativeQuery = true)
    List<Producer> findByIds(List<Long> producerIds);
}

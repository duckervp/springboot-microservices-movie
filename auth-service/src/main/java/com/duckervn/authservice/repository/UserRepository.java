package com.duckervn.authservice.repository;

import com.duckervn.authservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String id);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u.avatarUrl FROM User u")
    List<String> findUserImageUrls();
}

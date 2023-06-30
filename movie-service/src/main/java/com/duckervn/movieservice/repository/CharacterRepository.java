package com.duckervn.movieservice.repository;

import com.duckervn.movieservice.domain.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("SELECT c FROM Character c WHERE (:name IS NULL OR c.name LIKE :name)")
    List<Character> findAll(String name);

    @Query("SELECT c.avatarUrl FROM Character c")
    List<String> findAvatarUrls();
}

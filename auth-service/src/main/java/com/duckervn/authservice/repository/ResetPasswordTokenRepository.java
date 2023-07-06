package com.duckervn.authservice.repository;

import com.duckervn.authservice.domain.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
    List<ResetPasswordToken> findByClientId(String clientId);
}

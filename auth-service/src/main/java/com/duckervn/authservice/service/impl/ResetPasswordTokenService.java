package com.duckervn.authservice.service.impl;

import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.ResetPasswordToken;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import com.duckervn.authservice.repository.ResetPasswordTokenRepository;
import com.duckervn.authservice.service.IResetPasswordTokenService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordTokenService implements IResetPasswordTokenService {

    private final ResetPasswordTokenRepository passwordTokenRepository;

    private final ServiceConfig serviceConfig;

    @Override
    public ResetPasswordToken generateRPT(String clientId) {
        String id = UUID.randomUUID().toString();
        String token = Base64.getEncoder().encodeToString(id.getBytes());
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(serviceConfig.getRptValidityInMinute());
        Timestamp timestamp = Timestamp.valueOf(expiredAt);
        ResetPasswordToken rpt = new ResetPasswordToken(id, clientId, token, timestamp.getTime());
        return add(rpt);
    }

    private ResetPasswordToken add(ResetPasswordToken resetPasswordToken) {
        List<ResetPasswordToken> oldRPT = passwordTokenRepository.findByClientId(resetPasswordToken.getClientId());
        passwordTokenRepository.deleteAll(oldRPT);
        passwordTokenRepository.save(resetPasswordToken);
        return resetPasswordToken;
    }

    @Override
    public ResetPasswordToken getById(String tokenId) {
        return passwordTokenRepository.findById(tokenId).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void remove(ResetPasswordToken resetPasswordToken) {
        passwordTokenRepository.delete(resetPasswordToken);
    }

}

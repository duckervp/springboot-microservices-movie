package com.duckervn.authservice.service;

import com.duckervn.authservice.domain.entity.ResetPasswordToken;

public interface IResetPasswordTokenService {

    ResetPasswordToken generateRPT(String clientId);

    ResetPasswordToken getById(String tokenId);

    void remove(ResetPasswordToken resetPasswordToken);
}

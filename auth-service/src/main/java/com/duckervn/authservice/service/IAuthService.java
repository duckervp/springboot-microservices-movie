package com.duckervn.authservice.service;

import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;

public interface IAuthService {
    TokenOutput register(RegisterInput registerInput);

    TokenOutput login(String clientId, String clientSecret);

    TokenOutput refresh(String refreshToken);

    void updatePassword(Client client, String newPassword, boolean encode);

    void changePassword(ChangePasswordInput changePasswordInput);

    void requestResetPassword(String email);

    void resetPassword(String token, ResetPasswordInput resetPasswordInput);
}

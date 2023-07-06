package com.duckervn.authservice.service;

import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;

public interface IUserService {

    TokenOutput register(RegisterInput registerInput);

    User findById(String id);

    Response updateUser(String userId, UpdateUserInput input);

    Response deleteUser(String userId);

    TokenOutput login(String clientId, String clientSecret);

    void updatePassword(Client client, String newPassword, boolean encode);

    Response changePassword(ChangePasswordInput changePasswordInput);

    Response requestResetPassword(String email);

    Response resetPassword(String token, ResetPasswordInput resetPasswordInput);

    Response findAll();
}

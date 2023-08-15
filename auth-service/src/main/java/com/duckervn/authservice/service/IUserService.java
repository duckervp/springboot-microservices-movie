package com.duckervn.authservice.service;

import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.Client;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;

import java.util.List;

public interface IUserService {

    TokenOutput register(RegisterInput registerInput);

    User findById(String id);

    User updateUser(String userId, UpdateUserInput input);

    void deleteUser(String userId);

    TokenOutput login(String clientId, String clientSecret);

    void updatePassword(Client client, String newPassword, boolean encode);

    void changePassword(ChangePasswordInput changePasswordInput);

    void requestResetPassword(String email);

    void resetPassword(String token, ResetPasswordInput resetPasswordInput);

    List<User> findAll();
}

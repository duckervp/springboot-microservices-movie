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

    User findById(String id);

    User updateUser(String userId, UpdateUserInput input);

    void deleteUser(String userId);

    List<User> findAll();
}

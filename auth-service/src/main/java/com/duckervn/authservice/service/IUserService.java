package com.duckervn.authservice.service;

import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;

import java.util.Map;

public interface IUserService {
    Map<String, String> register(RegisterInput registerInput);

    User findById(String id);

    Response updateUser(String userId, UpdateUserInput input);

    Response deleteUser(String userId);
}

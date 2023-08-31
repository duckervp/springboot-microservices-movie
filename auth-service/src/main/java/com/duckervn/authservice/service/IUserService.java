package com.duckervn.authservice.service;

import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;

import java.util.List;

public interface IUserService {

    User findById(String id);

    User updateUser(String userId, UpdateUserInput input);

    void deleteUser(String userId);

    List<User> findAll();
}

package com.duckervn.authservice.service;

import com.duckervn.authservice.domain.model.register.RegisterInput;

import java.util.Map;

public interface IUserService {
    Map<String, String> register(RegisterInput registerInput);
}

package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.entity.LoginActivity;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;
import com.duckervn.activityservice.repository.LoginActivityRepository;
import com.duckervn.activityservice.service.ILoginActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginActivityService implements ILoginActivityService {

    private final LoginActivityRepository loginActivityRepository;

    private final ObjectMapper objectMapper;

    @Override
    public Response save(LoginActivityInput input) {
        LoginActivity loginActivity = objectMapper.convertValue(input, LoginActivity.class);

        loginActivity.setLoginAt(LocalDateTime.now());

        loginActivityRepository.save(loginActivity);

        // TODO: public add user exp here

        return Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.ADDED_LOGIN_ACTIVITY)
                .result(loginActivity).build();
    }

}

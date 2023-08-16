package com.duckervn.activityservice.service.impl;

import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.entity.LoginActivity;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;
import com.duckervn.activityservice.queue.EventProducer;
import com.duckervn.activityservice.repository.LoginActivityRepository;
import com.duckervn.activityservice.service.ILoginActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginActivityService implements ILoginActivityService {

    private final LoginActivityRepository loginActivityRepository;

    private final ObjectMapper objectMapper;

    private final EventProducer eventProducer;

    private final ServiceConfig serviceConfig;

    @Override
    public LoginActivity save(LoginActivityInput input) {
        LoginActivity loginActivity = objectMapper.convertValue(input, LoginActivity.class);

        loginActivity.setLoginAt(LocalDateTime.now());

        loginActivityRepository.save(loginActivity);

        updateUserExp(loginActivity);

        return loginActivity;
    }

    private void updateUserExp(LoginActivity loginActivity) {
        Long countDateLogin = loginActivityRepository.countByUserIdAndLoginAt(loginActivity.getUserId(), loginActivity.getLoginAt().toLocalDate());
        if (countDateLogin > 1) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put("userId",  loginActivity.getUserId());
        eventProducer.publish(serviceConfig.getUserTopic(), serviceConfig.getUpdateUserExpEvent(), data);
    }

}

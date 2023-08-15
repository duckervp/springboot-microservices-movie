package com.duckervn.activityservice.service;

import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.entity.LoginActivity;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;

public interface ILoginActivityService {
    LoginActivity save(LoginActivityInput input);

}

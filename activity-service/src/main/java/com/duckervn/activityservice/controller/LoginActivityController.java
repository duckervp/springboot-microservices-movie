package com.duckervn.activityservice.controller;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.model.addloginactivity.LoginActivityInput;
import com.duckervn.activityservice.service.ILoginActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/login")
public class LoginActivityController {
    private final ILoginActivityService campaignService;

    @PostMapping
    public ResponseEntity<?> addLoginActivity(@RequestBody LoginActivityInput input) {
        Response response = Response.builder().code(HttpStatus.CREATED.value())
                .message(RespMessage.ADDED_LOGIN_ACTIVITY)
                .result(campaignService.save(input)).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

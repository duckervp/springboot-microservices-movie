package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.Credential;
import com.duckervn.authservice.domain.model.getToken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.service.IUserService;
import com.duckervn.authservice.service.client.OAuth2AuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/user")
public class AuthController {
    private final IUserService userService;

    private final OAuth2AuthorizationClient authorizationClient;

    @PostMapping("/register")
    public TokenOutput register(@RequestBody @Valid RegisterInput registerInput) {
        Map<String, String> userCredentials = userService.register(registerInput);
        return authorizationClient.getToken(Credential.GRANT_TYPE,
                userCredentials.get(Credential.CLIENT_ID),
                userCredentials.get(Credential.CLIENT_SECRET));
    }
}

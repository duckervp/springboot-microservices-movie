package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.Credential;
import com.duckervn.authservice.common.RespMessage;
import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.getToken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;
import com.duckervn.authservice.resolver.annotation.UserInfo;
import com.duckervn.authservice.service.IUserService;
import com.duckervn.authservice.service.client.OAuth2AuthorizationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    private final OAuth2AuthorizationClient authorizationClient;

    @GetMapping
    public ResponseEntity<?> fetchUser(@UserInfo Optional<User> user) {
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(RespMessage.FOUND_USER)
                        .result(user).build());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserInput input) {
        return ResponseEntity.ok(userService.updateUser(userId, input));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @PostMapping("/register")
    public TokenOutput register(@RequestBody @Valid RegisterInput registerInput) {
        Map<String, String> userCredentials = userService.register(registerInput);
        return authorizationClient.getToken(Credential.GRANT_TYPE,
                userCredentials.get(Credential.CLIENT_ID),
                userCredentials.get(Credential.CLIENT_SECRET));
    }
}

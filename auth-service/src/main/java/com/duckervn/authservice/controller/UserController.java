package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.RespMessage;
import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;
import com.duckervn.authservice.resolver.annotation.UserInfo;
import com.duckervn.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/")
    public ResponseEntity<?> fetchUser(@UserInfo Optional<User> user) {
        return ResponseEntity.ok(
                Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(RespMessage.FOUND_USER)
                        .result(user).build());
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserInput input) {
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_USER)
                .result(userService.updateUser(userId, input)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_USER).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Response> findById(@PathVariable String userId) {
        return ResponseEntity.ok(Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(RespMessage.FOUND_USER)
                        .result(userService.findById(userId))
                .build());
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Response> findAllUsers() {
        return ResponseEntity.ok(Response.builder()
                        .code(HttpStatus.OK.value())
                        .message(RespMessage.FOUND_USERS)
                        .results(userService.findAll())
                .build());
    }

    @PostMapping("/register")
    public TokenOutput register(@RequestBody @Valid RegisterInput registerInput) {
        return userService.register(registerInput);
    }

    @PostMapping("/login")
    public TokenOutput login(@RequestParam String clientId, @RequestParam String clientSecret) {
        return userService.login(clientId, clientSecret);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid ChangePasswordInput changePasswordInput) {
        userService.changePassword(changePasswordInput);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.PASSWORD_CHANGED).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reset-password-request")
    public ResponseEntity<Response> requestResetPassword(@RequestParam String email) {
        userService.requestResetPassword(email);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.REQUEST_PASSWORD_RESET).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordInput resetPasswordInput) {
        userService.resetPassword(token, resetPasswordInput);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.PASSWORD_RESET).build();
        return ResponseEntity.ok(response);
    }
}

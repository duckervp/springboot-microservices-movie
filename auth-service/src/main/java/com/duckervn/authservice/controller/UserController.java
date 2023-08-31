package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.RespMessage;
import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.domain.exception.AccessDeniedException;
import com.duckervn.authservice.domain.model.auth.Auth;
import com.duckervn.authservice.domain.model.updateuser.UpdateUserInput;
import com.duckervn.authservice.resolver.annotation.AuthInfo;
import com.duckervn.authservice.resolver.annotation.UserInfo;
import com.duckervn.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody @Valid UpdateUserInput input, @AuthInfo Auth auth) {
        if (!auth.isAdmin() && !auth.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }
        Response response = Response.builder()
                .code(HttpStatus.OK.value())
                .message(RespMessage.UPDATED_USER)
                .result(userService.updateUser(userId, input)).build();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId, @AuthInfo Auth auth) {
        if (!auth.isAdmin() && !auth.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }
        userService.deleteUser(userId);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.DELETED_USER).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> findById(@PathVariable String userId, @AuthInfo Auth auth) {
        if (!auth.isAdmin() && !auth.getUserId().equals(userId)) {
            throw new AccessDeniedException();
        }
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

}

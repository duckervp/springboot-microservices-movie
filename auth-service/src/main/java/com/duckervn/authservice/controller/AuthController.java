package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.Constants;
import com.duckervn.authservice.common.Credential;
import com.duckervn.authservice.common.RespMessage;
import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.model.changepassword.ChangePasswordInput;
import com.duckervn.authservice.domain.model.gettoken.TokenOutput;
import com.duckervn.authservice.domain.model.login.CredentialInput;
import com.duckervn.authservice.domain.model.register.RegisterInput;
import com.duckervn.authservice.domain.model.resetpassword.ResetPasswordInput;
import com.duckervn.authservice.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/auth")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterInput registerInput) {
        TokenOutput tokenOutput = authService.register(registerInput);
        return responseWithCookie(tokenOutput);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialInput credential) {
        TokenOutput tokenOutput = authService.login(credential.getClientId(), credential.getClientSecret());
        return responseWithCookie(tokenOutput);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid ChangePasswordInput changePasswordInput) {
        authService.changePassword(changePasswordInput);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.PASSWORD_CHANGED).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reset-password-request")
    public ResponseEntity<Response> requestResetPassword(@RequestParam String email) {
        authService.requestResetPassword(email);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.REQUEST_PASSWORD_RESET).build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<Response> resetPassword(@RequestParam String token, @RequestBody @Valid ResetPasswordInput resetPasswordInput) {
        authService.resetPassword(token, resetPasswordInput);
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.PASSWORD_RESET).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = Constants.REFRESH_TOKEN_COOKIE) String refreshToken) {
        TokenOutput tokenOutput = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenOutput);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = Constants.REFRESH_TOKEN_COOKIE) String refreshToken) {
        authService.logout(refreshToken);
        ResponseCookie refreshTokenCookie = ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None")
//                .maxAge(0)
                .build();
        Response response = Response.builder().code(HttpStatus.OK.value()).message(RespMessage.LOGGED_OUT).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body(response);
    }

    private ResponseEntity<?> responseWithCookie(TokenOutput tokenOutput) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(Constants.REFRESH_TOKEN_COOKIE, tokenOutput.getRefresh_token())
                .httpOnly(true)
                .secure(true)
                .maxAge(86400)
                .path("/")
                .sameSite("None")
                .build();

        tokenOutput.setRefresh_token(null);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString()).body(tokenOutput);
    }
}

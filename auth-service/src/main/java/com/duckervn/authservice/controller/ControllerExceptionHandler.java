package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.RespMessage;
import com.duckervn.authservice.common.Response;
import com.duckervn.authservice.domain.exception.AccessDeniedException;
import com.duckervn.authservice.domain.exception.InvalidTokenException;
import com.duckervn.authservice.domain.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleException(FeignException e) {
        log.info("Error: ", e);
        if (e.status() == 401) {
            Response response =  Response.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid credentials")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Response response =  Response.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(InvalidTokenException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        log.info("Error: ", e);
        String message = e.getMessage();
        if (e.hasFieldErrors()) {
            message = e.getFieldErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
        }
        Response response =  Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(IllegalArgumentException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleException(OAuth2AuthenticationException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid request.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleException(InsufficientAuthenticationException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Unauthorized request.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleException(ResourceNotFoundException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(RespMessage.RESOURCE_NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleException(AccessDeniedException e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(RespMessage.ACCESS_DENIED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        log.info("Error: ", e);
        Response response =  Response.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage() + e.getClass())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

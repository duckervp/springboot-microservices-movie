package com.duckervn.activityservice.controller;

import com.duckervn.activityservice.common.RespMessage;
import com.duckervn.activityservice.common.Response;
import com.duckervn.activityservice.domain.exception.ResourceNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
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

        if (e.status() == 404) {
            Response response = Response.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(RespMessage.RESOURCE_NOT_FOUND)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
            Response response = Response.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleException(AuthenticationException e) {
        log.info("Error: ", e);

        Response response =  Response.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Unauthorized request!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleException(AccessDeniedException e) {
        log.info("Error: ", e);

        Response response =  Response.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("Access denied!")
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(Exception e) {
        log.info("Error: ", e);

        Response response =  Response.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal Server Error")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

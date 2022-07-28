package com.duckervn.authservice.controller;

import com.duckervn.authservice.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class BaseController {
    public ResponseEntity<?> successfulResponse(Object data) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(Response.SUCCESS, true);
        body.put(Response.DATA, data);
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> successfulResponse() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(Response.SUCCESS, true);
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> failedResponse(HttpStatus httpStatus, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(Response.SUCCESS, false);
        body.put(Response.ERROR, message);
        log.info("Status: {}, error: {}", httpStatus, message);
        return new ResponseEntity<>(body, httpStatus);
    }
}

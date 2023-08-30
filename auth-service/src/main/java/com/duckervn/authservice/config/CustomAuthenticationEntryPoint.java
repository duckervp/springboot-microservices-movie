package com.duckervn.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) {
        String pathServer = request.getServletPath();
        Enumeration<String> authorizationHeaders = request.getHeaders(HttpHeaders.AUTHORIZATION);
        List<String> authorizationHeaderValues = Collections.list(authorizationHeaders);

        for (String authorizationHeaderValue : authorizationHeaderValues) {
            log.info("Token: {}", authorizationHeaderValue);
        }
        log.info("FORWARD dispatch for {} URL : {}", request.getMethod(), pathServer);
        handlerExceptionResolver.resolveException(request, response, null, ex);
    }

}

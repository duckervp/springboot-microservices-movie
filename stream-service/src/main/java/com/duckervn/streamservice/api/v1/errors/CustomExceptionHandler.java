package com.duckervn.streamservice.api.v1.errors;

import com.duckervn.streamservice.common.Response;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.channels.ClosedChannelException;

@Configuration
@Order(-2)
@Slf4j
public class CustomExceptionHandler extends org.springframework.web.reactive.handler.WebFluxResponseStatusExceptionHandler {

    public CustomExceptionHandler() {
        log.info("Initialising custom exception handler");
    }

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, Throwable ex) {
        if (ex.getCause() instanceof ClosedChannelException) {
            log.info("CLOSED CHANNEL ERROR OBSERVED");
            // ignore after log
            return Mono.empty();
        }

        ServerHttpResponse response = exchange.getResponse();

        HttpHeaders responseHeaders = response.getHeaders();

        int status = super.determineRawStatusCode(ex);

        Response error = Response.builder()
                .code(status)
                .message(ex.getMessage())
                .build();

        if (status == HttpStatus.NOT_FOUND.value()) {
            log.warn(buildResponse(exchange, ex));
        } else if (status == HttpStatus.BAD_REQUEST.value()) {
            log.warn(buildResponse(exchange, ex)); // bad routes are warn level
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            log.error(buildResponse(exchange, ex), ex); // internal server errors are debug level
        }

        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        responseHeaders.setCacheControl(CacheControl.noCache());
        response.setRawStatusCode(error.getCode());

        byte[] bytes = error.toString().getBytes();
        DataBuffer buffer = response.bufferFactory().wrap(bytes);

        return response
                .writeWith(Mono.just(buffer));
    }

    private String buildResponse(ServerWebExchange exchange, Throwable ex) {
        return ("Failed to handle error in request [" + exchange.getRequest().getMethod() + " " + exchange.getRequest().getURI() + "]: " + ex.getMessage());
    }
}

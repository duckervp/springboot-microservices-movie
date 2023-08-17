package com.duckervn.activityservice.filter;

import com.duckervn.activityservice.common.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestLogFilter extends GenericFilterBean {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        chain.doFilter(requestWrapper, responseWrapper);
        logRequest(requestWrapper);
        logResponse(responseWrapper);
    }

    @SneakyThrows
    private void logRequest(ContentCachingRequestWrapper request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, Object> headers = headersToMap(Collections.list(request.getHeaderNames()), request::getHeader);
        String body = new String(request.getContentAsByteArray());
        body = body.replaceAll("\\s+", "");
        log.info(Constants.REQUEST);
        log.info("ENDPOINT: {}", request.getMethod() + request.getRequestURI());
        log.info("PARAMETERS: {}", mapToString(parameters));
        log.info("HEADERS: {}", mapToString(headers));
        log.info("BODY: {}", body);

    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        Map<String, Object> headers = headersToMap(response.getHeaderNames(), response::getHeader);
        String body = new String(response.getContentAsByteArray());
        body = body.replaceAll("\\s+", "");
        log.info(Constants.RESPONSE);
        log.info("HEADERS: {}", mapToString(headers));
        log.info("BODY: {}", body);
        response.copyBodyToResponse();
    }

    @SneakyThrows
    private String mapToString(Map<String, ?> map) {
        return objectMapper.writeValueAsString(map);
    }

    @SneakyThrows
    private Map<String, Object> headersToMap(Collection<String> headerNames, Function<String, String> headerValueResolver) {
        Map<String, Object> result = new LinkedHashMap<>();
        headerNames.forEach(header -> result.put(header, headerValueResolver.apply(header)));
        return result;

    }
}

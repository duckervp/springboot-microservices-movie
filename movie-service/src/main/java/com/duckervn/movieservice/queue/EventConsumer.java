package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.common.Constants;
import com.duckervn.movieservice.common.TypeRef;
import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.service.IFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final ObjectMapper objectMapper;

    private final IFileService fileService;

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @KafkaListener(topics = "${topic.movie}")
    @SendTo
    public String consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        Map<String, Object> resultMap = new HashMap<>();

        if (Objects.nonNull(event)) {
            resultMap.put(Constants.EVENT_ATTR, event);
            if (event.equals(serviceConfig.getFindMovieStoredFileEvent())) {
                resultMap.put(Constants.DATA_ATTR, fileService.getAllStoredFiles());
            }
        }

        return objectMapper.writeValueAsString(resultMap);
    }
}

package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.service.IFileService;
import com.fasterxml.jackson.core.type.TypeReference;
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
    @KafkaListener(topics = "${topic.movie.request}")
    @SendTo("dev-movie-response")
    public String consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, new TypeReference<>() {
        });

        String event = null;

        if (requestMap.containsKey("event")) {
            event = (String) requestMap.get("event");
        }

        Map<String, Object> resultMap = new HashMap<>();

        if (Objects.nonNull(event)) {
            if (event.equals("movie-stored-file.find")) {
                resultMap.put("data", fileService.getAllStoredFiles());
            }
        }

        return objectMapper.writeValueAsString(resultMap);
    }
}

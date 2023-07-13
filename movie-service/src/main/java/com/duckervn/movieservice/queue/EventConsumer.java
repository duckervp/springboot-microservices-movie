package com.duckervn.movieservice.queue;

import com.duckervn.movieservice.config.ServiceConfig;
import com.duckervn.movieservice.service.IFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
    @RabbitListener(queues = "${queue.movieQueue.name}")
    public Map<String, Object> consumeMessageFromQueue(Map<String, Object> requestMap) {
        log.info("Consume message: {}", objectMapper.writeValueAsString(requestMap));

        String routingKey = null;

        if (requestMap.containsKey("routingKey")) {
            routingKey = (String) requestMap.get("routingKey");
        }

        if (Objects.nonNull(routingKey)) {
            if (routingKey.equals(serviceConfig.getFindMovieStoredFileRK())) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("data", fileService.getAllStoredFiles());
                return resultMap;
            }
        }

        return null;
    }
}

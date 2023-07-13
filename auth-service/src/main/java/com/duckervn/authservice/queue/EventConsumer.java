package com.duckervn.authservice.queue;

import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IFileStoreService;
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

    private final UserRepository userRepository;

    private final IFileStoreService fileStoreService;

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @KafkaListener(topics = "${topic.user}")
    @SendTo
    public String consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, new TypeReference<>() {});

        String userId = null;
        String event = null;

        if (requestMap.containsKey("data")) {
            Map<String, Object> data = objectMapper.convertValue(requestMap.get("data"), new TypeReference<>() {
            });
            if (Objects.nonNull(data) && data.containsKey("userId")) {
                userId = (String) data.get("userId");
            }
        }

        if (requestMap.containsKey("event")) {
            event = (String) requestMap.get("event");
        }

        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("event", event);

        if (Objects.nonNull(event)) {
            if (Objects.nonNull(userId) && event.equals("user.exist")) {
                resultMap.put("userId", userId);
                resultMap.put("exist", userRepository.existsById(userId));
            } else if (Objects.nonNull(userId) && event.equals("user.find")) {
                Map<String, Object> map = objectMapper.convertValue(userRepository.findById(userId).orElse(null), new TypeReference<>() {
                });
                resultMap.putAll(map);
            } else {
                if (event.equals("user-stored-file.find")) {
                    resultMap.put("data", fileStoreService.getStoredImageUrls());
                }
            }
        }
        return objectMapper.writeValueAsString(resultMap);
    }
}

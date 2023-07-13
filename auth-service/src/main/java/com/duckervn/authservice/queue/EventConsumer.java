package com.duckervn.authservice.queue;

import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IFileStoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
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

    private final UserRepository userRepository;

    private final IFileStoreService fileStoreService;

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @RabbitListener(queues = "${queue.userQueue.name}")
    public Map<String, Object> consumeMessageFromQueue(Map<String, Object> requestMap) {
        log.info("Consume message: {}", objectMapper.writeValueAsString(requestMap));

        String userId = null;
        String routingKey = null;

        if (requestMap.containsKey("data")) {
            Map<String, Object> data = objectMapper.convertValue(requestMap.get("data"), new TypeReference<>() {
            });
            if (Objects.nonNull(data) && data.containsKey("userId")) {
                userId = (String) data.get("userId");
            }
        }

        if (requestMap.containsKey("routingKey")) {
            routingKey = (String) requestMap.get("routingKey");
        }

        Map<String, Object> resultMap = new HashMap<>();
        if (Objects.nonNull(routingKey)) {
            if (Objects.nonNull(userId) && routingKey.equals(serviceConfig.getCheckUserExistRK())) {
                resultMap.put("userId", userId);
                resultMap.put("exist", userRepository.existsById(userId));
            } else if (Objects.nonNull(userId) && routingKey.equals(serviceConfig.getFindUserRK())) {
                Map<String, Object> map = objectMapper.convertValue(userRepository.findById(userId).orElse(null), new TypeReference<>() {
                });
                resultMap.putAll(map);
            } else {
                if (routingKey.equals(serviceConfig.getFindUserStoredFileRK())) {
                    resultMap.put("data", fileStoreService.getStoredImageUrls());
                }
            }
        }
        return resultMap;
    }
}

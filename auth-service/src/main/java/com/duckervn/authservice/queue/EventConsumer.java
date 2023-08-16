package com.duckervn.authservice.queue;

import com.duckervn.authservice.common.Constants;
import com.duckervn.authservice.common.TypeRef;
import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.repository.UserRepository;
import com.duckervn.authservice.service.IFileStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String userId = null;
        String event = null;

        if (requestMap.containsKey(Constants.DATA_ATTR)) {
            Map<String, Object> data = objectMapper.convertValue(requestMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
            if (Objects.nonNull(data) && data.containsKey("userId")) {
                userId = (String) data.get("userId");
            }
        }

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        Map<String, Object> resultMap = new HashMap<>();

        if (Objects.nonNull(event)) {
            resultMap.put(Constants.EVENT_ATTR, event);
            if (event.equals(serviceConfig.getCheckUserExistEvent())) {
                boolean exist = false;
                if (Objects.nonNull(userId)) {
                    exist = userRepository.existsById(userId);
                }
                Map<String, Object> response = new HashMap<>();
                response.put("userId", userId);
                response.put("exist", exist);
                resultMap.put(Constants.DATA_ATTR, response);
            } else if (event.equals(serviceConfig.getFindUserEvent()) && Objects.nonNull(userId)) {
                Map<String, Object> response = objectMapper.convertValue(userRepository.findById(userId).orElse(null), TypeRef.MAP_STRING_OBJECT);
                resultMap.put(Constants.DATA_ATTR, response);
            } else if (event.equals(serviceConfig.getFindUserStoredFileEvent())) {
                resultMap.put(Constants.DATA_ATTR, fileStoreService.getStoredImageUrls());
            } else if (event.equals(serviceConfig.getUpdateUserExpEvent()) && Objects.nonNull(userId)) {
                User user = userRepository.findById(userId).orElse(null);
                if (Objects.nonNull(user)) {
                    user.setExp(Objects.nonNull(user.getExp()) ? user.getExp() + 2L : 2L);
                    user.setModifiedAt(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
        }
        return objectMapper.writeValueAsString(resultMap);
    }
}

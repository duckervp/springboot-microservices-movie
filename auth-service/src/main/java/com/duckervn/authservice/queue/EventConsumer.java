package com.duckervn.authservice.queue;

import com.duckervn.authservice.common.Constants;
import com.duckervn.authservice.common.TypeRef;
import com.duckervn.authservice.config.ServiceConfig;
import com.duckervn.authservice.domain.entity.User;
import com.duckervn.authservice.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @KafkaListener(topics = "${topic.user}")
    @SendTo
    public void consumeMessageFromQueue(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        if (Objects.nonNull(event)) {
            String userId = null;
            if (requestMap.containsKey(Constants.DATA_ATTR)) {
                Map<String, Object> data = objectMapper.convertValue(requestMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
                if (Objects.nonNull(data) && data.containsKey("userId")) {
                    userId = (String) data.get("userId");
                }
            }
            if (event.equals(serviceConfig.getUpdateUserExpEvent()) && Objects.nonNull(userId)) {
                User user = userRepository.findById(userId).orElse(null);
                if (Objects.nonNull(user)) {
                    user.setExp(Objects.nonNull(user.getExp()) ? user.getExp() + 2L : 2L);
                    user.setModifiedAt(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
        }
    }
}

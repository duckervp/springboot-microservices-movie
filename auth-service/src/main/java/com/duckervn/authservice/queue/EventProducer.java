package com.duckervn.authservice.queue;

import com.duckervn.authservice.config.ServiceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProducer {

    private final ServiceConfig serviceConfig;

    private final RabbitTemplate rabbitTemplate;

    @SneakyThrows
    public void publish(String routingKey, Object data) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("routingKey", routingKey);
        requestMap.put("data", data);
        log.info("request: {}", requestMap);

        rabbitTemplate.convertAndSend(serviceConfig.getExchange(), routingKey, requestMap);
    }
}

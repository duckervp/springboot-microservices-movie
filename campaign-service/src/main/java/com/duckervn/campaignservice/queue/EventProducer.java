package com.duckervn.campaignservice.queue;

import com.duckervn.campaignservice.config.ServiceConfig;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final ServiceConfig serviceConfig;

    private final RabbitTemplate rabbitTemplate;

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    @SneakyThrows
    public ListenableFuture<Map<String, Object>> publish(String routingKey, Object data, boolean wait) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("routingKey", routingKey);
        requestMap.put("data", data);
        if (wait) {
            return asyncRabbitTemplate.convertSendAndReceive(serviceConfig.getExchange(), routingKey, requestMap);
        } else {
            rabbitTemplate.convertAndSend(serviceConfig.getExchange(), routingKey, requestMap);
            return null;
        }
    }
}

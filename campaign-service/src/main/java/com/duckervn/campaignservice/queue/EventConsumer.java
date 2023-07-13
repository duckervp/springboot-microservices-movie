package com.duckervn.campaignservice.queue;

import com.duckervn.campaignservice.config.ServiceConfig;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.service.impl.CampaignRecipientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final CampaignRecipientService campaignRecipientService;

    private final ObjectMapper objectMapper;

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @RabbitListener(queues = "${queue.campaignQueue.name}")
    public void consumeMessage(Map<String, Object> requestMap) {
        log.info("Consume message: {}", objectMapper.writeValueAsString(requestMap));

        String routingKey = null;

        if (requestMap.containsKey("routingKey")) {
            routingKey = (String) requestMap.get("routingKey");
        }

        if (Objects.nonNull(routingKey)) {
            if (routingKey.equals(serviceConfig.getAddRecipientRK())) {
                if (requestMap.containsKey("data")) {
                    CampaignRecipientInput campaignRecipientInput = objectMapper.convertValue(requestMap.get("data"), CampaignRecipientInput.class);
                    campaignRecipientService.save(campaignRecipientInput);
                }
            }
        }

    }
}

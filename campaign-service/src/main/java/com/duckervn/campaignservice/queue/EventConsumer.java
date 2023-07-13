package com.duckervn.campaignservice.queue;

import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.service.impl.CampaignRecipientService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final CampaignRecipientService campaignRecipientService;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @KafkaListener(topics = "${topic.campaign}")
    public void consumeMessage(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, new TypeReference<>() {
        });

        log.info("Consume message: {}", requestMap);

        String event = null;

        if (requestMap.containsKey("event")) {
            event = (String) requestMap.get("event");
        }

        if (Objects.nonNull(event)) {
            if (event.equals("campaign-recipient.add")) {
                if (requestMap.containsKey("data")) {
                    CampaignRecipientInput campaignRecipientInput = objectMapper.convertValue(requestMap.get("data"), CampaignRecipientInput.class);
                    campaignRecipientService.save(campaignRecipientInput);
                }
            }
        }

    }
}

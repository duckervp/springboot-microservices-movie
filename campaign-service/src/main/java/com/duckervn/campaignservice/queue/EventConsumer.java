package com.duckervn.campaignservice.queue;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.TypeRef;
import com.duckervn.campaignservice.config.ServiceConfig;
import com.duckervn.campaignservice.domain.model.addcampaignrecipient.CampaignRecipientInput;
import com.duckervn.campaignservice.service.impl.CampaignRecipientService;
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

    private final ServiceConfig serviceConfig;

    @SneakyThrows
    @KafkaListener(topics = "${topic.campaign}")
    public void consumeMessage(String request) {
        log.info("Consume message: {}", request);

        Map<String, Object> requestMap = objectMapper.readValue(request, TypeRef.MAP_STRING_OBJECT);

        String event = null;

        if (requestMap.containsKey(Constants.EVENT_ATTR)) {
            event = (String) requestMap.get(Constants.EVENT_ATTR);
        }

        if (Objects.nonNull(event)) {
            if (event.equals(serviceConfig.getAddCampaignRecipientEvent())) {
                if (requestMap.containsKey(Constants.DATA_ATTR)) {
                    Map<String, Object> data = objectMapper.readValue((String) requestMap.get(Constants.DATA_ATTR), TypeRef.MAP_STRING_OBJECT);
                    Long campaignId = Long.parseLong(data.getOrDefault("campaignId", "-1").toString());
                    CampaignRecipientInput campaignRecipientInput = objectMapper.convertValue(requestMap.get("campaignRecipientInput"), CampaignRecipientInput.class);
                    campaignRecipientService.save(campaignId, campaignRecipientInput);
                }
            }
        }

    }
}

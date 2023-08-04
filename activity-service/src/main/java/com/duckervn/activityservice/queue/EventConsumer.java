package com.duckervn.activityservice.queue;

import com.duckervn.activityservice.common.Constants;
import com.duckervn.activityservice.common.TypeRef;
import com.duckervn.activityservice.config.ServiceConfig;
import com.duckervn.activityservice.domain.model.addrating.RatingInput;
import com.duckervn.activityservice.service.impl.RatingService;
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
    private final RatingService campaignRecipientService;

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
                    RatingInput ratingInput = objectMapper.convertValue(requestMap.get(Constants.DATA_ATTR), RatingInput.class);
                    campaignRecipientService.save(ratingInput);
                }
            }
        }

    }
}

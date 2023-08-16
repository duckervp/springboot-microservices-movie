package com.duckervn.activityservice.queue;

import com.duckervn.activityservice.common.Constants;
import com.duckervn.activityservice.common.TypeRef;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public void publish(String topic, String event, Object data) {
        log.info("Produce message: {topic: {}, event: {}, data: {}}", topic, event, data);
        ProducerRecord<String, String> record = populateData(topic, event, data);
        kafkaTemplate.send(record);
    }

    @SneakyThrows
    private ProducerRecord<String, String> populateData(String topic, String event, Object data) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(Constants.EVENT_ATTR, event);
        requestMap.put(Constants.DATA_ATTR, data);
        String requestString = objectMapper.writeValueAsString(requestMap);

        return new ProducerRecord<>(topic, 0, null, requestString);
    }
}

package com.duckervn.campaignservice.queue;

import com.duckervn.campaignservice.common.Constants;
import com.duckervn.campaignservice.common.TypeRef;
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

    private final ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public Map<String, Object> publishAndWait(String topic, String replyTopic, String event, Object data) {
        log.info("Produce message: {topic: {}, event: {}, data: {}}", topic, event, data);

        ProducerRecord<String, String> record = populateData(topic, replyTopic, event, data);

        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);
        return objectMapper.readValue(consumerRecord.value(), TypeRef.MAP_STRING_OBJECT);
    }

    public void publish(String topic, String event, Object data) {
        log.info("Produce message: {topic: {}, event: {}, data: {}}", topic, event, data);
        ProducerRecord<String, String> record = populateData(topic, null, event, data);
        kafkaTemplate.send(record);
    }

    @SneakyThrows
    private ProducerRecord<String, String> populateData(String topic, String replyTopic, String event, Object data) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put(Constants.EVENT_ATTR, event);
        requestMap.put(Constants.DATA_ATTR, data);
        String requestString = objectMapper.writeValueAsString(requestMap);

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, 0, null, requestString);

        if (Objects.nonNull(replyTopic)) {
            producerRecord.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes());
        }

        return producerRecord;
    }
}

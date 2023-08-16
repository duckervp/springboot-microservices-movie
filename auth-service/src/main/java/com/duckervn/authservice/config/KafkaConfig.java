package com.duckervn.authservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final ServiceConfig serviceConfig;

    @Bean
    public NewTopic campaignTopic() {
        return TopicBuilder.name(serviceConfig.getCampaignTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(serviceConfig.getUserTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

}

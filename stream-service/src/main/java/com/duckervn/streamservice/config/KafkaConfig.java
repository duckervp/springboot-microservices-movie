package com.duckervn.streamservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    private final ServiceConfig serviceConfig;

    @Bean
    public NewTopic movieTopic() {
        return TopicBuilder.name(serviceConfig.getMovieTopic())
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

    @Bean
    public NewTopic userToStreamReplyTopic() {
        return TopicBuilder.name(serviceConfig.getUserToStreamReplyTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic movieToStreamReplyTopic() {
        return TopicBuilder.name(serviceConfig.getMovieToStreamReplyTopic())
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(
            ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate,
            ConsumerFactory<String, Object> cf
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setReplyTemplate(replyKafkaTemplate);
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(cf.getConfigurationProperties()));
        return factory;
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> pf, KafkaMessageListenerContainer<String, String> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
        ContainerProperties containerProperties = new ContainerProperties(serviceConfig.getUserToStreamReplyTopic(), serviceConfig.getMovieToStreamReplyTopic());
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }
}

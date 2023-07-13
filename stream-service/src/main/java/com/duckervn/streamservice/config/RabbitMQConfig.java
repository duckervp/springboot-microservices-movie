package com.duckervn.streamservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final ServiceConfig serviceConfig;

    @Bean
    public Queue movieQueue() {
        return new Queue(serviceConfig.getMovieQueue(), false);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(serviceConfig.getUserQueue(), false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(serviceConfig.getExchange());
    }

    @Bean
    public Binding movieBinding(Queue movieQueue , TopicExchange exchange) {
        return BindingBuilder.bind(movieQueue).to(exchange).with(serviceConfig.getMovieRoutingKey());
    }

    @Bean
    public Binding userBinding(Queue userQueue , TopicExchange exchange) {
        return BindingBuilder.bind(userQueue).to(exchange).with(serviceConfig.getUserRoutingKey());
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public AsyncRabbitTemplate asyncRabbitTemplate(
            RabbitTemplate rabbitTemplate){
        return new AsyncRabbitTemplate(rabbitTemplate);
    }
}

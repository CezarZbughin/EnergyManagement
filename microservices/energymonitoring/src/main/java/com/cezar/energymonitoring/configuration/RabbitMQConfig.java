package com.cezar.energymonitoring.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    static public final String topicExchangeName = "device-exchange";
    static public final String eventQueue = "device-save-event-queue";
    static public final String eventQueueRoutingKey = "device.crud.#";
    static public final String consumptionQueue = "device-consumption-event-queue";
    static public final String consumptionQueueRoutingKey = "device.consumption.#";

    @Bean
    Queue eventQueue() {
        return new Queue(eventQueue, false);
    }

    @Bean
    Queue consumptionQueue() {
        return new Queue(consumptionQueue, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    public Binding binding(){

        return BindingBuilder
                .bind(eventQueue())
                .to(exchange())
                .with(eventQueueRoutingKey);
    }

    // binding between json queue and exchange using routing key
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder
                .bind(consumptionQueue())
                .to(exchange())
                .with(consumptionQueueRoutingKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

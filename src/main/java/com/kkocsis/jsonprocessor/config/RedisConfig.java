package com.kkocsis.jsonprocessor.config;

import com.kkocsis.jsonprocessor.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    private static final String LISTENER_METHOD_NAME = "save";

    @Value("${jsonprocessor.redis.topic.pattern}")
    private String redisTopicPattern;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(redisTopicPattern));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MessageService messageService) {
        return new MessageListenerAdapter(messageService, LISTENER_METHOD_NAME);
    }


}

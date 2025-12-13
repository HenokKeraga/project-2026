package org.example.twitter.config;

import org.example.twitter.dto.TweetDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// TimelineService/src/main/java/com/example/timelineservice/config/RedisConfig.java
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // In real life: configure host/port, password, etc.
        return new LettuceConnectionFactory();
    }


    @Bean
    public RedisTemplate<String, Long> timelineRedisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer(Long.class));
        return template;
    }

    // New: tweet objects
    @Bean
    public RedisTemplate<String, TweetDto> tweetRedisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, TweetDto> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer(Long.class));
        return template;
    }
}

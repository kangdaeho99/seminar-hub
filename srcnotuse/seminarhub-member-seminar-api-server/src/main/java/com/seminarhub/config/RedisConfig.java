package com.seminarhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * [ 2023-07-30 daeho.kang ]
 * Description : Redis 연결설정
 *
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description : Redis 서버에 대한 연결을 설정하는 Bean을 생성합니다.
     *  Lettuce Redis Client를 사용하며, Redis Template에서 사용됩니다.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * [ 2023-07-26 daeho.kang ]
     * Description : RedisRefreshTokenTemplate을 설정하는 Bean을 생성합니다.
     * String 타입의 key와 value를 사용하는 Redis Template을 생성합니다.
     */
    @Bean
    public RedisTemplate<String, String> redisRefreshTokenTemplate(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }

}

package com.seminarhub.config;

import com.seminarhub.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {
    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }
}

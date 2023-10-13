package com.seminarhub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class SeminarHubMonolithic {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubMonolithic.class, args);;
    }
}
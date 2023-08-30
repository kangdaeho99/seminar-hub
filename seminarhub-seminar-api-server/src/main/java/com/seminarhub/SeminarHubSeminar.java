package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * [ 2023-08-19 daeho.kang ]
 * Description : Main Class of SeminarHubSeminar
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaAuditing
public class SeminarHubSeminar {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubSeminar.class, args);;
    }
}
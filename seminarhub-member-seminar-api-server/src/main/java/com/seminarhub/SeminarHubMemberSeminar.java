package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * [ 2023-08-30 daeho.kang ]
 * Description: Main class for SeminarHubMemberSeminar application
 */
@EnableJpaAuditing
@EnableDiscoveryClient
@SpringBootApplication
public class SeminarHubMemberSeminar {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubMemberSeminar.class, args);
    }
}
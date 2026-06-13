package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * [ 2023-08-19 daeho.kang ]
 * Description : SeminarHubEureka Main
 * It Discovers the Service
 */
@EnableEurekaServer
@SpringBootApplication
public class SeminarHubEureka {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubEureka.class, args);
    }
}
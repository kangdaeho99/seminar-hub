package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * [ 2023-08-18 daeho.kang ]
 * Description : SeminarHubConfig Main
 *
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class SeminarHubConfig {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubConfig.class, args);
    }
}
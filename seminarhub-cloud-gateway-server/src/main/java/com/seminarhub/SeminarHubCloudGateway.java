package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * [ 2023-08-18 daeho.kang ]
 * Description : SeminarHubCloudGateway Main
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SeminarHubCloudGateway {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubCloudGateway.class, args);
    }
}
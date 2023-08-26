package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SeminarHubMemberSeminar {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        SpringApplication.run(SeminarHubMemberSeminar.class, args);
    }
}
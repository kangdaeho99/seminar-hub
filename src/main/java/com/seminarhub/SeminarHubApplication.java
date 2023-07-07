package com.seminarhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SeminarHubApplication {
	public static void main(String[] args) {
		SpringApplication.run(SeminarHubApplication.class, args);
	}
}

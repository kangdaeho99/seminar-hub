package com.apress.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SeminarHubBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeminarHubBatchApplication.class, args);
	}

}

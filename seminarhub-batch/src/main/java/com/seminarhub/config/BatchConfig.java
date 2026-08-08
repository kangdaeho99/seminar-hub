package com.seminarhub.config;

import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    @Override
    protected ExecutionContextSerializer getExecutionContextSerializer() {
        return new Jackson2ExecutionContextStringSerializer();
    }

    // @Bean
    // public JobLauncherApplicationRunner jobLauncherApplicationRunner(
    //         JobLauncher jobLauncher,
    //         JobExplorer jobExplorer,
    //         JobRepository jobRepository) {
        
    //     return new JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository);
    // }
}


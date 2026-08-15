package com.seminarhub.config;

import com.seminarhub.entity.Delivery;
import com.seminarhub.listener.DeliveryChunkListener;
import com.seminarhub.listener.DeliveryProcessListener;
import com.seminarhub.listener.DeliveryReadListener;
import com.seminarhub.listener.DeliverySkipListener;
import com.seminarhub.listener.DeliveryWriteListener;
import com.seminarhub.policy.DeliverySkipPolicy;
import com.seminarhub.processor.DeliveryItemProcessor;
import com.seminarhub.reader.DeliveryKeysetItemReader;
import com.seminarhub.repository.DeliveryRepository;
import com.seminarhub.validator.DeliveryJobParametersValidator;
import com.seminarhub.writer.DeliveryItemWriter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.concurrent.Future;

@Configuration
@RequiredArgsConstructor
public class DeliveryBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DeliveryRepository deliveryRepository;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 500;

    @Bean
    public JobParametersValidator deliveryJobParametersValidator() {
        return new DeliveryJobParametersValidator();
    }

    @Bean
    public Job deliveryStatusUpdateJob() {
        return new JobBuilder("deliveryStatusUpdateJob", jobRepository)
                .validator(deliveryJobParametersValidator())
                .start(deliveryStatusUpdateStep())
                .build();
    }

    @Bean
    public Step deliveryStatusUpdateStep() {
        return new StepBuilder("deliveryStatusUpdateStep", jobRepository)
                .<Delivery, Future<Delivery>>chunk(CHUNK_SIZE, transactionManager)
                .reader(deliveryReader(null, null))
                .processor(asyncDeliveryProcessor())
                .writer(asyncDeliveryWriter())
                .faultTolerant()
                .processorNonTransactional()
                .skipPolicy(new DeliverySkipPolicy())
                .listener(new DeliveryChunkListener())
                .listener(new DeliveryReadListener())
                .listener(new DeliveryProcessListener())
                .listener(new DeliveryWriteListener())
                .listener(new DeliverySkipListener())
                .build();
    }

    @Bean
    @StepScope
    public DeliveryKeysetItemReader deliveryReader(
            @Value("#{jobParameters['startAt']}") LocalDateTime startAt,
            @Value("#{jobParameters['endAt']}") LocalDateTime endAt) {
        return new DeliveryKeysetItemReader(
                deliveryRepository,
                startAt,
                endAt,
                CHUNK_SIZE);
    }

    @Bean
    public DeliveryItemProcessor deliveryProcessor() {
        return new DeliveryItemProcessor();
    }

    @Bean
    public TaskExecutor deliveryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);   // 스레드 축소
        executor.setMaxPoolSize(10);   // 스레드 축소 (300 TPS 방어)
        executor.setQueueCapacity(500); // CHUNK_SIZE에 맞춰 큐 확장
        executor.setThreadNamePrefix("async-batch-");
        executor.setRejectedExecutionHandler(new BlockingRejectedExecutionHandler());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }

    @Bean
    public AsyncItemProcessor<Delivery, Delivery> asyncDeliveryProcessor() {
        AsyncItemProcessor<Delivery, Delivery> processor = new AsyncItemProcessor<>();
        processor.setDelegate(deliveryProcessor());
        processor.setTaskExecutor(deliveryTaskExecutor());
        return processor;
    }

    @Bean
    public DeliveryItemWriter deliveryWriter() {
        return new DeliveryItemWriter(entityManagerFactory);
    }

    @Bean
    public AsyncItemWriter<Delivery> asyncDeliveryWriter() {
        AsyncItemWriter<Delivery> writer = new AsyncItemWriter<>();
        writer.setDelegate(deliveryWriter());
        return writer;
    }
}
